package com.bardframework.bard.core;

import com.bardframework.bard.core.doc.Document;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.core.marker.Match;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class HandlerMethod {
    public Method method;
    public Class<? extends Servlet> servletClass;

    public List<AnnotatedHandler<? extends Filter>> annotatedFilters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedAdapters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedClassAdapters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedServletAdapters = new LinkedList<>();
    public List<HandlerParameter> parameters = new LinkedList<>();
    public List<HandlerField> fields = new LinkedList<>();

    private LinkedList<Adapter> runAdapters = new LinkedList<>();
    private LinkedList<Filter> runFilters = new LinkedList<>();
    private LinkedList<HandlerParameter> runParameters = new LinkedList<>();
    private LinkedList<HandlerField> runFields = new LinkedList<>();

    public Object run(Context context, GenericHandler o, boolean isCleanup) {
        Object result = null;
        try {
            result = before(context, o, isCleanup);
            // if context.exception is null, then the result is not he real result we need
            if (o instanceof Handler && context.exception == null) {
                context.result = result;
            }
        } catch (final InvocationTargetException e) {
            // throw by method.invoke, the cause is the true exception
            context.exception = e.getCause();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException e) {
            // these exceptions are caused by framework, so print them
            Util.getLogger().error("Error found in generic handler: {}", e);
        } catch (Throwable e) {
            // it is the exception throw by filters or injectors
            context.exception = e;
        }
        try {
            after();
        } catch (IllegalAccessException | InstantiationException e) {
            Util.getLogger().error("Error found in generic handler: {}", e);
        }
        return result;
    }

    public void genDoc(Document document, Class<? extends Servlet> servletClass)
        throws IllegalAccessException, InstantiationException {
        for (AnnotatedHandler<? extends Adapter> annotatedAdapter : annotatedAdapters) {
            Adapter adapter = annotatedAdapter.handlerClass.newInstance();
            adapter.annotation = annotatedAdapter.annotation;
            runAdapters.addFirst(adapter);
            adapter.generateDoc();
        }
    }

    private Object before(Context context, Object o, boolean isCleanup) throws Throwable {
        // TODO: This is really an ugly hacker for chained adapter

        // not match if there is no adapter defines on handler method
        if (o instanceof Handler && annotatedAdapters.size() == 0) {
            return NoAdapter.NO_ADAPTER;
        }

        List<AnnotatedHandler<? extends Adapter>> outAnnotatedAdapters = new LinkedList<>();
        outAnnotatedAdapters.addAll(annotatedServletAdapters);
        outAnnotatedAdapters.addAll(annotatedClassAdapters);
        for (AnnotatedHandler<? extends Adapter> annotatedAdapter : outAnnotatedAdapters) {
            Adapter adapter = annotatedAdapter.handlerClass.newInstance();
            adapter.annotation = annotatedAdapter.annotation;
            adapter.context = context;
            runAdapters.addFirst(adapter);
            HandlerMeta.runAnnotated(adapter, servletClass, Match.class);
        }

        for (AnnotatedHandler<? extends Adapter> annotatedAdapter : annotatedAdapters) {
            Adapter adapter = annotatedAdapter.handlerClass.newInstance();
            adapter.annotation = annotatedAdapter.annotation;
            adapter.context = context;
            runAdapters.addFirst(adapter);
            Object result = HandlerMeta.runAnnotated(adapter, servletClass, Match.class);
            if (result != NoAdapter.NO_ADAPTER && !((Boolean) result)) {
                return NoAdapter.NO_ADAPTER;
            }
        }

        for (AnnotatedHandler<? extends Filter> annotatedFilter : annotatedFilters) {
            Filter filter = annotatedFilter.handlerClass.newInstance();
            filter.annotation = annotatedFilter.annotation;
            filter.context = context;
            runFilters.addFirst(filter);
            HandlerMeta.runAnnotated(filter, servletClass, Before.class);
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }

        for (HandlerField field : fields) {
            runFields.addFirst(field);
            field.run(context, o);
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }


        Object[] args = new Object[parameters.size()];
        for (int i = 0; i < args.length; i++) {
            runParameters.addFirst(parameters.get(i));
            // context change in parameter will not change current context
            context.injectorVariable = null;
            parameters.get(i).run(context);
            args[i] = context.injectorVariable;
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }

        return method.invoke(o, args);
    }

    private void after() throws IllegalAccessException, InstantiationException {
        for (Adapter adapter : runAdapters) {
            HandlerMeta.runAnnotated(adapter, servletClass, After.class);
        }
        runAdapters = new LinkedList<>();

        for (HandlerField field : runFields) {
            field.cleanup();
        }
        runFields = new LinkedList<>();

        for (HandlerParameter parameter : runParameters) {
            parameter.cleanup();
        }
        runParameters = new LinkedList<>();

        for (Filter filter : runFilters) {
            HandlerMeta.runAnnotated(filter, servletClass, After.class);
        }
        runFilters = new LinkedList<>();
    }
}

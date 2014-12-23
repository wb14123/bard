package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.core.marker.Match;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class HandlerMethodRunner {

    private HandlerMethod handlerMethod;

    private LinkedList<Adapter> runAdapters = new LinkedList<>();
    private LinkedList<Filter> runFilters = new LinkedList<>();
    private LinkedList<HandlerParameterRunner> runParameters = new LinkedList<>();
    private LinkedList<HandlerFieldRunner> runFields = new LinkedList<>();

    public HandlerMethodRunner(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

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

    private Object before(Context context, Object o, boolean isCleanup) throws Throwable {
        // TODO: This is really an ugly hacker for chained adapter

        // not match if there is no adapter defines on handler method
        if (o instanceof Handler && handlerMethod.annotatedAdapters.size() == 0) {
            return NoAdapter.NO_ADAPTER;
        }

        List<AnnotatedHandler<? extends Adapter>> allAdapters = new LinkedList<>();
        allAdapters.addAll(handlerMethod.annotatedServletAdapters);
        allAdapters.addAll(handlerMethod.annotatedClassAdapters);
        allAdapters.addAll(handlerMethod.annotatedAdapters);
        int adapterLoop = 0;
        for (AnnotatedHandler<? extends Adapter> annotatedAdapter : allAdapters) {
            Adapter adapter = annotatedAdapter.newInstance();
            adapter.annotation = annotatedAdapter.annotation;
            adapter.context = context;
            adapter.isFinal =
                adapterLoop++ >= handlerMethod.annotatedServletAdapters.size()
                    + handlerMethod.annotatedClassAdapters.size();
            runAdapters.addFirst(adapter);
            Object result =
                HandlerMeta.runAnnotated(adapter, handlerMethod.servletClass, Match.class);
            if (result != NoAdapter.NO_ADAPTER && !((Boolean) result)) {
                return NoAdapter.NO_ADAPTER;
            }
        }

        for (AnnotatedHandler<? extends Filter> annotatedFilter : handlerMethod.annotatedFilters) {
            Filter filter = annotatedFilter.newInstance();
            filter.annotation = annotatedFilter.annotation;
            filter.context = context;
            runFilters.addFirst(filter);
            HandlerMeta.runAnnotated(filter, handlerMethod.servletClass, Before.class);
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }

        for (HandlerField field : handlerMethod.fields) {
            HandlerFieldRunner fieldRunner = new HandlerFieldRunner(field);
            runFields.addFirst(fieldRunner);
            fieldRunner.run(context, o);
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }


        Object[] args = new Object[handlerMethod.parameters.size()];
        for (int i = 0; i < args.length; i++) {
            HandlerParameterRunner parameterRunner =
                new HandlerParameterRunner(handlerMethod.parameters.get(i));
            runParameters.addFirst(parameterRunner);
            // context change in parameter will not change current context
            context.injectorVariable = null;
            parameterRunner.run(context);
            args[i] = context.injectorVariable;
            if (context.exception != null && !isCleanup) {
                return null;
            }
        }

        return handlerMethod.method.invoke(o, args);
    }

    private void after() throws IllegalAccessException, InstantiationException {
        for (Adapter adapter : runAdapters) {
            HandlerMeta.runAnnotated(adapter, handlerMethod.servletClass, After.class);
        }
        runAdapters = new LinkedList<>();

        for (HandlerFieldRunner field : runFields) {
            field.cleanup();
        }
        runFields = new LinkedList<>();

        for (HandlerParameterRunner parameter : runParameters) {
            parameter.cleanup();
        }
        runParameters = new LinkedList<>();

        for (Filter filter : runFilters) {
            HandlerMeta.runAnnotated(filter, handlerMethod.servletClass, After.class);
        }
        runFilters = new LinkedList<>();
    }
}

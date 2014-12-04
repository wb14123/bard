package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class HandlerField {
    public Field field;
    public Class<? extends Servlet> servletClass;

    public List<AnnotatedHandler<? extends Injector>> annotatedInjectors = new LinkedList<>();

    private LinkedList<Injector> runInjectors = new LinkedList<>();
    private boolean setted = false;

    public void run(Context context, Object c)
        throws IllegalAccessException, InstantiationException {
        if (setted || annotatedInjectors.size() == 0) {
            return;
        }
        context.injectorVariableType = field.getType();
        for (AnnotatedHandler<? extends Injector> annotatedInjector : annotatedInjectors) {
            Injector injector = annotatedInjector.handlerClass.newInstance();
            injector.annotation = annotatedInjector.annotation;
            injector.context = context;
            runInjectors.addFirst(injector);
            HandlerMeta.runAnnotated(injector, servletClass, Before.class);
            if (context.exception != null && !context.isExceptionHandled()) {
                return;
            }
        }
        field.set(c, context.injectorVariable);
        setted = true;
    }

    public void cleanup() throws IllegalAccessException, InstantiationException {
        for (Injector injector : runInjectors) {
            HandlerMeta.runAnnotated(injector, servletClass, After.class);
        }
        runInjectors = new LinkedList<>();
    }
}

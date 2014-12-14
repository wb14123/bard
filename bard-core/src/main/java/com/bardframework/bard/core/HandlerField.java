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

    public void run(Context context, Object c)
        throws HandlerFactory.HandlerInitException, IllegalAccessException {
        if (annotatedInjectors.size() == 0) {
            return;
        }
        context.injectorVariableType = field.getType();
        for (AnnotatedHandler<? extends Injector> annotatedInjector : annotatedInjectors) {
            Injector injector = HandlerMeta.handlerFactory.initInjector(
                annotatedInjector.handlerClass);
            injector.annotation = annotatedInjector.annotation;
            injector.context = context;
            runInjectors.addFirst(injector);
            HandlerMeta.runAnnotated(injector, servletClass, Before.class);
            if (context.exception != null) {
                return;
            }
        }
        field.set(c, context.injectorVariable);
    }

    public void cleanup() throws IllegalAccessException, InstantiationException {
        for (Injector injector : runInjectors) {
            HandlerMeta.runAnnotated(injector, servletClass, After.class);
        }
        runInjectors = new LinkedList<>();
    }
}

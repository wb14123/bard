package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class HandlerParameter {
    public Parameter parameter;
    public Class<? extends Servlet> servletClass;

    public List<AnnotatedHandler<? extends Injector>> annotatedInjectors = new LinkedList<>();

    private LinkedList<Injector> runInjectors = new LinkedList<>();

    public void run(Context context) throws HandlerFactory.HandlerInitException {
        context.injectorVariableType = parameter.getType();
        for (AnnotatedHandler<? extends Injector> annotatedInjector : annotatedInjectors) {
            Injector injector = annotatedInjector.newInstance();
            injector.annotation = annotatedInjector.annotation;
            injector.context = context;
            runInjectors.addFirst(injector);
            HandlerMeta.runAnnotated(injector, servletClass, Before.class);
            if (context.exception != null) {
                return;
            }
        }
    }

    public void cleanup() throws IllegalAccessException, InstantiationException {
        for (Injector injector : runInjectors) {
            HandlerMeta.runAnnotated(injector, servletClass, After.class);
        }
        runInjectors = new LinkedList<>();
    }
}

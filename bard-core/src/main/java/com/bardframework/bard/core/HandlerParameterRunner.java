package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.util.LinkedList;

public class HandlerParameterRunner {

    private HandlerParameter handlerParameter;
    private LinkedList<Injector> runInjectors = new LinkedList<>();

    public HandlerParameterRunner(HandlerParameter parameter) {
        handlerParameter = parameter;
    }

    public void run(Context context) throws HandlerFactory.HandlerInitException {
        context.injectorVariableType = handlerParameter.parameter.getType();
        for (AnnotatedHandler<? extends Injector> annotatedInjector : handlerParameter.annotatedInjectors) {
            Injector injector = annotatedInjector.newInstance();
            injector.annotation = annotatedInjector.annotation;
            injector.context = context;
            runInjectors.addFirst(injector);
            HandlerMeta.runAnnotated(injector, handlerParameter.servletClass, Before.class);
            if (context.exception != null) {
                return;
            }
        }
    }

    public void cleanup() throws IllegalAccessException, InstantiationException {
        for (Injector injector : runInjectors) {
            HandlerMeta.runAnnotated(injector, handlerParameter.servletClass, After.class);
        }
        runInjectors = new LinkedList<>();
    }
}

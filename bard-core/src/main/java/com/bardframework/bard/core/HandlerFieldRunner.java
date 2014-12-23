package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.util.LinkedList;

public class HandlerFieldRunner {

    private HandlerField handlerField;
    private LinkedList<Injector> runInjectors = new LinkedList<>();

    public HandlerFieldRunner(HandlerField handlerField) {
        this.handlerField = handlerField;
    }

    public void run(Context context, Object c)
        throws HandlerFactory.HandlerInitException, IllegalAccessException {
        if (handlerField.annotatedInjectors.size() == 0) {
            return;
        }
        context.injectorVariableType = handlerField.field.getType();
        for (AnnotatedHandler<? extends Injector> annotatedInjector : handlerField.annotatedInjectors) {
            Injector injector = annotatedInjector.newInstance();
            injector.annotation = annotatedInjector.annotation;
            injector.context = context;
            runInjectors.addFirst(injector);
            HandlerMeta.runAnnotated(injector, handlerField.servletClass, Before.class);
            if (context.exception != null) {
                return;
            }
        }
        handlerField.field.set(c, context.injectorVariable);
    }

    public void cleanup() throws IllegalAccessException, InstantiationException {
        for (Injector injector : runInjectors) {
            HandlerMeta.runAnnotated(injector, handlerField.servletClass, After.class);
        }
        runInjectors = new LinkedList<>();
    }
}

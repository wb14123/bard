package com.bardframework.bard.core;

import java.lang.annotation.Annotation;

public class AnnotatedHandler<HandlerClass extends GenericHandler> {
    public Annotation annotation;
    public Class<? extends HandlerClass> handlerClass;

    public AnnotatedHandler(Annotation annotation, Class<? extends HandlerClass> handlerClass) {
        this.annotation = annotation;
        this.handlerClass = handlerClass;
    }
}

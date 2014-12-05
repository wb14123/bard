package com.bardframework.bard.core;

import java.lang.annotation.Annotation;

/**
 * GenericHandler is a class that is the foundation of Adapter, Filter, Injector and Handler.
 * It could run its method with defined annotations.
 *
 * @param <AnnotationType> Which annotation is this GenericHandler bind to. No need on Handler.
 * @see Adapter
 * @see Filter
 * @see Injector
 * @see Handler
 */
public abstract class GenericHandler<AnnotationType extends Annotation> {
    /**
     * Used by {@link Filter}, {@link Injector} and
     * {@link Adapter}, the annotation instance that bind to it.
     */
    public AnnotationType annotation;
    /**
     * The context in this handler.
     */
    public Context context = new Context();

    public GenericHandler() {
    }
}

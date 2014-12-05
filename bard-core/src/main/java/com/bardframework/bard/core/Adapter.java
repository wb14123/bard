package com.bardframework.bard.core;

import com.bardframework.bard.core.doc.Api;

import java.lang.annotation.Annotation;

/**
 * <p>
 * An adapter is a mechanism to tell the framework whether the current handler method should handle the HTTP request.
 * This class is a foundation that could let you implement an adapter with an annotation.
 * </p>
 * <p>
 * You can define match functions with annotation {@link com.bardframework.bard.core.marker.Match} on it.
 * These functions must return a boolean value.
 * </p>
 * <p>
 * You can also define cleanup functions with annotation {@link com.bardframework.bard.core.marker.After}.
 * </p>
 *
 * @param <AnnotationType> Which annotation is this adapter bind to. It should be the same class you define in @BindTo .
 * @see BindTo
 */
public abstract class Adapter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {

    public Api api;

    public abstract void generateDoc();
}

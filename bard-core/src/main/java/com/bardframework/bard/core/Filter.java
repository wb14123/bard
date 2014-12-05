package com.bardframework.bard.core;

import com.bardframework.bard.core.doc.Api;

import java.lang.annotation.Annotation;

public abstract class Filter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {
    public Api api;

    public abstract void generateDoc();
}

package com.bardframework.bard.core;

import com.bardframework.bard.core.doc.DocParameter;

import java.lang.annotation.Annotation;

public abstract class Injector<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {
    public DocParameter docParameter;

    public abstract void generateDoc();
}

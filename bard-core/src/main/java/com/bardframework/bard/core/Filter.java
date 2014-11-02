package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Filter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {

    public void before()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        runMethods(Before.class);
    }

    public void after()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        runMethods(After.class);
    }

    public abstract void generateDoc();
}

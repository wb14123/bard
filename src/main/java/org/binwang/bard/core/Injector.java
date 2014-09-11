package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Injector<AnnotationType extends Annotation>
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

package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Filter<AnnotationType extends Annotation>
    extends GenericHandler<AnnotationType> {

    public static Filter newInstance(
        Class<? extends Filter> filterClass,
        Context context,
        Annotation annotation,
        AnnotationMapper mapper)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
        InstantiationException {
        Filter filter = filterClass.newInstance();
        filter.build(context, null, Object.class, annotation, mapper);
        return filter;
    }

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
}

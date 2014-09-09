package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Filter<AnnotationType extends Annotation> extends GenericHandler<Object, AnnotationType> {
    public Filter(Context context,
                  Object variable,
                  AnnotationType annotation,
                  AnnotationMapper mapper) {
        super(context, variable, annotation, mapper);
    }

    public static Filter newInstance(
            Class<? extends Filter> filterClass,
            Context context,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return filterClass.getDeclaredConstructor(Context.class, Object.class, Annotation.class, AnnotationMapper.class)
                .newInstance(context, null, annotation, mapper);
    }

    public void before()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        runMethods(Before.class);
    }

    public void after()
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        runMethods(After.class);
    }
}

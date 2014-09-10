package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Filter<AnnotationType extends Annotation> extends GenericHandler<AnnotationType> {

    public Filter(Context context, AnnotationType annotation, AnnotationMapper mapper) {
        super(context, null, Object.class, annotation, mapper);
    }

    public static Filter newInstance(
            Class<? extends Filter> filterClass,
            Class<? extends Annotation> annotationClass,
            Context context,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return filterClass.getDeclaredConstructor(Context.class, annotationClass, AnnotationMapper.class)
                .newInstance(context, annotation, mapper);
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

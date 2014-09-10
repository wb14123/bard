package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Injector<AnnotationType extends Annotation> extends GenericHandler<AnnotationType> {

    public Injector(Context context,
                    Object variable,
                    Class<?> returnType,
                    AnnotationType annotation,
                    AnnotationMapper mapper) {
        super(context, variable, returnType, annotation, mapper);
    }

    public static Injector newInstance(
            Class<? extends Injector> injectorClass,
            Class<? extends Annotation> annotationClass,
            Context context,
            Object variable,
            Class<?> returnType,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return injectorClass.getDeclaredConstructor(Context.class, Object.class, Class.class, annotationClass, AnnotationMapper.class)
                .newInstance(context, variable, returnType, annotation, mapper);
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

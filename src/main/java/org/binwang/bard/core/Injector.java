package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Injector<AnnotationType extends Annotation> extends GenericHandler<AnnotationType> {

    public static Injector newInstance(
            Class<? extends Injector> injectorClass,
            Context context,
            Object variable,
            Class<?> returnType,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Injector injector = injectorClass.newInstance();
        injector.build(context, variable, returnType, annotation, mapper);
        return injector;
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

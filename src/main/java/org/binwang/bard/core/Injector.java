package org.binwang.bard.core;

import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class Injector<ReturnType, AnnotationType extends Annotation> extends GenericHandler<ReturnType, AnnotationType> {
    public Injector() {
        super();
    }

    public Injector(Context context,
                    ReturnType variable,
                    AnnotationType annotation,
                    AnnotationMapper mapper) {
        super(context, variable, annotation, mapper);
    }

    public static Injector newInstance(
            Class<? extends Injector> injectorClass,
            Context context,
            Object variable,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // TODO: check constructor type?
        return injectorClass.getDeclaredConstructor(Context.class, Object.class, Annotation.class, AnnotationMapper.class)
                .newInstance(context, variable, annotation, mapper);
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

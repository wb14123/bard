package org.binwang.bard.core;

import org.binwang.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Adapter<AnnotationType extends Annotation> extends GenericHandler<AnnotationType> {

    public Adapter(Context context, AnnotationType annotation, AnnotationMapper mapper) {
        super(context, null, Object.class, annotation, mapper);
    }

    public static Adapter newInstance(
            Class<? extends Adapter> adapterClass,
            Class<? extends Annotation> annotationClass,
            Context context,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return adapterClass.getDeclaredConstructor(Context.class, annotationClass, AnnotationMapper.class)
                .newInstance(context, annotation, mapper);
    }

    public boolean match()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            Boolean result = (Boolean) runMethod(method, Match.class);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}

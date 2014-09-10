package org.binwang.bard.core;

import org.binwang.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Adapter<AnnotationType extends Annotation> extends GenericHandler<AnnotationType> {

    public static Adapter newInstance(
            Class<? extends Adapter> adapterClass,
            Context context,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Adapter adapter = adapterClass.newInstance();
        adapter.build(context, null, Object.class, annotation, mapper);
        return adapter;
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

package org.binwang.bard.core;

import org.binwang.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Adapter<AnnotationType extends Annotation> extends GenericHandler<Object, AnnotationType> {
    public Adapter() {
        super();
    }

    public Adapter(Context context,
                   Object variable,
                   AnnotationType annotation,
                   AnnotationMapper mapper) {
        super(context, variable, annotation, mapper);
    }

    public static Adapter newInstance(
            Class<? extends Adapter> adapterClass,
            Context context,
            Annotation annotation,
            AnnotationMapper mapper)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // TODO: check constructor type?
        return adapterClass.getDeclaredConstructor(Context.class, Object.class, Annotation.class, AnnotationMapper.class)
                .newInstance(context, null, annotation, mapper);
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

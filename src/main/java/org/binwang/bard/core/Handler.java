package org.binwang.bard.core;

import org.binwang.bard.core.marker.Handle;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Handler extends GenericHandler<Annotation> {

    public static Handler newInstance(
        Class<? extends Handler> handlerClass,
        Context context,
        AnnotationMapper mapper)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
        InstantiationException {
        Handler handler = handlerClass.newInstance();
        handler.context = context;
        handler.mapper = mapper;
        return handler;
    }

    public Object run()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            Object result = runMethod(method, Handle.class);
            if (result != NoAdapter.NO_ADAPTER) {
                return result;
            }
        }
        return NoAdapter.NO_ADAPTER;
    }
}

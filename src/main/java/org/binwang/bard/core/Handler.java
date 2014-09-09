package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


public class Handler<ReturnType, AnnotationType extends Annotation> {
    public Context context;
    public ReturnType variable;

    protected AnnotationType annotation;
    private AnnotationFilterMap map;

    public Handler(Context context, ReturnType variable, AnnotationType annotation, AnnotationFilterMap map) {
        this.context = context;
        this.variable = variable;
        this.annotation = annotation;
        this.map = map;
    }

    public static Handler newHandlerInstance(Class<? extends Handler> handlerClass, Context context, AnnotationFilterMap map) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return handlerClass
                .getDeclaredConstructor(Context.class, Object.class, Object.class, AnnotationFilterMap.class)
                .newInstance(context, null, null, map);
    }

    public void before() {
    }

    public void run() {
    }

    public void after() {
    }

    private void runMethod(Method m) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        runFuncFilers(m);
        runWithVarFilters(m);
        // TODO: run funcFilter after actions
    }

    private void runFuncFilers(Method m) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Annotation[] annotations = m.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.getClass();
            Class<? extends Handler> handlerClass = map.map.get(annotationClass);
            if (handlerClass == null)
                continue;
            Handler handler = handlerClass
                    .getDeclaredConstructor(Context.class, Object.class, annotationClass, AnnotationFilterMap.class)
                    .newInstance(context, null, annotation, map);
            handler.before();
        }
    }

    private void runWithVarFilters(Method m) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Parameter[] parameters = m.getParameters();
        Object[] args = new Object[parameters.length];
        int i = 0;
        for (; i < parameters.length; i++) {
            args[i] = getParams(parameters[i]);
        }
        m.invoke(args);
    }

    private Object getParams(Parameter parameter) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Annotation[] annotations = parameter.getAnnotations();
        Class<?> parameterType = parameter.getClass();
        Object var = null;
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.getClass();
            Class<? extends Handler> handlerClass = map.map.get(annotationClass);
            if (handlerClass == null)
                continue;
            Handler handler = handlerClass
                    .getDeclaredConstructor(Context.class, parameterType, annotationClass, AnnotationFilterMap.class)
                    .newInstance(context, var, annotation, map);
            handler.before();
            var = handler.variable;
            context = handler.context;
        }
        return var;
    }

}

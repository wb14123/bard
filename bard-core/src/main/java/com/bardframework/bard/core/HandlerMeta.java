package com.bardframework.bard.core;

import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.core.marker.Match;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class HandlerMeta {

    public static AnnotationMapper annotationMapper;
    private static HashMap<
        Class<? extends Servlet>,
        HashMap<Class<? extends GenericHandler>, HandlerMeta>
        > cache = new HashMap<>();
    public List<HandlerMethod> handlerMethods = new LinkedList<>();
    public Map<Class<? extends Annotation>, HandlerMethod> methodsMap = new HashMap<>();

    private static HandlerMeta getFromCache(
        Class<? extends GenericHandler> handlerClass,
        Class<? extends Servlet> servletClass) {
        HashMap<Class<? extends GenericHandler>, HandlerMeta> c2 = cache.get(servletClass);
        if (c2 == null) {
            return null;
        }
        return c2.get(handlerClass);
    }

    private static void putToCache(
        Class<? extends GenericHandler> handlerClass,
        Class<? extends Servlet> servletClass,
        HandlerMeta meta
    ) {

        HashMap<Class<? extends GenericHandler>, HandlerMeta> c2 = cache.get(servletClass);
        if (c2 == null) {
            c2 = new HashMap<>();
        }
        c2.put(handlerClass, meta);
        cache.put(servletClass, c2);
    }

    public static HandlerMeta get(Class<? extends GenericHandler> handlerClass,
        Class<? extends Servlet> servletClass) {
        HandlerMeta meta = getFromCache(handlerClass, servletClass);
        if (meta != null) {
            return meta;
        }

        meta = new HandlerMeta();
        Method[] methods = handlerClass.getDeclaredMethods();

        Field[] fields = handlerClass.getDeclaredFields();
        for (Method method : methods) {
            HandlerMethod handlerMethod =
                meta.newHandlerMethod(method, fields, handlerClass, servletClass);
            meta.handlerMethods.add(handlerMethod);
            for (Annotation annotation : method.getAnnotations()) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                if (annotationClass == Before.class || annotationClass == After.class
                    || annotationClass == Match.class) {
                    meta.methodsMap.put(annotationClass, handlerMethod);
                }
            }
        }

        putToCache(handlerClass, servletClass, meta);
        return meta;
    }

    public static <T extends GenericHandler> Object runAnnotated(
        T handler,
        Class<? extends Servlet> servletClass,
        Class<? extends Annotation> annotationClass
    ) throws IllegalAccessException, InstantiationException {
        HandlerMeta meta = HandlerMeta.get(handler.getClass(), servletClass);
        HandlerMethod method = meta.methodsMap.get(annotationClass);
        if (method == null) {
            return null;
        }
        return method.run(handler.context, handler);
    }

    public static <T extends GenericHandler> Object runHandler(
        T handler, Class<? extends Servlet> servletClass
    ) {
        HandlerMeta meta = HandlerMeta.get(handler.getClass(), servletClass);
        for (HandlerMethod method : meta.handlerMethods) {
            Object result = method.run(handler.context, handler);
            if (result != NoAdapter.NO_ADAPTER) {
                return result;
            }
        }
        return NoAdapter.NO_ADAPTER;
    }

    private static HandlerParameter newHandlerParameter(Parameter parameter,
        Class<? extends Servlet> servletClass) {
        HandlerParameter handlerParameter = new HandlerParameter();
        handlerParameter.parameter = parameter;
        handlerParameter.servletClass = servletClass;
        for (Annotation annotation : parameter.getAnnotations()) {
            Class<? extends Injector> injectorClass =
                annotationMapper.injectorMap.get(annotation.annotationType());
            if (injectorClass != null) {
                handlerParameter.annotatedInjectors
                    .add(new AnnotatedHandler<>(annotation, injectorClass));
                if (getFromCache(injectorClass, servletClass) == null) {
                    HandlerMeta.get(injectorClass, servletClass);
                }
            }
        }
        return handlerParameter;
    }

    private static HandlerField newHandlerField(Field filed,
        Class<? extends Servlet> servletClass) {
        HandlerField handlerField = new HandlerField();
        handlerField.field = filed;
        handlerField.servletClass = servletClass;
        for (Annotation annotation : filed.getAnnotations()) {
            Class<? extends Injector> injectorClass =
                annotationMapper.injectorMap.get(annotation.annotationType());
            if (injectorClass != null) {
                handlerField.annotatedInjectors
                    .add(new AnnotatedHandler<>(annotation, injectorClass));
                if (getFromCache(injectorClass, servletClass) == null) {
                    HandlerMeta.get(injectorClass, servletClass);
                }
            }
        }
        return handlerField;
    }

    private HandlerMethod newHandlerMethod(Method method, Field[] fields,
        Class<? extends GenericHandler> handlerClass,
        Class<? extends Servlet> servletClass
    ) {
        HandlerMethod handlerMethod = new HandlerMethod();
        handlerMethod.method = method;
        handlerMethod.servletClass = servletClass;
        List<Annotation> annotations = new LinkedList<>();
        // only Handler get annotations from handlerClass and servletClass
        if (Handler.class.isAssignableFrom(handlerClass)) {
            annotations.addAll(Arrays.asList(servletClass.getAnnotations()));
            annotations.addAll(Arrays.asList(handlerClass.getAnnotations()));
        }
        annotations.addAll(Arrays.asList(method.getAnnotations()));

        int i = 0;
        for (Annotation annotation : annotations) {
            Class<? extends Filter> filterClass =
                annotationMapper.filterMap.get(annotation.annotationType());
            if (filterClass != null) {
                handlerMethod.annotatedFilters.add(new AnnotatedHandler<>(annotation, filterClass));
                if (getFromCache(filterClass, servletClass) == null) {
                    HandlerMeta.get(filterClass, servletClass);
                }
            }
            Class<? extends Adapter> adapterClass =
                annotationMapper.adapterMap.get(annotation.annotationType());
            if (adapterClass != null) {
                // TODO: ugly hack for adapter chain
                AnnotatedHandler<? extends Adapter> annotatedAdapter =
                    new AnnotatedHandler<>(annotation, adapterClass);
                if (Handler.class.isAssignableFrom(handlerClass)) {
                    if (i < servletClass.getAnnotations().length) {
                        handlerMethod.annotatedServletAdapters.add(annotatedAdapter);
                    }
                    else if (i < servletClass.getAnnotations().length + handlerClass
                        .getAnnotations().length) {
                        handlerMethod.annotatedClassAdapters.add(annotatedAdapter);
                    } else {
                        handlerMethod.annotatedAdapters.add(annotatedAdapter);
                    }
                }
                if (getFromCache(adapterClass, servletClass) == null) {
                    HandlerMeta.get(adapterClass, servletClass);
                }
            }

            i++;
        }
        for (Parameter parameter : method.getParameters()) {
            handlerMethod.parameters.add(newHandlerParameter(parameter, servletClass));
        }
        for (Field filed : fields) {
            handlerMethod.fields.add(newHandlerField(filed, servletClass));
        }
        return handlerMethod;
    }

}

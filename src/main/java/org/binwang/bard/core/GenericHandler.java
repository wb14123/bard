package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;

/*
    GenericHandler. Used by Filter, Injector, Matcher and Handler
 */
public abstract class GenericHandler<AnnotationType extends Annotation> {
    // HTTP context
    protected Context context;
    // used by Injector, after run all the injectors, this variable should be the result
    protected Object variable;
    // type of variable
    protected Class returnType = Object.class;
    // annotation bind to this handler
    protected AnnotationType annotation;
    // how to find handler class from annotation class
    protected AnnotationMapper mapper;

    public GenericHandler() {
    }

    public <HandlerType extends GenericHandler> HandlerType newFromThis(
        Class<? extends HandlerType> handlerClass,
        Class<?> returnType,
        Annotation annotation
    ) throws IllegalAccessException, InstantiationException {
        HandlerType handler = handlerClass.newInstance();
        handler.context = context;
        handler.variable = variable;
        handler.returnType = returnType;
        handler.annotation = annotation;
        handler.mapper = mapper;
        return handler;
    }

    // run methods with specified annotation
    protected void runMethods(Class<? extends Annotation> requiredAnnotation)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            runMethod(m, requiredAnnotation);
        }
    }

    // run method with adapters, filters and injectors; this method should have some specified annotation class on it
    protected Object runMethod(Method m, Class<? extends Annotation> requiredAnnotation)
        throws NoSuchMethodException, IllegalAccessException,
        InstantiationException, InvocationTargetException {
        Boolean findRequiredAnnotation = false;
        Boolean findAdapterOnMethod = false;
        NoAdapter noAdapter = NoAdapter.NO_ADAPTER;

        Annotation[] methodAnnotations = m.getAnnotations();
        Annotation[] classAnnotations = this.getClass().getAnnotations();
        LinkedList<Adapter> adapters = new LinkedList<>();
        Filter[] filters = new Filter[methodAnnotations.length + classAnnotations.length];
        int filterSize = 0;

        for (int i = 0; i < methodAnnotations.length + classAnnotations.length; i++) {
            Annotation annotation;
            if (i < methodAnnotations.length) {
                annotation = methodAnnotations[i];
            } else {
                annotation = classAnnotations[i - methodAnnotations.length];
            }
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (annotationClass == requiredAnnotation) {
                findRequiredAnnotation = true;
            }

            Class<? extends Adapter> adapterClass = mapper.adapterMap.get(annotationClass);
            if (adapterClass != null) {
                Adapter adapter = newFromThis(adapterClass, Object.class, annotation);
                adapters.add(adapter);
                if (i < methodAnnotations.length) {
                    findAdapterOnMethod = true;
                }
            }

            Class<? extends Filter> filterClass = mapper.filterMap.get(annotationClass);
            if (filterClass != null) {
                Filter filter = newFromThis(filterClass, Object.class, annotation);
                filters[filterSize++] = filter;
            }
        }

        // is the method has the specified annotation?
        if (!findRequiredAnnotation && requiredAnnotation != null) {
            return noAdapter;
        }

        // if no adapter specified on handler
        if (this instanceof Handler && !findAdapterOnMethod) {
            return noAdapter;
        }

        // run adapter first, check if all the adapters return true
        for (Adapter adapter : adapters) {
            if (!adapter.match()) {
                return noAdapter;
            }
        }

        Object result = null;
        int i = 0;
        // run filters
        try {
            for (; i < filterSize; i++) {
                filters[i].context = context;
                filters[i].before();
                context = filters[i].context;
            }
            // run the method with injectors
            result = runWithInjectors(m);
            context.result = result;
        } catch (final InvocationTargetException e) {
            context.exception = new Exception(e.getCause());
        } finally {
            for (i = i - 1; i >= 0; i--) {
                filters[i].context = context;
                filters[i].after();
                context = filters[i].context;
            }
        }

        return result;
    }

    // run the method with injectors
    private Object runWithInjectors(Method m)
        throws InvocationTargetException, IllegalAccessException, NoSuchMethodException,
        InstantiationException {
        Parameter[] parameters = m.getParameters();
        Object[] args = new Object[parameters.length];
        int i = 0;
        for (; i < parameters.length; i++) {
            args[i] = getParams(parameters[i]);
        }
        return m.invoke(this, args);
    }

    // get method params from injectors
    private Object getParams(Parameter parameter)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Annotation[] annotations = parameter.getAnnotations();
        Class<?> parameterClass = parameter.getType();
        LinkedList<Injector> injectors = new LinkedList<>();
        Object var = null;
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            Class<? extends Injector> injectorClass = mapper.injectorMap.get(annotationClass);
            if (injectorClass == null) {
                continue;
            }
            Injector injector = newFromThis(injectorClass, parameterClass, annotation);
            injector.variable = var;
            injector.before();
            var = injector.variable;
            context = injector.context;
            injectors.addFirst(injector);
        }
        for (Injector injector : injectors) {
            injector.after();
        }
        return var;
    }

}

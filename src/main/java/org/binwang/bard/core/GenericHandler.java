package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
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
    protected Class returnType;
    // annotation bind to this handler
    protected AnnotationType annotation;
    // how to find handler class from annotation class
    protected AnnotationMapper mapper;

    public void build (
            Context context,
            Object variable,
            Class<?> returnType,
            AnnotationType annotation,
            AnnotationMapper mapper) {
        this.context = context;
        this.variable = variable;
        this.returnType = returnType;
        this.annotation = annotation;
        this.mapper = mapper;
    }

    public GenericHandler() {
    }

    public abstract void handleError(Exception e);

    public abstract void generateDoc();

    // run methods with specified annotation
    protected void runMethods(Class<? extends Annotation> requiredAnnotation)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            runMethod(m, requiredAnnotation);
        }
    }

    // run method with adapters, filters and injectors; this method should have some specified annotation class on it
    protected Object runMethod(Method m, Class<? extends Annotation> requiredAnnotation)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Boolean shouldRun = false;
        NoAdapter noAdapter = NoAdapter.NO_ADAPTER;

        Annotation[] annotations = m.getAnnotations();
        LinkedList<Adapter> adapters = new LinkedList<Adapter>();
        LinkedList<Filter> filters = new LinkedList<Filter>();

        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (annotationClass == requiredAnnotation) {
                shouldRun = true;
            }

            Class<? extends Adapter> adapterClass = mapper.adapterMap.get(annotationClass);
            if (adapterClass != null) {
                Adapter adapter = Adapter.newInstance(
                        adapterClass, context, annotation, mapper);
                adapters.add(adapter);
            }

            Class<? extends Filter> filterClass = mapper.filterMap.get(annotationClass);
            if (filterClass != null) {
                Filter filter = Filter.newInstance(
                        filterClass, context, annotation, mapper);
                filters.add(filter);
            }
        }

        // is the method has the specified annotation?
        if (!shouldRun && requiredAnnotation != null) {
            return noAdapter;
        }

        // run adapter first, check if all the adapters return true
        for (Adapter adapter : adapters) {
            if (!adapter.match()) {
                return noAdapter;
            }
        }

        // run filter's before actions
        for (Filter filter : filters) {
            filter.before();
        }

        // run the method with injectors
        Object result = runWithInjectors(m);

        // TODO: handle error

        // run after actions, reverse order
        // TODO: filter result?
        Iterator<Filter> iterator = filters.descendingIterator();
        if (iterator.hasNext()) {
            Filter filter = iterator.next();
            filter.after();
        }
        return result;
    }

    // run the method with injectors
    private Object runWithInjectors(Method m)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
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
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Annotation[] annotations = parameter.getAnnotations();
        Class<?> parameterClass = parameter.getType();
        LinkedList<Injector> injectors = new LinkedList<Injector>();
        Object var = null;
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            Class<? extends Injector> injectorClass = mapper.injectorMap.get(annotationClass);
            if (injectorClass == null)
                continue;
            Injector injector = Injector.newInstance(
                    injectorClass, context, var, parameterClass, annotation, mapper);
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

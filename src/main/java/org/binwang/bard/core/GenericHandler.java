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

    /**
     * Run GenericHandler method with adapters, filters and injectors.
     * If the class is {@link Handler}, it will only run when there is at least one adapter on it.
     *
     * @param m                  The method to run
     * @param requiredAnnotation Method will run when has requiredAnnotation. If it is null, method will run.
     * @return The method's return. Or NO_ADAPTER if do not match the request.
     * @see org.binwang.bard.core.Filter
     * @see org.binwang.bard.core.Adapter
     * @see org.binwang.bard.core.Injector
     * @see org.binwang.bard.core.Handler
     */
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

        // get method's annotation, say adapters and filters
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

        // if no adapter specified on handler, return it
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
        int filterI = 0;
        int injectorI;
        LinkedList<LinkedList<Injector>> injectors = new LinkedList<>();
        // try-catch, so that if error occurs, recovery to run filters' and injectors' after actions
        try {
            // run filters before
            for (; filterI < filterSize; filterI++) {
                filters[filterI].context = context;
                filters[filterI].before();
                context = filters[filterI].context;
            }

            // run injectors before to get params
            Parameter[] parameters = m.getParameters();
            Object args[] = new Object[parameters.length];

            for (injectorI = 0; injectorI < parameters.length; injectorI++) {
                // run injectors on one param, to get what should it be
                Annotation[] annotations = parameters[injectorI].getAnnotations();
                Class parameterClass = parameters[injectorI].getType();
                LinkedList<Injector> paramInjectors = new LinkedList<>();
                injectors.addFirst(paramInjectors);
                Object var = null;
                for (Annotation annotation : annotations) {
                    Class<? extends Annotation> annotationClass = annotation.annotationType();
                    Class<? extends Injector> injectorClass =
                        mapper.injectorMap.get(annotationClass);
                    if (injectorClass == null) {
                        continue;
                    }
                    Injector injector = newFromThis(injectorClass, parameterClass, annotation);
                    injector.context = context;
                    injector.variable = var;
                    // add injector, in order to run after actions
                    paramInjectors.addFirst(injector);
                    injector.before();
                    var = injector.variable;
                    context = injector.context;
                    if (context.exception != null) {
                        throw context.exception;
                    }
                }
                args[injectorI] = var;
            }

            // run method
            result = m.invoke(this, args);

            // just put the result in context only if the class is Handler (or its extends)
            if (this instanceof Handler) {
                context.result = result;
            }

        } catch (final InvocationTargetException e) {
            // throw by m.invoke, the cause is the true exception
            context.exception = (Exception) e.getCause();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            // these exceptions are caused by framework, so print them
            e.printStackTrace();
        } catch (Exception e) {
            // it is the exception throw by filters or injectors
            context.exception = e;
        } finally {
            // first run injector after actions
            for (LinkedList<Injector> paramsInjectors : injectors) {
                for (Injector injector : paramsInjectors) {
                    injector.context = context;
                    injector.after();
                    context = injector.context;
                }
            }

            // then run filter after actions
            // TODO: this is a bug, which filter to run maybe error
            for (filterI = filterI - 1; filterI >= 0; filterI--) {
                filters[filterI].context = context;
                filters[filterI].after();
                context = filters[filterI].context;
            }
        }

        return result;
    }
}

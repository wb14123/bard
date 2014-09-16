package org.binwang.bard.core;

import org.binwang.bard.core.doc.Api;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

/**
 * GenericHandler is a class that is the foundation of Adapter, Filter, Injector and Handler.
 * It could run its method with defined annotations.
 *
 * @param <AnnotationType> Which annotation is this GenericHandler bind to. No need on Handler.
 * @see org.binwang.bard.core.Adapter
 * @see org.binwang.bard.core.Filter
 * @see org.binwang.bard.core.Injector
 * @see org.binwang.bard.core.Handler
 */
public abstract class GenericHandler<AnnotationType extends Annotation> {
    /**
     * The context in this handler.
     */
    protected Context context;

    // TODO: should variable and returnType more suitable in context?
    /**
     * Only used by {@link org.binwang.bard.core.Injector}, store the current variable that will be injected.
     */
    protected Object variable;

    /**
     * Only used by {@link org.binwang.bard.core.Injector}, the type inject variable.
     */
    protected Class returnType = Object.class;

    /**
     * Used by {@link org.binwang.bard.core.Filter}, {@link org.binwang.bard.core.Injector} and
     * {@link org.binwang.bard.core.Adapter}, the annotation instance that bind to it.
     */
    protected AnnotationType annotation;

    /**
     * A mapper to find GenericHandler from annotation class.
     */
    protected AnnotationMapper mapper;

    /**
     * API document, used except in {@link org.binwang.bard.core.Handler}
     */
    protected Api api = new Api();

    /**
     * APIs, used in {@link org.binwang.bard.core.Handler}
     */
    protected List<Api> apis = new LinkedList<>();

    public GenericHandler() {
    }

    /**
     * Create new instance given the class. Put current context and mapper in it.
     *
     * @param handlerClass  Which class is the new instance?
     * @param returnType    The type of variable.
     * @param annotation    The annotation in it.
     * @param <HandlerType> The class of the new instance, too. Used for compiling safety.
     * @return The new created instance.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
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
        handler.api = api;
        return handler;
    }

    /**
     * Invoke runMethod on methods with specified annotation.
     *
     * @param requiredAnnotation Run methods with the annotation.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected void runMethods(Class<? extends Annotation> requiredAnnotation)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            runMethod(m, requiredAnnotation);
        }
    }

    /**
     * Generate {@link org.binwang.bard.core.doc.Api} from the methods' annotations. Store them in this.api.
     * If the class is {@link org.binwang.bard.core.Handler}, get a list of Api for each method, store them in this.apis.
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected void generateApi() throws InstantiationException, IllegalAccessException {
        // TODO: class annotations
        Method[] methods = this.getClass().getMethods();
        for (Method m : methods) {
            boolean isHandler = false;
            Annotation[] annotations = m.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();

                Class<? extends Filter> filterClass = mapper.filterMap.get(annotationClass);
                if (filterClass != null) {
                    Filter filter = newFromThis(filterClass, Object.class, annotation);
                    filter.generateApi();
                    filter.generateDoc();
                    api = filter.api;
                }

                Class<? extends Adapter> adapterClass = mapper.adapterMap.get(annotationClass);
                if (adapterClass != null) {
                    isHandler = true;
                    Adapter adapter = newFromThis(adapterClass, Object.class, annotation);
                    adapter.generateApi();
                    adapter.generateDoc();
                    api = adapter.api;
                }
            }

            Parameter[] parameters = m.getParameters();
            for (Parameter parameter : parameters) {
                Annotation[] pAnnotations = parameter.getAnnotations();
                for (Annotation annotation : pAnnotations) {
                    Class<? extends Annotation> annotationClass = annotation.annotationType();
                    Class<? extends Injector> injectorClass =
                        mapper.injectorMap.get(annotationClass);
                    if (injectorClass != null) {
                        Injector injector =
                            newFromThis(injectorClass, parameter.getType(), annotation);
                        injector.generateApi();
                        injector.generateDoc();
                        api = injector.api;
                    }
                }
            }

            if (isHandler && this instanceof Handler) {
                apis.add(api);
                api = new Api();
            }
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

        // get method's annotation
        Annotation[] methodAnnotations = m.getAnnotations();
        // get class's annotation
        Annotation[] classAnnotations = this.getClass().getAnnotations();
        LinkedList<Adapter> adapters = new LinkedList<>();
        Filter[] filters = new Filter[methodAnnotations.length + classAnnotations.length];
        int filterSize = 0;

        // check the annotations, to get adapters and filters
        for (int i = 0; i < methodAnnotations.length + classAnnotations.length; i++) {
            Annotation annotation;
            if (i < classAnnotations.length) {
                annotation = classAnnotations[i];
            } else {
                annotation = methodAnnotations[i - classAnnotations.length];
            }
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (annotationClass == requiredAnnotation) {
                findRequiredAnnotation = true;
            }

            Class<? extends Adapter> adapterClass = mapper.adapterMap.get(annotationClass);
            if (adapterClass != null) {
                Adapter adapter = newFromThis(adapterClass, Object.class, annotation);
                adapters.add(adapter);
                if (i >= classAnnotations.length) {
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
        boolean matching = true;
        LinkedList<Adapter> runAdapters = new LinkedList<>();
        for (Adapter adapter : adapters) {
            adapter.context = context;
            runAdapters.addFirst(adapter);
            matching = adapter.match();
            context = adapter.context;
            if (!matching) {
                break;
            }
        }

        for (Adapter adapter : runAdapters) {
            adapter.after();
            context = adapter.context;
        }

        if (!matching) {
            return NoAdapter.NO_ADAPTER;
        }


        Object result = null;
        int filterI = 0;
        int injectorI;
        LinkedList<LinkedList<Injector>> injectors = new LinkedList<>();
        LinkedList<Filter> runFilters = new LinkedList<>();
        // try-catch, so that if error occurs, recovery to run filters' and injectors' after actions
        try {
            // run filters before
            for (; filterI < filterSize; filterI++) {
                filters[filterI].context = context;
                runFilters.addFirst(filters[filterI]);
                filters[filterI].before();
                context = filters[filterI].context;
                if (context.exception != null) {
                    throw context.exception;
                }
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
            for (Filter filter : runFilters) {
                filter.context = context;
                filter.after();
                context = filter.context;
            }
        }

        return result;
    }
}

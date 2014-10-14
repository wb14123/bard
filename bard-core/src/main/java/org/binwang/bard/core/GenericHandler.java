package org.binwang.bard.core;

import org.binwang.bard.core.doc.Api;
import org.binwang.bard.core.doc.DocParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    // TODO: should injectorVariable and injectorVariableType more suitable in context?
    /**
     * Only used by {@link org.binwang.bard.core.Injector}, store the current injectorVariable that will be injected.
     */
    protected Object injectorVariable;

    /**
     * Only used by {@link org.binwang.bard.core.Injector}, the type of inject injectorVariable.
     */
    protected Class<?> injectorVariableType = Object.class;

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
     * API document, used in {@link org.binwang.bard.core.Filter} and {@link org.binwang.bard.core.Adapter}
     */
    protected Api api = new Api();

    /**
     * APIs, used in {@link org.binwang.bard.core.Handler}
     */
    protected List<Api> apis = new LinkedList<>();

    /**
     * Parameter in API, used in {@link org.binwang.bard.core.Injector}
     */
    protected DocParameter docParameter;

    /**
     * Handler return type, used for generate API. Used in {@link org.binwang.bard.core.Filter}
     */
    protected Class<?> returnType;

    public GenericHandler() {
    }

    /**
     * Create new instance given the class. Put current context and mapper in it.
     *
     * @param handlerClass         Which class is the new instance?
     * @param injectorVariableType The type of injectorVariable.
     * @param annotation           The annotation in it.
     * @param <HandlerType>        The class of the new instance, too. Used for compiling safety.
     * @return The new created instance.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public <HandlerType extends GenericHandler> HandlerType newFromThis(
        Class<? extends HandlerType> handlerClass,
        Class<?> injectorVariableType,
        Annotation annotation
    ) throws IllegalAccessException, InstantiationException {
        HandlerType handler = handlerClass.newInstance();
        handler.context = context;
        handler.injectorVariable = injectorVariable;
        handler.injectorVariableType = injectorVariableType;
        handler.annotation = annotation;
        handler.mapper = mapper;
        handler.api = api;
        handler.docParameter = docParameter;
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
        Method[] methods = this.getClass().getDeclaredMethods();
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
        Method[] methods = this.getClass().getDeclaredMethods();
        Annotation[] classAnnotations = this.getClass().getAnnotations();
        for (Method m : methods) {
            boolean isHandler = false;
            Annotation[] methodAnnotations = m.getAnnotations();
            for (int i = 0; i < methodAnnotations.length + classAnnotations.length; i++) {
                Annotation annotation;
                if (i < classAnnotations.length) {
                    annotation = classAnnotations[i];
                } else {
                    annotation = methodAnnotations[i - classAnnotations.length];
                }

                Class<? extends Annotation> annotationClass = annotation.annotationType();
                Class<? extends Filter> filterClass = mapper.filterMap.get(annotationClass);
                if (filterClass != null) {
                    Filter<?> filter = newFromThis(filterClass, Object.class, annotation);
                    filter.returnType = m.getReturnType();
                    filter.generateApi();
                    filter.generateDoc();
                    api = filter.api;
                }

                Class<? extends Adapter> adapterClass = mapper.adapterMap.get(annotationClass);
                if (adapterClass != null) {
                    if (i >= classAnnotations.length) {
                        isHandler = true;
                    }
                    Adapter adapter = newFromThis(adapterClass, Object.class, annotation);
                    adapter.generateApi();
                    adapter.generateDoc();
                    api = adapter.api;
                }
            }

            Parameter[] parameters = m.getParameters();
            for (Parameter parameter : parameters) {
                docParameter = new DocParameter();
                boolean hasInjector = false;
                Annotation[] pAnnotations = parameter.getAnnotations();
                for (Annotation annotation : pAnnotations) {
                    Class<? extends Annotation> annotationClass = annotation.annotationType();
                    Class<? extends Injector> injectorClass =
                        mapper.injectorMap.get(annotationClass);
                    if (injectorClass != null) {
                        hasInjector = true;
                        Injector injector =
                            newFromThis(injectorClass, parameter.getType(), annotation);
                        injector.generateApi();
                        injector.generateDoc();
                        docParameter = injector.docParameter;
                    }
                }
                if (hasInjector) {
                    api.parameters.add(docParameter);
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
        Map<Class<? extends Annotation>, List<Adapter>> adapterMap = new LinkedHashMap<>();
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
                /*
                 Add the adapters with the same annotation type to one list, in order to processing them together.
                 Helpful while define adapters could be used both on class and method.
                */
                List<Adapter> adapters = adapterMap.get(annotationClass);
                if (adapters == null) {
                    adapters = new LinkedList<>();
                }
                adapters.add(adapter);
                adapterMap.put(annotationClass, adapters);
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

        /*
         Run adapter first, check if all the adapters return true.
         If some adapters has the same annotation type (this only happens both class and method has the same annotation),
         run all the chain, if one get true then it is successful.
         It is very helpful to define adapter annotations that could be used both on class and method: such as Path.
        */
        for (Map.Entry<Class<? extends Annotation>, List<Adapter>> entry : adapterMap.entrySet()) {
            boolean matching = false;

            List<Adapter> adapters = entry.getValue();
            LinkedList<Adapter> runAdapters = new LinkedList<>();

            for (Adapter adapter : adapters) {
                adapter.context = context;
                runAdapters.addFirst(adapter);
                matching = adapter.match() || matching;
                context = adapter.context;
            }

            for (Adapter adapter : runAdapters) {
                adapter.after();
                context = adapter.context;
            }

            if (!matching) {
                return NoAdapter.NO_ADAPTER;
            }
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

            // run injectors to init class fields
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.getAnnotations();
                Annotation[] annotations = field.getAnnotations();
                Class<?> fieldClass = field.getType();
                Object var = runInjectors(annotations, fieldClass, injectors);
                if (var != null) {
                    field.set(this, var);
                }
            }

            // run injectors before to get params
            Parameter[] parameters = m.getParameters();
            Object args[] = new Object[parameters.length];

            for (injectorI = 0; injectorI < parameters.length; injectorI++) {
                // run injectors on one param, to get what should it be
                Annotation[] annotations = parameters[injectorI].getAnnotations();
                Class<?> parameterClass = parameters[injectorI].getType();
                Object var = runInjectors(annotations, parameterClass, injectors);
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

    private Object runInjectors(Annotation[] annotations, Class<?> parameterClass,
        LinkedList<LinkedList<Injector>> injectors)
        throws Exception {
        Object var = null;
        LinkedList<Injector> paramInjectors = new LinkedList<>();
        injectors.addFirst(paramInjectors);
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            Class<? extends Injector> injectorClass =
                mapper.injectorMap.get(annotationClass);
            if (injectorClass == null) {
                continue;
            }
            Injector injector = newFromThis(injectorClass, parameterClass, annotation);
            injector.context = context;
            injector.injectorVariable = var;
            // add injector, in order to run after actions
            paramInjectors.addFirst(injector);
            injector.before();
            var = injector.injectorVariable;
            context = injector.context;
            if (context.exception != null) {
                throw context.exception;
            }
        }
        return var;
    }
}

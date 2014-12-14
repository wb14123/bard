package com.bardframework.bard.core;

import org.reflections.Reflections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Servlet extends HttpServlet {
    public static final long serialVersionUID = 1L;

    /**
     * A global map to get AnnotationMapper from servlet class.
     * <p/>
     * So that the reflect result could be cached to improve the performance.
     */
    private static Map<Class<? extends Servlet>, AnnotationMapper> mapperCache = new HashMap<>();

    /**
     * All the models found in packages. Used to generate API docuemtn.
     */

    private AnnotationMapper mapper;

    public Servlet() {
        Class<? extends Servlet> c = this.getClass();
        mapper = mapperCache.get(c);
        if (mapper != null) {
            return;
        }

        mapper = new AnnotationMapper();

        for (String pkg : getPackageNames()) {
            Reflections reflections = new Reflections(pkg);

            Set<Class<? extends Filter>> filters = reflections.getSubTypesOf(Filter.class);
            for (Class<? extends Filter> filterClass : filters) {
                BindTo bindTo = filterClass.getAnnotation(BindTo.class);
                if (bindTo == null) {
                    continue;
                }
                addFilter(bindTo.value(), filterClass);
            }

            Set<Class<? extends Adapter>> adapters = reflections.getSubTypesOf(Adapter.class);
            for (Class<? extends Adapter> adapterClass : adapters) {
                BindTo bindTo = adapterClass.getAnnotation(BindTo.class);
                if (bindTo == null) {
                    continue;
                }
                addAdapter(bindTo.value(), adapterClass);
            }

            Set<Class<? extends Injector>> injectors =
                reflections.getSubTypesOf(Injector.class);
            for (Class<? extends Injector> injectorClass : injectors) {
                BindTo bindTo = injectorClass.getAnnotation(BindTo.class);
                if (bindTo == null) {
                    continue;
                }
                addInjector(bindTo.value(), injectorClass);
            }

            Set<Class<? extends Handler>> handlers = reflections.getSubTypesOf(Handler.class);
            handlers.forEach(this::addHandler);
        }
        mapperCache.put(c, mapper);
    }

    /**
     * Specify where are the handlers, middleware and models.
     *
     * @return A list of package name that contains the handlers, middleware and models.
     */
    public abstract String[] getPackageNames();

    /*
    These methods that modified the filters, adapters, injectors and handlers, will not
    only modify this object, but will modify the all the objects that of this class.

    You may not want do it in production. I do it for the test, maybe I can find a better way.
     */

    public void addFilter(final Class<? extends Annotation> annotationClass,
        final Class<? extends Filter> filterClass) {
        mapper.filterMap.put(annotationClass, filterClass);
    }

    public void addAdapter(final Class<? extends Annotation> annotationClass,
        final Class<? extends Adapter> adapterClass) {
        mapper.adapterMap.put(annotationClass, adapterClass);
    }

    public void addInjector(final Class<? extends Annotation> annotationClass,
        final Class<? extends Injector> injectorClass) {
        mapper.injectorMap.put(annotationClass, injectorClass);
    }

    public void addHandler(final Class<? extends Handler> handlerClass) {
        mapper.handlers.add(handlerClass);
    }

    public void removeHandler(final Class<? extends Handler> handlerClass) {
        mapper.handlers.remove(handlerClass);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            HandlerMeta.annotationMapper = mapper;

            // TODO: performance for adapter? (instead of linear time)
            for (Class<? extends Handler> handlerClass : mapper.handlers) {
                Context context = new Context();
                context.request = request;
                context.response = response;
                Handler handler = handlerClass.newInstance();
                handler.context = context;
                Object result = HandlerMeta.runHandler(handler, this.getClass());

                /*
                Handler handler = Handler.newInstance(handlerClass, context, mapper);
                handler.setServlet(this);
                */
                if (result != NoAdapter.NO_ADAPTER) {
                    return;
                }
            }
            response.setStatus(404);
            response.getWriter().println("page not found");
        } catch (
            IllegalAccessException |
                InstantiationException |
                IOException e) {
            Util.getLogger().error("Error found in servlet: {}", e);
        }
    }
}


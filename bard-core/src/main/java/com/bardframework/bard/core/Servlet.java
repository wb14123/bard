package com.bardframework.bard.core;

import com.bardframework.bard.core.doc.Api;
import com.bardframework.bard.core.doc.Document;
import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import org.reflections.Reflections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    private Map<String, JsonSchema> models = new HashMap<>();

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
                addFilter(bindTo.value(), filterClass);
            }

            Set<Class<? extends Adapter>> adapters = reflections.getSubTypesOf(Adapter.class);
            for (Class<? extends Adapter> adapterClass : adapters) {
                BindTo bindTo = adapterClass.getAnnotation(BindTo.class);
                addAdapter(bindTo.value(), adapterClass);
            }

            Set<Class<? extends Injector>> injectors =
                reflections.getSubTypesOf(Injector.class);
            for (Class<? extends Injector> injectorClass : injectors) {
                BindTo bindTo = injectorClass.getAnnotation(BindTo.class);
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
    protected abstract String[] getPackageNames();

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

    /**
     * Get document of the whole web application.
     *
     * @return The document.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws JsonMappingException
     */
    public Document getDocument()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException, JsonMappingException {
        // get models
        for (String pkg : getPackageNames()) {
            Reflections reflections = new Reflections(pkg);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Model.class);
            for (Class<?> c : classes) {
                JsonSchema schema = Document.toJsonSchema(c);
                models.put(schema.getId(), schema);
            }
        }
        List<Api> apis = new LinkedList<>();
        for (Class<? extends Handler> handlerClass : mapper.handlers) {
            Handler handler = Handler.newInstance(handlerClass, null, mapper);
            handler.generateApi();
            apis.addAll(handler.apis);
        }
        Document document = new Document();
        document.apis = apis;
        document.models = models;
        return document;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if (path != null && path.equals("/api-doc")) {
            try {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.getWriter().write(getDocument().toJson());
                response.getWriter().close();
                return;
            } catch (IOException |
                InvocationTargetException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException e) {
                Util.getLogger().error("Error found in /api-doc: {}", e);
            }
        }

        try {
            // TODO: performance for adapter? (instead of linear time)
            for (Class<? extends Handler> handlerClass : mapper.handlers) {
                Context context = new Context(request, response);
                Handler handler = Handler.newInstance(handlerClass, context, mapper);
                handler.servletAnnotations = this.getClass().getAnnotations();
                Object result = handler.run();
                if (result != NoAdapter.NO_ADAPTER) {
                    return;
                }
            }
            response.setStatus(404);
            response.getWriter().println("page not found");
        } catch (NoSuchMethodException |
            IllegalAccessException |
            InvocationTargetException |
            InstantiationException |
            IOException e) {
            Util.getLogger().error("Error found in servlet: {}", e);
        }
    }
}


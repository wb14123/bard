package org.binwang.bard.core;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import org.binwang.bard.core.doc.Api;
import org.binwang.bard.core.doc.Document;
import org.binwang.bard.core.marker.Model;
import org.reflections.Reflections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Servlet extends HttpServlet {
    public Map<String, JsonSchema> models = new HashMap<>();
    private AnnotationMapper mapper = new AnnotationMapper();
    private List<Class<? extends Handler>> handlers = new LinkedList<>();

    public Servlet(String... pkgs)
        throws NoSuchFieldException, IllegalAccessException, InstantiationException,
        NoSuchMethodException, JsonMappingException {
        for (String pkg : pkgs) {
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

            Set<Class<? extends Injector>> injectors = reflections.getSubTypesOf(Injector.class);
            for (Class<? extends Injector> injectorClass : injectors) {
                BindTo bindTo = injectorClass.getAnnotation(BindTo.class);
                addInjector(bindTo.value(), injectorClass);
            }

            Set<Class<? extends Handler>> handlers = reflections.getSubTypesOf(Handler.class);
            handlers.forEach(this::addHandler);

            // get models
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Model.class);
            for (Class<?> c : classes) {
                JsonSchema schema = Document.toJsonSchema(c);
                models.put(schema.getId(), schema);
            }
        }
    }

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
        handlers.add(handlerClass);
    }

    public Document getDocument()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
        List<Api> apis = new LinkedList<>();
        for (Class<? extends Handler> handlerClass : handlers) {
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
                e.printStackTrace();
            }
        }

        try {
            // TODO: performance for adapter? (instead of linear time)
            for (Class<? extends Handler> handlerClass : handlers) {
                Context context = new Context(request, response);
                Handler handler = Handler.newInstance(handlerClass, context, mapper);
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
            e.printStackTrace();
        }
    }
}


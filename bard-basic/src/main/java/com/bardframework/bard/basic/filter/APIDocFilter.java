package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.marker.APIDoc;
import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.core.*;
import com.bardframework.bard.core.doc.Api;
import com.bardframework.bard.core.doc.DocParameter;
import com.bardframework.bard.core.doc.Document;
import com.bardframework.bard.core.doc.Response;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@BindTo(APIDoc.class)
public class APIDocFilter extends Filter<APIDoc> {
    private static Document document;

    @Doc("Return API document")
    @After public void generateAPI()
        throws JsonMappingException, InvocationTargetException, NoSuchMethodException,
        InstantiationException, IllegalAccessException {
        if (document == null) {
            Map<String, JsonSchema> models = new HashMap<>();
            for (String pkg : annotation.servletClass().newInstance().getPackageNames()) {
                Reflections reflections = new Reflections(pkg);
                Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Model.class);
                for (Class<?> c : classes) {
                    JsonSchema schema = Document.toJsonSchema(c);
                    models.put(schema.getId(), schema);
                }
            }
            document = new Document();
            document.models = models;
            document.name = annotation.value();
            for (Class<? extends Handler> handlerClass : HandlerMeta.annotationMapper.handlers) {
                getOneApi(document, null, null, handlerClass, annotation.servletClass());
            }
        }
        context.setResult(document);
        context.returnType = Document.class;
    }

    private void getOneApi(
        Document document,
        Api api,
        Context context,
        Class<? extends GenericHandler> handlerClass,
        Class<? extends Servlet> servletClass
    ) throws IllegalAccessException, InstantiationException {
        HandlerMeta meta = HandlerMeta.get(handlerClass, servletClass);
        for (HandlerMethod method : meta.handlerMethods) {
            if (Handler.class.isAssignableFrom(handlerClass)) {
                api = new Api();
                context = new Context();
                context.returnType = method.method.getReturnType();
            }
            for (AnnotatedHandler<? extends Adapter> annotatedAdapter : method.annotatedAdapters) {
                getOneApi(document, api, context, annotatedAdapter.handlerClass, servletClass);
                Adapter adapter = annotatedAdapter.handlerClass.newInstance();
                adapter.annotation = annotatedAdapter.annotation;
                adapter.api = api;
                adapter.generateDoc();
            }
            for (AnnotatedHandler<? extends Filter> annotatedFilter : method.annotatedFilters) {
                getOneApi(document, api, context, annotatedFilter.handlerClass, servletClass);
                Filter filter = annotatedFilter.handlerClass.newInstance();
                filter.annotation = annotatedFilter.annotation;
                filter.api = api;
                filter.generateDoc();
            }
            for (HandlerField field : method.fields) {
                DocParameter docParameter = new DocParameter();
                context.setInjectorVariable(field.field.getType());
                for (AnnotatedHandler<? extends Injector> annotatedInjector: field.annotatedInjectors) {
                    getOneApi(document, api, context, annotatedInjector.handlerClass, servletClass);
                    Injector injector = annotatedInjector.handlerClass.newInstance();
                    injector.annotation = annotatedInjector.annotation;
                    injector.docParameter = docParameter;
                    injector.generateDoc();
                }
                if (!docParameter.isNull()) {
                    api.parameters.add(docParameter);
                }
            }
            for (HandlerParameter parameter: method.parameters) {
                DocParameter docParameter = new DocParameter();
                context.setInjectorVariable(parameter.parameter.getType());
                for (AnnotatedHandler<? extends Injector> annotatedInjector: parameter.annotatedInjectors) {
                    getOneApi(document, api, context, annotatedInjector.handlerClass, servletClass);
                    Injector injector = annotatedInjector.handlerClass.newInstance();
                    injector.annotation = annotatedInjector.annotation;
                    injector.docParameter = docParameter;
                    injector.generateDoc();
                }
                if (!docParameter.isNull()) {
                    api.parameters.add(docParameter);
                }
            }
            if (Handler.class.isAssignableFrom(handlerClass)) {
                document.apis.add(api);
            }
        }
    }

    @Override public void generateDoc() {
        for (Response response : api.responses) {
            if (response.code == 200 && response.returnType == void.class) {
                response.returnType = Document.class;
            }
        }
    }
}

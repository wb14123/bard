package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.marker.APIDoc;
import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Context;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.core.doc.Api;
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
            for (String pkg : getServlet().getPackageNames()) {
                Reflections reflections = new Reflections(pkg);
                Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Model.class);
                for (Class<?> c : classes) {
                    JsonSchema schema = Document.toJsonSchema(c);
                    models.put(schema.getId(), schema);
                }
            }
            List<Api> apis = new LinkedList<>();
            for (Class<? extends Handler> handlerClass : mapper.handlers) {
                Handler handler = Handler.newInstance(handlerClass, new Context(), mapper);
                handler.setServlet(getServlet());
                apis.addAll(handler.generateApi());
            }
            document = new Document();
            document.apis = apis;
            document.models = models;
            document.name = annotation.value();
        }
        context.setResult(document);
        context.returnType = Document.class;
    }

    @Override public void generateDoc() {
        for (Response response : api.responses) {
            if (response.code == 200 && response.returnType == void.class) {
                response.returnType = Document.class;
            }
        }
    }
}

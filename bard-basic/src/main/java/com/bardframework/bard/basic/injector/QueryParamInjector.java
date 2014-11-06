package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;

import javax.ws.rs.QueryParam;

@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        context.custom.put("param", annotation.value());
        String param = context.request.getParameter(annotation.value());
        if (param == null) {
            injectorVariable = null;
            return;
        }
        TypeParser parser = TypeParser.newBuilder().build();
        injectorVariable = parser.parse(param, injectorVariableType);
    }

    @Override
    public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "url";
    }
}

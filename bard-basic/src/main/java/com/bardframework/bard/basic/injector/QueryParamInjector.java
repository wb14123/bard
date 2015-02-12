package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.marker.HandleError;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;
import com.github.drapostolos.typeparser.TypeParserException;

import javax.ws.rs.QueryParam;

@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    @HandleErrors({
        @HandleError(code = 400, exception = TypeParserException.class, description = "params error")
    })
    public void getParams() {
        context.putCustom("param", annotation.value());
        String param = context.getRequest().getParameter(annotation.value());
        if (param == null || param.equals("")) {
            return;
        }
        TypeParser parser = TypeParser.newBuilder().build();
        context.setInjectorVariable(parser.parse(param, context.getInjectorVariableType()));
    }

    @Override
    public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "url";
    }
}

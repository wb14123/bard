package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;

import javax.ws.rs.HeaderParam;

@BindTo(HeaderParam.class)
public class HeaderParamInjector extends Injector<HeaderParam> {
    @Before
    public void get() {
        context.putCustom("param", annotation.value());
        String param = context.getRequest().getHeader(annotation.value());
        if (param == null || param.equals("")) {
            return;
        }
        TypeParser parser = TypeParser.newBuilder().build();
        context.setInjectorVariable(parser.parse(param, context.getInjectorVariableType()));
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "header";
    }
}

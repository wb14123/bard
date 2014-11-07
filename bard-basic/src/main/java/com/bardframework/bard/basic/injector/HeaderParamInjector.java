package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

import javax.ws.rs.HeaderParam;

@BindTo(HeaderParam.class)
public class HeaderParamInjector extends Injector<HeaderParam> {
    @Before public void get() {
        context.putCustom("param", annotation.value());
        context.setInjectorVariable(context.getRequest().getHeader(annotation.value()));
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "header";
    }
}

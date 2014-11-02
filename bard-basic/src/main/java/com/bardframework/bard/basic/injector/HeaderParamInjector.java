package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

import javax.ws.rs.HeaderParam;

@BindTo(HeaderParam.class)
public class HeaderParamInjector extends Injector<HeaderParam> {
    @Before public void get() {
        injectContext.put("param", annotation.value());
        injectorVariable = context.request.getHeader(annotation.value());
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "header";
    }
}

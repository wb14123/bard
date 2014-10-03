package org.binwang.bard.basic.injector;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

import javax.ws.rs.HeaderParam;

@BindTo(HeaderParam.class)
public class HeaderParamInjector extends Injector<HeaderParam> {
    @Before public void get() {
        injectorVariable = context.request.getHeader(annotation.value());
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "header";
    }
}

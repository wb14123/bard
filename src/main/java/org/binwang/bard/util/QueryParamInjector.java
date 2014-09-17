package org.binwang.bard.util;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        String param = context.request.getParameter(annotation.value());
        if (param == null || param.equals("")) {
            injectorVariable = null;
            return;
        }
        try {
            if (injectorVariableType == Integer.class) {
                this.injectorVariable = Integer.parseInt(param);
            } else if (injectorVariableType == Double.class) {
                this.injectorVariable = Double.parseDouble(param);
            }
        } catch (NumberFormatException e) {
            this.injectorVariable = null;
        }
    }

    @Override
    public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "url-query";
    }
}

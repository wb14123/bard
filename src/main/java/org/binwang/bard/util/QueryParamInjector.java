package org.binwang.bard.util;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.doc.Parameter;
import org.binwang.bard.core.marker.Before;

@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        String param = context.request.getParameter(annotation.value());
        if (param == null || param.equals("")) {
            variable = null;
            return;
        }
        try {
            if (returnType == Integer.class) {
                this.variable = Integer.parseInt(param);
            } else if (returnType == Double.class) {
                this.variable = Double.parseDouble(param);
            }
        } catch (NumberFormatException e) {
            this.variable = null;
        }
    }

    @Override
    public void generateDoc() {
        Parameter parameter = new Parameter();
        parameter.name = annotation.value();
        parameter.type = returnType;
        api.parameters.add(parameter);
    }
}

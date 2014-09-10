package org.binwang.bard.util;

import org.binwang.bard.core.AnnotationMapper;
import org.binwang.bard.core.Context;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        String param = context.request.getParameter(annotation.value());
        if (returnType == Integer.class) {
            this.variable = Integer.parseInt(param);
        } else if (returnType == Double.class) {
            this.variable = Double.parseDouble(param);
        }
    }

    @Override
    public void handleError(Exception e) {
    }

    @Override
    public void generateDoc() {

    }
}

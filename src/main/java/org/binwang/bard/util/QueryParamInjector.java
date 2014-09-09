package org.binwang.bard.util;

import org.binwang.bard.core.AnnotationMapper;
import org.binwang.bard.core.Context;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

public class QueryParamInjector<ReturnType> extends Injector<ReturnType, QueryParam> {

    public QueryParamInjector(Context context, ReturnType variable, QueryParam annotation, AnnotationMapper mapper) {
        super(context, variable, annotation, mapper);
    }

    @Before
    public Object getParams() {
        String param = context.request.getParameter(annotation.value());
        if (variable instanceof Integer) {
            variable = (ReturnType) Integer.getInteger(param);
        } else if (variable instanceof Double) {
            variable = (ReturnType) Double.valueOf(param);
        }
        return null;
    }

    @Override
    public void handleError(Exception e) {

    }

    @Override
    public void generateDoc() {

    }
}

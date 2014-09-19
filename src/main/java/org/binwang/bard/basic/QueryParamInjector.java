package org.binwang.bard.basic;

import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.basic.marker.QueryParam;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

@BindTo(QueryParam.class)
public class QueryParamInjector extends Injector<QueryParam> {

    @Before
    public void getParams() {
        String param = context.request.getParameter(annotation.value());
        TypeParser parser = TypeParser.newBuilder().build();
        injectorVariable = parser.parse(param, injectorVariableType);
    }

    @Override
    public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "url-query";
    }
}

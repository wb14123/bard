package com.bardframework.bard.basic.injector;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;

import javax.ws.rs.PathParam;
import java.util.Map;

@BindTo(PathParam.class)
public class PathParamInjector extends Injector<PathParam> {
    @Before public void getParam() {
        injectContext.put("param", annotation.value());
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) context.custom.get("path-params");
        if (map == null) {
            injectorVariable = null;
            return;
        }
        String param = map.get(annotation.value());
        TypeParser parser = TypeParser.newBuilder().build();
        injectorVariable = parser.parse(param, injectorVariableType);
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "path";
    }
}

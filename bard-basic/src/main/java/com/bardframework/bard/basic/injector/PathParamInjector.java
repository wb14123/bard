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
        context.putCustom("param", annotation.value());
        Map<String, String> map = context.getCustom("path-params");
        if (map == null) {
            context.setInjectorVariable(null);
            return;
        }
        String param = map.get(annotation.value());
        TypeParser parser = TypeParser.newBuilder().build();
        context.setInjectorVariable(parser.parse(param, context.getInjectorVariableType()));
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "path";
    }
}

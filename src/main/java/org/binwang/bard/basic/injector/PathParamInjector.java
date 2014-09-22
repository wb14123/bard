package org.binwang.bard.basic.injector;

import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

import javax.ws.rs.PathParam;
import java.util.Map;

@BindTo(PathParam.class)
public class PathParamInjector extends Injector<PathParam> {
    @Before public void getParam() {
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

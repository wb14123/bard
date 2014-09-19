package org.binwang.bard.basic;

import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.basic.marker.PathParam;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

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
    }
}

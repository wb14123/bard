package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Match;
import org.glassfish.jersey.uri.PathTemplate;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@BindTo(Path.class)
public class PathAdapter extends Adapter<Path> {
    private String oldPath;

    private String removeLastSlash(String str) {
        if (str.length() > 0 && str.charAt(str.length() - 1) == '/') {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Match
    public boolean match() {
        oldPath = (String) context.custom.get("path");
        if (oldPath == null) {
            oldPath = "";
        }
        String currentPath = oldPath + annotation.value();
        context.custom.put("path", currentPath);
        PathTemplate pathTemplate = new PathTemplate(removeLastSlash(currentPath));
        Map<String, String> values = new HashMap<>();
        String realPath = removeLastSlash(context.request.getPathInfo());
        if (pathTemplate.match(realPath, values)) {
            context.custom.put("path-params", values);
            return true;
        }
        return false;
    }

    @After
    public void cleanUp() {
        context.custom.put("path", oldPath);
    }

    @Override
    public void generateDoc() {
        if (api.path == null) {
            api.path = "";
        }
        api.path += removeLastSlash(annotation.value());
    }
}

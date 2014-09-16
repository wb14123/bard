package org.binwang.bard.util;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Match;

@BindTo(Path.class)
public class PathAdapter extends Adapter<Path> {
    private String oldPath;

    @Match
    public boolean match() {
        oldPath = (String) context.custom.get("path");
        if (oldPath == null) {
            oldPath = "";
        }
        String currentPath = oldPath + annotation.value();
        context.custom.put("path", currentPath);
        String realPath = context.request.getPathInfo();
        return currentPath.equals(realPath);
    }

    @After
    public void cleanUp() {
        context.custom.put("path", oldPath);
    }

    @Override
    public void generateDoc() {

    }
}

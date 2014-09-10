package org.binwang.bard.util;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.marker.Match;

public class PathAdapter extends Adapter<Path> {

    @Match
    public boolean match() {
        String queryPath = annotation.value();
        String realPath = context.request.getPathInfo();
        return queryPath.equals(realPath);
    }

    @Override
    public void handleError(Exception e) {
    }

    @Override
    public void generateDoc() {

    }
}

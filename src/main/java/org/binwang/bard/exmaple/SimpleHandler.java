package org.binwang.bard.exmaple;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.marker.Handle;
import org.binwang.bard.util.QueryParam;

public class SimpleHandler extends Handler {

    @Handle
    public Integer printParams(@QueryParam("id") Integer id) {
        return id;
    }

    @Override
    public void handleError(Exception e) {

    }

    @Override
    public void generateDoc() {

    }
}

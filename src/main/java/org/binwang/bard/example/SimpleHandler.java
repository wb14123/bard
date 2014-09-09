package org.binwang.bard.example;

import org.binwang.bard.core.AnnotationMapper;
import org.binwang.bard.core.Context;
import org.binwang.bard.core.Handler;
import org.binwang.bard.core.marker.Handle;
import org.binwang.bard.util.QueryParam;

import java.io.IOException;

public class SimpleHandler extends Handler {

    public SimpleHandler(Context context, AnnotationMapper mapper) {
        super(context, mapper);
    }

    @Handle
    public Integer printParams(@QueryParam("id") Integer id) throws IOException {
        context.response.getWriter().print("hello, world!");
        return id;
    }

    @Override
    public void handleError(Exception e) {
    }

    @Override
    public void generateDoc() {

    }
}

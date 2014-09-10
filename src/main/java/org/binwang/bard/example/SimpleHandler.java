package org.binwang.bard.example;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.marker.Handle;
import org.binwang.bard.util.Path;
import org.binwang.bard.util.QueryParam;

import java.io.IOException;

public class SimpleHandler extends Handler {

    @Handle
    @Path("/get")
    public Integer printParams(@QueryParam("id") Integer id) throws IOException {
        context.response.getWriter().print("hello, " + id.toString());
        return id;
    }

    @Handle
    @Path("/add")
    public Integer add(@QueryParam("a") Integer a,
                       @QueryParam("b") Integer b) throws IOException {
        Integer c = a + b;
        context.response.getWriter().print(c.toString());
        return c;
    }

    @Override
    public void handleError(Exception e) {
    }

    @Override
    public void generateDoc() {

    }
}

package org.binwang.bard.example;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.marker.Handle;
import org.binwang.bard.util.Path;
import org.binwang.bard.util.PlainText;
import org.binwang.bard.util.QueryParam;

import java.io.IOException;

public class SimpleHandler extends Handler {

    @Handle
    @Path("/get")
    @PlainText
    public Integer printParams(@QueryParam("id") Integer id) throws IOException {
        return id;
    }

    @Handle
    @Path("/add")
    @PlainText
    public Integer add(
        @QueryParam("a") Integer a,
        @QueryParam("b") Integer b) throws IOException {
        return a + b;
    }

    @Override
    public void handleError(Exception e) {
    }

    @Override
    public void generateDoc() {

    }
}

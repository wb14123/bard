package org.binwang.bard.example;

import org.binwang.bard.core.Handler;
import org.binwang.bard.util.Path;
import org.binwang.bard.util.PlainText;
import org.binwang.bard.util.QueryParam;
import org.binwang.bard.util.Required;

import java.io.IOException;

@PlainText
public class SimpleHandler extends Handler {

    @Path("/get")
    public Integer printParams(@QueryParam("id") Integer id) throws IOException {
        return id;
    }

    @Path("/add")
    public Integer add(
        @QueryParam("a") @Required Integer a,
        @QueryParam("b") @Required Integer b) throws IOException {
        return a + b;
    }
}

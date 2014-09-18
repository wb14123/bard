package org.binwang.bard.example;

import org.binwang.bard.basic.marker.*;
import org.binwang.bard.core.Handler;

import java.io.IOException;

@PlainText
@Path("/myapp")
public class SimpleHandler extends Handler {

    @Path("/get")
    @Doc("A simple handler just return what you give")
    public User printParams(@QueryParam("id") Integer id) throws IOException {
        User user = new User();
        user.id = id;
        return user;
    }

    @Path("/add")
    @Doc("Add two numbers")
    public Integer add(
        @QueryParam("a") @Required @Doc("first number") Integer a,
        @QueryParam("b") @Required @Doc("second number") Integer b) throws IOException {
        return a + b;
    }
}

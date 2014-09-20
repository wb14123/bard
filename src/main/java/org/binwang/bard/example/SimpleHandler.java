package org.binwang.bard.example;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.Handler;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.IOException;

@Produces("application/json")
@Path("/myapp")
public class SimpleHandler extends Handler {

    @Path("/get/{id}/{name}")
    @Doc("A simple handler just return what you give")
    public User printParams(
        @PathParam("id") Integer id,
        @PathParam("name") String name
    ) throws IOException {
        User user = new User();
        user.id = id;
        user.name = name;
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

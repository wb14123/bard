package org.binwang.bard.example.simple;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.Handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Produces("application/json")
public class SimpleHandler extends Handler {

    @GET
    @Path("/add")
    @Doc("Add two numbers")
    public int add(
        @Doc("The first number") @QueryParam("a") @Required int a,
        @Doc("The second number") @QueryParam("b") @Required int b) {
        return a + b;
    }

}

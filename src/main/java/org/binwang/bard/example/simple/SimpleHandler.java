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
    @Doc("Get a user by id")
    public int getUser(
        @QueryParam("a") @Required int a,
        @QueryParam("b") @Required int b) {
        return a + b;
    }

}

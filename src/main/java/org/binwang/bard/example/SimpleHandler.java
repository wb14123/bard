package org.binwang.bard.example;

import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.Handler;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

@Produces("application/json")
@Path("/user")
public class SimpleHandler extends Handler {

    public static Map<Integer, User> UserStorage = new HashMap<>();

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id") @Required int id) {
        return UserStorage.get(id);
    }

    @PUT
    public User insertUser(
        @FormParam("id") @Required int id,
        @FormParam("name") String name) {
        User user = new User();
        user.id = id;
        user.name = name;
        UserStorage.put(id, user);
        return user;
    }
}

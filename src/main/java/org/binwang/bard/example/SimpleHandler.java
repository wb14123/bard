package org.binwang.bard.example;

import org.binwang.bard.basic.marker.Doc;
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
    @Doc("Get a user by id")
    public User getUser(@PathParam("id") @Required int id) {
        return UserStorage.get(id);
    }

    @PUT
    @Doc("Create a new user")
    public User insertUser(
        @FormParam("id") @Required int id,
        @FormParam("name") String name) {
        User user = new User();
        user.id = id;
        user.name = name;
        UserStorage.put(id, user);
        return user;
    }

    @DELETE
    @Path("/{id}")
    @Doc("Delete a user")
    public String deleteUser(@PathParam("id") @Required int id) {
        UserStorage.remove(id);
        return "ok";
    }

    @POST
    @Doc("Update a user")
    public User updateUser(
        @FormParam("id") @Required int id,
        @FormParam("name") String name) {
        User user = UserStorage.get(id);
        if (user == null) {
            return null;
        }
        if (name != null) {
            user.name = name;
        }
        UserStorage.put(id, user);
        return user;
    }
}

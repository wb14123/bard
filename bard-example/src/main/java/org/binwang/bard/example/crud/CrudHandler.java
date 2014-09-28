package org.binwang.bard.example.crud;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.Handler;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

@Produces("application/json")
@Path("/user")
public class CrudHandler extends Handler {

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
    @HandleErrors({
        @ErrorCase(code = 20000, logLevel = "DEBUG", exception = UserNotFoundException.class,
            description = "user not found")
    })
    public User deleteUser(@PathParam("id") @Required int id) throws UserNotFoundException {
        User user = UserStorage.remove(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
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

    public static class UserNotFoundException extends Exception {
        public static final long serialVersionUID = 1L;

        public UserNotFoundException(Integer userId) {
            super("user " + userId.toString() + " not found.");
        }
    }
}

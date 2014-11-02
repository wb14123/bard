package com.bardframework.bard.example.db;

import com.bardframework.bard.basic.marker.*;
import com.bardframework.bard.core.Handler;
import org.binwang.bard.util.db.marker.DBSession;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@LogRequest
@Produces("application/json")
public class SimpleHandler extends Handler {

    @DBSession public Session dbSession;

    @GET
    @Path("/get")
    @Doc("Get a user")
    @HandleErrors({
        @ErrorCase(code = 20000, exception = ObjectNotFoundException.class, logLevel = "DEBUG",
            description = "User not found")
    })
    public User getUser(
        @Doc("User id") @QueryParam("id") @Required long userId
    ) {
        User user = new User();
        dbSession.load(user, userId);
        return user;
    }

    @GET
    @Path("/insert")
    @Doc("Insert a user")
    public String insertUser(
        @Doc("User id") @QueryParam("id") @Required long id,
        @Doc("User name") @QueryParam("name") @Required String name
    ) {
        User user = new User();
        user.id = id;
        user.name = name;
        dbSession.save(user);
        return "ok";
    }
}

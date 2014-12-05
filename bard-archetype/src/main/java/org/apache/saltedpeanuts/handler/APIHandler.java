package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.marker.APIDoc;
import com.bardframework.bard.core.Handler;
import org.apache.saltedpeanuts.SimpleServlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class APIHandler extends Handler {
    @Path("/api-doc")
    @GET
    @APIDoc(value="Bard Simple", servletClass = SimpleServlet.class)
    public void getAPI() {
    }
}

package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.marker.APIDoc;
import com.bardframework.bard.core.Handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class APIHandler extends Handler {
    @Path("/api-doc")
    @GET
    @APIDoc
    public void getAPI() {
    }
}

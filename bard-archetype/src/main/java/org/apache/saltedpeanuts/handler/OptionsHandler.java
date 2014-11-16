package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.core.Handler;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;

public class OptionsHandler extends Handler {
    @Path("/{all:.*}")
    @OPTIONS
    @Doc(
        "This is a handler that handle requests with OPTIONS request. It just returns empty string")
    public String handle() {
        return "";
    }
}

package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.core.Handler;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;

public class OptionsHandler extends Handler {
    @Path("/{all:.*}")
    @OPTIONS
    public String handle() {
        return "";
    }
}

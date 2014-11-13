package org.apache.saltedpeanuts;

import com.bardframework.bard.basic.marker.CORSHeaders;
import com.bardframework.bard.basic.marker.LogRequest;
import com.bardframework.bard.core.Servlet;

import javax.ws.rs.Produces;


@LogRequest
@CORSHeaders
@Produces("application/json")
public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    @Override public String[] getPackageNames() {
        return new String[] {
            "com.bardframework.bard.core",
            "com.bardframework.bard.basic",
            "com.bardframework.bard.util",
            "org.apache.saltedpeanuts"
        };
    }
}

package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.basic.marker.Required;
import com.bardframework.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HandleErrorsFilterTest extends GenericTester {
    @Test
    public void testBreakChain() throws ServletException, IOException {
        request.setPathInfo("/produce-test/json");
        servlet.service(request, response);
        assertEquals(null, response.getHeader("here"));
    }


    @Path("/handle-error")
    public static class HandleErrorHandler extends Handler {
        // @Required include @HandleErrors, which should break the chain
        public void breakChain(@Required int a) {
            context.getResponse().addHeader("here", "true");
        }
    }
}

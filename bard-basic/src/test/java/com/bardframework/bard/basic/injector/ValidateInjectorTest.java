package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ValidateInjectorTest extends GenericTester {

    @Test
    public void biggerThanMax() throws ServletException, IOException {
        request.setPathInfo("/bigger-than-max");
        servlet.service(request, response);
        assertNull(response.getHeader("ok"));
    }

    @Test
    public void smallerThanMax() throws ServletException, IOException {
        request.setPathInfo("/smaller-than-max");
        servlet.service(request, response);
        assertNotNull(response.getHeader("ok"));
    }

    @Test
    public void sameAsMax() throws ServletException, IOException {
        request.setPathInfo("/same-as-max");
        servlet.service(request, response);
        assertNotNull(response.getHeader("ok"));
    }

    @Test
    public void biggerThanMin() throws ServletException, IOException {
        request.setPathInfo("/bigger-than-min");
        servlet.service(request, response);
        assertNotNull(response.getHeader("ok"));
    }

    @Test
    public void smallerThanMin() throws ServletException, IOException {
        request.setPathInfo("/smaller-than-min");
        servlet.service(request, response);
        assertNull(response.getHeader("ok"));
    }

    @Test
    public void sameAsMin() throws ServletException, IOException {
        request.setPathInfo("/same-as-min");
        servlet.service(request, response);
        assertNotNull(response.getHeader("ok"));
    }

    public static class TestHandler extends Handler {
        @Path("/bigger-than-max")
        public void biggerThanMax(@DefaultValue("100") @Max(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }

        @Path("/smaller-than-max")
        public void smallerThanMax(@DefaultValue("1") @Max(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }

        @Path("/same-as-max")
        public void sameAsMax(@DefaultValue("10") @Max(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }

        @Path("/bigger-than-min")
        public void biggerThanMin(@DefaultValue("100") @Min(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }

        @Path("/smaller-than-min")
        public void smallerThanMin(@DefaultValue("1") @Min(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }

        @Path("/same-as-min")
        public void sameAsMin(@DefaultValue("10") @Min(value = 10) long a) {
            context.getResponse().setHeader("ok", "ok");
        }
    }
}

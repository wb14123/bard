package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.ErrorResult;
import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.core.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ProduceFilterTest extends GenericTester {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void jsonTest() throws ServletException, IOException {
        request.setPathInfo("/produce-test/json");
        servlet.service(request, response);
        String content = response.getContentAsString();
        TesterClass tester = mapper.readValue(content, TesterClass.class);
        assertEquals(1, tester.a);
        assertThat(response.getHeader("Content-Type"),
            CoreMatchers.containsString("application/json"));
    }

    @Test
    public void otherTest() throws ServletException, IOException {
        request.setPathInfo("/produce-test/other");
        servlet.service(request, response);
        String content = response.getContentAsString();
        ErrorResult result = mapper.readValue(content, ErrorResult.class);
        assertEquals(500, result.code);
        assertThat(response.getHeader("Content-Type"),
            CoreMatchers.containsString("application/json"));
    }


    public static class TesterClass {
        public int a = 1;
    }


    @Path("/produce-test")
    public static class ProduceTestHandler extends Handler {
        @Path("/json")
        @Produces("application/json")
        public TesterClass jsonTest() {
            return new TesterClass();
        }

        @Path("/other")
        @Produces("application/other")
        public TesterClass otherTest() {
            return new TesterClass();
        }
    }


}

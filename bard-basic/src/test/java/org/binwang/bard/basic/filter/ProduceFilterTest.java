package org.binwang.bard.basic.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.binwang.bard.basic.ErrorResult;
import org.binwang.bard.basic.GenericTester;
import org.binwang.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ProduceFilterTest extends GenericTester {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void jsonTest() throws ServletException, IOException {
        request.setPathInfo("/produce-test/json");
        servlet.service(request, response);
        assertEquals("application/json", response.getHeader("Content-Type"));
        String content = response.getContentAsString();
        TesterClass tester = mapper.readValue(content, TesterClass.class);
        assertEquals(1, tester.a);
    }

    @Test
    public void otherTest() throws ServletException, IOException {
        request.setPathInfo("/produce-test/other");
        servlet.service(request, response);
        String content = response.getContentAsString();
        ErrorResult result = mapper.readValue(content, ErrorResult.class);
        assertEquals(500, result.code);
        assertEquals("application/json", response.getHeader("Content-Type"));
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

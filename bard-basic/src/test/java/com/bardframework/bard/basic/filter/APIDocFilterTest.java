package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.basic.marker.APIDoc;
import com.bardframework.bard.core.Handler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class APIDocFilterTest extends GenericTester {
    @Test
    public void testDoc() throws IOException, ServletException {
        request.setPathInfo("/test-api-doc");
        servlet.service(request, response);
        ObjectMapper mapper = new ObjectMapper();
        String content = response.getContentAsString();
        TypeReference<HashMap<String, Object>> typeRef
            = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> doc = mapper.readValue(content, typeRef);
        assertEquals("name", doc.get("name"));
        assertNotNull(doc.get("models"));
        assertNotNull(doc.get("APIs"));
    }

    public static class APIHandler extends Handler {
        @Path("/test-api-doc")
        @Produces("application/json")
        @APIDoc("name")
        public void getAPI() {
        }
    }

}

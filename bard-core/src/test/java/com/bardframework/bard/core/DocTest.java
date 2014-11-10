package com.bardframework.bard.core;

import com.bardframework.bard.core.defines.AddHeaderFilter;
import com.bardframework.bard.core.defines.IntegerZeroInjector;
import com.bardframework.bard.core.defines.TrueAdapter1;
import com.bardframework.bard.core.doc.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

public class DocTest {
    public Servlet servlet = null;
    public MockHttpServletRequest request;
    public MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet() {
            @Override protected String[] getPackageNames() {
                return new String[] {"com.bardframework.bard.core.defines"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testDoc() throws IOException {
        servlet.addHandler(ComplexHandler.class);
        request.setPathInfo("/api-doc");
        servlet.service(request, response);
        ObjectMapper mapper = new ObjectMapper();
        String content = response.getContentAsString();
        Document document = mapper.readValue(content, Document.class);
        Assert.assertNotNull(document);
        servlet.removeHandler(ComplexHandler.class);
    }

    @AddHeaderFilter(name = "complex-handler", value = "true")
    public static class ComplexHandler extends Handler {
        @TrueAdapter1
        @AddHeaderFilter(name = "complex-filter-2", value = "true")
        public void addHeader(@IntegerZeroInjector int a) {
        }
    }
}

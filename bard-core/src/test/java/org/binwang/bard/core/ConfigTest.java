package org.binwang.bard.core;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.binwang.bard.core.defines.TrueAdapter1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigTest {
    public Servlet servlet = null;
    public HttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        NoSuchFieldException, JsonMappingException {
        servlet = new Servlet() {
            @Override protected String[] getPackageNames() {
                return new String[] {"org.binwang.bard.core.defines"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testConfig() {
        servlet.addHandler(ConfigHandler.class);
        servlet.service(request, response);
        Assert.assertEquals("1", response.getHeader("test.int"));
        Assert.assertEquals("abc", response.getHeader("test.string"));
    }

    public static class ConfigHandler extends Handler {
        @TrueAdapter1
        public void getConfig() {
            context.response.addHeader("test.int", Util.getConfig().getString("test.int"));
            context.response.addHeader("test.string", Util.getConfig().getString("test.string"));
        }
    }
}

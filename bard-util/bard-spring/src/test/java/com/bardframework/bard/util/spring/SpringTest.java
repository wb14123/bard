package com.bardframework.bard.util.spring;

import com.bardframework.bard.core.Handler;
import com.bardframework.bard.core.Servlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SpringTest {

    private Servlet servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        SpringHandlerFactory.setContext(context);

        servlet = new TestServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testFilterBean() throws ServletException, IOException {
        request.setPathInfo("/util/spring/filter-test");
        servlet.service(request, response);
        assertEquals("hello", response.getHeader("filter-embed"));
    }

    @Test
    public void testHandlerBean() throws ServletException, IOException {
        request.setPathInfo("/util/spring/handler-test");
        servlet.service(request, response);
        assertEquals("hello", response.getHeader("handler-embed"));
    }

    public static class TestServlet extends Servlet {
        @Override public String[] getPackageNames() {
            return new String[] {
                "com.bardframework.bard.basic",
                "com.bardframework.bard.util"
            };
        }
    }


    @Component
    @Path("/util/spring")
    public static class BeanHandlerTester extends Handler {
        private Embed embed;

        @Autowired
        public BeanHandlerTester(Embed embed) {
            this.embed = embed;
        }

        @Path("/filter-test")
        @FilterBeanTester
        public void filterTest() {
        }

        @Path("/handler-test")
        public void handlerTest() {
            context.getResponse().addHeader("handler-embed", embed.getMessage());
        }
    }


    @Configuration
    @ComponentScan
    public static class Application {
        @Bean Embed mockEmbed() {
            return new Embed() {
                @Override public String getMessage() {
                    return "hello";
                }
            };
        }
    }
}

package org.binwang.bard.example;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.binwang.bard.core.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends Servlet {

    public SimpleServlet()
        throws NoSuchFieldException, InstantiationException, IllegalAccessException,
        NoSuchMethodException, JsonMappingException {
        super("org.binwang.bard.basic", "org.binwang.bard.example");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

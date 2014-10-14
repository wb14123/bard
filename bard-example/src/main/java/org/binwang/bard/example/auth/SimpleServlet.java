package org.binwang.bard.example.auth;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.binwang.bard.core.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    public SimpleServlet()
        throws NoSuchFieldException, InstantiationException, IllegalAccessException,
        NoSuchMethodException, JsonMappingException {
        super("org.binwang.bard.basic", "org.binwang.bard.util", "org.binwang.bard.example.auth");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

package org.binwang.bard.example;

import org.binwang.bard.core.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends Servlet {

    public SimpleServlet()
        throws NoSuchFieldException, InstantiationException, IllegalAccessException,
        NoSuchMethodException {
        super("org.binwang.bard.util", "org.binwang.bard.example");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

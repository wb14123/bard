package org.binwang.bard.example;

import org.binwang.bard.core.Servlet;
import org.binwang.bard.util.UtilServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleServlet extends UtilServlet {

    public SimpleServlet() {
        super();
        addHandler(SimpleHandler.class);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

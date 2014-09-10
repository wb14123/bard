package org.binwang.bard.example;

import org.binwang.bard.util.UtilServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends UtilServlet {

    public SimpleServlet() {
        super();
        addHandler(SimpleHandler.class);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

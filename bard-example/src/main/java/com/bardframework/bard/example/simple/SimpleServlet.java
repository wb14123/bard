package com.bardframework.bard.example.simple;

import com.bardframework.bard.core.Servlet;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    @Override public String[] getPackageNames() {
        return new String[] {
            "com.bardframework.bard.basic",
            "com.bardframework.bard.example.simple"
        };
    }

}

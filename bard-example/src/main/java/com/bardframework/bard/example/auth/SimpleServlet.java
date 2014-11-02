package com.bardframework.bard.example.auth;

import com.bardframework.bard.core.Servlet;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    @Override protected String[] getPackageNames() {
        return new String[] {
            "com.bardframework.bard.basic",
            "org.bardframework.bard.util",
            "org.bardframework.bard.example.auth"
        };
    }
}

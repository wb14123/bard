package com.bardframework.bard.example.crud;

import com.bardframework.bard.core.Servlet;

public class CrudServlet extends Servlet {
    public static final long serialVersionUID = 2L;

    @Override protected String[] getPackageNames() {
        return new String[] {
            "com.bardframework.bard.basic",
            "com.bardframework.bard.example.crud"
        };
    }
}

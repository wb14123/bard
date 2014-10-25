package org.binwang.bard.example.db;

import org.binwang.bard.core.Servlet;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    @Override protected String[] getPackageNames() {
        return new String[] {
            "org.binwang.bard.basic",
            "org.binwang.bard.util",
            "org.binwang.bard.example.db"
        };
    }

}

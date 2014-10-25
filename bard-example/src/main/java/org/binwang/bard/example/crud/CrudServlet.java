package org.binwang.bard.example.crud;

import org.binwang.bard.core.Servlet;

public class CrudServlet extends Servlet {
    public static final long serialVersionUID = 2L;

    @Override protected String[] getPackageNames() {
        return new String[] {
            "org.binwang.bard.basic",
            "org.binwang.bard.example.crud"
        };
    }
}

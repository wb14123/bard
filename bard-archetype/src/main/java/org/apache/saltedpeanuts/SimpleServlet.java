package org.apache.saltedpeanuts;

import com.bardframework.bard.core.Servlet;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    @Override protected String[] getPackageNames() {
        return new String[] {
            "com.bardframework.bard.basic",
            "com.bardframework.bard.util",
            "org.apache.saltedpeanuts"
        };
    }
}

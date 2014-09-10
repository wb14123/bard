package org.binwang.bard.util;


import org.binwang.bard.core.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilServlet extends Servlet {
    public UtilServlet() {
        addInjector(QueryParam.class, QueryParamInjector.class);
        addAdapter(Path.class, PathAdapter.class);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

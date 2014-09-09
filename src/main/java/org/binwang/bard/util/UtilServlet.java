package org.binwang.bard.util;


import org.binwang.bard.core.Servlet;

public class UtilServlet extends Servlet {
    public UtilServlet() {
        addInjector(QueryParam.class, QueryParamInjector.class);
    }
}

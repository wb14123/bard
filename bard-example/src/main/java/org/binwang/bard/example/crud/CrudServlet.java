package org.binwang.bard.example.crud;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.binwang.bard.core.Servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrudServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    public CrudServlet()
        throws NoSuchFieldException, InstantiationException, IllegalAccessException,
        NoSuchMethodException, JsonMappingException {
        super("org.binwang.bard.basic", "org.binwang.bard.example.crud");
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

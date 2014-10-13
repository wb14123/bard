package org.binwang.bard.example.db;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.binwang.bard.core.Servlet;
import org.binwang.bard.util.db.DBManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends Servlet {
    public static final long serialVersionUID = 1L;

    public SimpleServlet()
        throws NoSuchFieldException, InstantiationException, IllegalAccessException,
        NoSuchMethodException, JsonMappingException {
        super("org.binwang.bard.basic", "org.binwang.bard.util", "org.binwang.bard.example.db");
        String[] modelPkgs = {"org.binwang.bard.example.db"};
        Class<?>[] modelClasses = {User.class};
        DBManager.init(modelPkgs, modelClasses);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        super.service(req, resp);
    }
}

package org.binwang.bard.core;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Context {
    // jetty handler args
    public String target;
    public Request baseRequest;
    public HttpServletRequest request;
    public HttpServletResponse response;

    // params
    public Map<String, Object> queryParams = new HashMap<String, Object>();

    // custom context
    public Map<String, Object> custom = new HashMap<String, Object>();
}

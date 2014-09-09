package org.binwang.bard.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Context {
    public HttpServletRequest request;
    public HttpServletResponse response;

    // custom context
    public Map<String, Object> custom = new HashMap<String, Object>();

    public Context(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}

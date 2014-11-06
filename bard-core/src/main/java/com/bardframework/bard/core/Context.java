package com.bardframework.bard.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used in GenericHandler, so that you could store information while run
 * the middleware and handlers.
 */
public class Context {
    /**
     * The raw HttpServletRequest
     */
    public HttpServletRequest request;

    /**
     * The raw HttpServletResponse
     */
    public HttpServletResponse response;

    /**
     * The result got by handler
     */
    public Object result;

    /**
     * If there is any exception while running the chain, it is stored here.
     */
    public Throwable exception;

    /**
     * Is the exception handled? If not, the framework will through it.
     */
    public boolean exceptionHandled = false;

    /**
     * The custom map so that you could put anything into the context.
     */
    public Map<String, Object> custom = new HashMap<>();

    public Context(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}

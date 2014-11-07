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
    protected HttpServletRequest request;

    /**
     * The raw HttpServletResponse
     */
    protected HttpServletResponse response;

    /**
     * The result got by handler
     */
    protected Object result;

    /**
     * If there is any exception while running the chain, it is stored here.
     */
    protected Throwable exception;
    /**
     * Only used by {@link Injector}, store the current injectorVariable that will be injected.
     */
    protected Object injectorVariable;
    /**
     * Only used by {@link Injector}, the type of inject injectorVariable.
     */
    protected Class<?> injectorVariableType = Object.class;
    /**
     * The custom map so that you could put anything into the context.
     */
    protected Map<String, Object> custom = new HashMap<>();
    /**
     * Is the exception handled? If not, the framework will through it.
     */
    private boolean exceptionHandled = false;

    public Context(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public <T> void putCustom(String key, T value) {
        custom.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCustom(String key) {
        return (T) custom.get(key);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isExceptionHandled() {
        return exceptionHandled;
    }

    public void setExceptionHandled(boolean exceptionHandled) {
        this.exceptionHandled = exceptionHandled;
    }

    public Object getInjectorVariable() {
        return injectorVariable;
    }

    public void setInjectorVariable(Object injectorVariable) {
        this.injectorVariable = injectorVariable;
    }

    public Class<?> getInjectorVariableType() {
        return injectorVariableType;
    }
}

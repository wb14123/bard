package com.bardframework.bard.core;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class HandlerMethod {
    public Method method;
    public Class<? extends Servlet> servletClass;

    public List<AnnotatedHandler<? extends Filter>> annotatedFilters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedAdapters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedClassAdapters = new LinkedList<>();
    public List<AnnotatedHandler<? extends Adapter>> annotatedServletAdapters = new LinkedList<>();
    public List<HandlerParameter> parameters = new LinkedList<>();
    public List<HandlerField> fields = new LinkedList<>();
}

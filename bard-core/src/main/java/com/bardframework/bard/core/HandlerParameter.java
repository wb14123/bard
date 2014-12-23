package com.bardframework.bard.core;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class HandlerParameter {
    public Parameter parameter;
    public Class<? extends Servlet> servletClass;
    public List<AnnotatedHandler<? extends Injector>> annotatedInjectors = new LinkedList<>();
}

package com.bardframework.bard.core;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class HandlerField {
    public Field field;
    public Class<? extends Servlet> servletClass;

    public List<AnnotatedHandler<? extends Injector>> annotatedInjectors = new LinkedList<>();
}

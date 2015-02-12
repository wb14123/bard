package com.bardframework.bard.basic.marker;

import com.bardframework.bard.core.Servlet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface APIDoc {
    public String value() default "unnamed";

    public Class<? extends Servlet> servletClass();
}

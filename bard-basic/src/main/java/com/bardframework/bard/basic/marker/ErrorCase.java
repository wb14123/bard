package com.bardframework.bard.basic.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorCase {
    public Class<? extends Exception> exception();

    public int code();

    public String logLevel();

    public String description();
}

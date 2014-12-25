package com.bardframework.bard.util.user.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
    public boolean required() default false;
}

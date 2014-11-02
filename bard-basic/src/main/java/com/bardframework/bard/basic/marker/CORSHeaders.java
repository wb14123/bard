package com.bardframework.bard.basic.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CORSHeaders {
    public String origin() default "*";

    public String methods() default "GET, POST, PUT, DELETE, OPTIONS";

    public String headers() default "accept, auth-token, Content-Type";
}

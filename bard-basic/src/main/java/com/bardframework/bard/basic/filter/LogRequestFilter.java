package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.marker.LogRequest;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.Util;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

import java.util.Date;

@BindTo(LogRequest.class)
public class LogRequestFilter extends Filter<LogRequest> {
    private Date startTime;

    @Before public void before() {
        startTime = new Date();
    }

    @After public void after() {
        Date endTime = new Date();
        long timeDiff = endTime.getTime() - startTime.getTime();
        String queryString = context.getRequest().getQueryString();
        if (queryString == null) {
            queryString = "";
        } else {
            queryString = "?" + queryString;
        }
        Util.getLogger().info("Request complete, method: {}, URI: {}{}, time: {}ms",
            context.getRequest().getMethod(),
            context.getRequest().getRequestURI(),
            queryString,
            timeDiff);
        if (context.getException() != null && !context.isExceptionHandled()) {
            Util.getLogger().warn("Exception found after request: {}", context.getException());
        }
    }

    @Override public void generateDoc() {
    }
}

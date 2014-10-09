package org.binwang.bard.basic.filter;

import org.binwang.bard.basic.marker.LogRequest;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

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
        String queryString = context.request.getQueryString();
        if (queryString == null) {
            queryString = "";
        } else {
            queryString = "?" + queryString;
        }
        logger.info("Request complete, method: {}, URI: {}{}, time: {}ms",
            context.request.getMethod(),
            context.request.getRequestURI(),
            queryString,
            timeDiff);
    }

    @Override public void generateDoc() {
    }
}

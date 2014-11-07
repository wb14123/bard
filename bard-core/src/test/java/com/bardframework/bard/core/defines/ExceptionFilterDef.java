package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

@BindTo(ExceptionFilter.class)
public class ExceptionFilterDef extends Filter<ExceptionFilter> {
    @Before public void putException() throws TestFilterException {
        throw new TestFilterException();
    }

    @After public void getException() {
        if (context.getException() instanceof TestFilterException) {
            context.getResponse().setHeader("exception", "true");
        }
    }

    @Override public void generateDoc() {
    }


    public static class TestFilterException extends Exception {
    }
}

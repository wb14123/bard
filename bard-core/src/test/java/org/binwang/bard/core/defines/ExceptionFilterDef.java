package org.binwang.bard.core.defines;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

@BindTo(ExceptionFilter.class)
public class ExceptionFilterDef extends Filter<ExceptionFilter> {
    @Before public void putException() throws TestFilterException {
        throw new TestFilterException();
    }

    @After public void getException() {
        if (context.exception instanceof TestFilterException) {
            context.response.setHeader("exception", "true");
        }
    }

    @Override public void generateDoc() {
    }


    public static class TestFilterException extends Exception {
    }
}

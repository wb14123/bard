package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;

@BindTo(ExceptionInjector.class)
public class ExceptionInjectorDef extends Injector<ExceptionInjector> {
    @Before public void putException() throws TestInjectorException {
        throw new TestInjectorException();
    }

    @After public void handleException() {
        if (context.exception instanceof TestInjectorException) {
            context.response.addHeader("get_exception", "true");
        }
    }

    @Override public void generateDoc() {
    }


    public static class TestInjectorException extends Exception {
    }
}

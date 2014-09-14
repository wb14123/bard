package org.binwang.bard.core.defines;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

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

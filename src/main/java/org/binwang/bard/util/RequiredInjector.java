package org.binwang.bard.util;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;

@BindTo(Required.class)
public class RequiredInjector extends Injector<Required> {
    @Before
    public void validateParam() throws RequiredNullException {
        if (injectorVariable == null) {
            throw new RequiredNullException();
        }
    }

    @After
    public void handleNullRequired() {
        if (context.exception instanceof RequiredNullException) {
            context.exception = null;
            context.result = "should not be null";
        }
    }

    @Override public void generateDoc() {
        docParameter.limitations.put("required", true);
    }


    public static class RequiredNullException extends Exception {
    }
}

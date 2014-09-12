package org.binwang.bard.core.defines;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.After;

@BindTo(ExceptionFilter.class)
public class ExceptionFilterDef extends Filter<ExceptionFilter> {
    @After public void getException() {
        if (context.exception != null) {
            context.response.setHeader("exception", "true");
        }
    }

    @Override public void generateDoc() {
    }
}

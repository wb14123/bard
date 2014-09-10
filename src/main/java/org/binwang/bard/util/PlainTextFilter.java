package org.binwang.bard.util;

import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.After;

import java.io.IOException;

public class PlainTextFilter extends Filter<PlainText> {

    @After
    public void writeResult() throws IOException {
        context.response.getWriter().print(context.result);
    }

    @Override
    public void handleError(Exception e) {

    }

    @Override
    public void generateDoc() {

    }
}

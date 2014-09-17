package org.binwang.bard.util;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.doc.Response;
import org.binwang.bard.core.marker.After;

import java.io.IOException;

@BindTo(PlainText.class)
public class PlainTextFilter extends Filter<PlainText> {

    @After
    public void writeResult() throws IOException {
        if (context.exception != null) {
            context.response.getWriter().print(context.exception);
        } else {
            context.response.getWriter().print(context.result);
        }
    }

    @Override
    public void generateDoc() {
        api.produces = "text/plain";
        Response response = new Response();
        response.code = 200;
        response.returnType = returnType;
        api.responses.add(response);
    }
}

package org.binwang.bard.basic.filter;

import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.doc.Response;
import org.binwang.bard.core.marker.After;

@BindTo(HandleErrors.class)
public class HandleErrorsFilter extends Filter<HandleErrors> {
    @After public void handleError() {
        for (ErrorCase errorCase : annotation.value()) {
            if (context.exception != null && context.exception.getClass() == errorCase
                .exception()) {
                context.response.setStatus(errorCase.code());
                context.result = context.exception.toString();
                if (errorCase.logLevel().equals("ERROR")) {
                    context.exception.printStackTrace();
                }
                break;
            }
        }
    }

    @Override public void generateDoc() {
        for (ErrorCase errorCase : annotation.value()) {
            Response response = new Response();
            response.code = errorCase.code();
            response.returnType = String.class;
            response.description = errorCase.description();
            api.responses.add(response);
        }
    }
}
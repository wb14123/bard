package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.ErrorResult;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.doc.Response;
import com.bardframework.bard.core.marker.After;

@BindTo(HandleErrors.class)
public class HandleErrorsFilter extends Filter<HandleErrors> {
    @After public void handleError() {
        for (ErrorCase errorCase : annotation.value()) {
            if (context.exception != null && context.exception.getClass() == errorCase
                .exception() && !context.exceptionHandled) {
                ErrorResult err = new ErrorResult();

                context.exceptionHandled = true;
                context.response.setStatus(errorCase.code());
                err.code = errorCase.code();
                String msg = context.exception.getMessage();
                if (msg == null) {
                    msg = context.exception.toString();
                }
                err.message = msg;
                context.result = err;
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

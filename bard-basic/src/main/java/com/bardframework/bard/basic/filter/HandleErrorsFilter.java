package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.ErrorResult;
import com.bardframework.bard.basic.marker.HandleError;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.Util;
import com.bardframework.bard.core.doc.Response;
import com.bardframework.bard.core.marker.After;

@BindTo(HandleErrors.class)
public class HandleErrorsFilter extends Filter<HandleErrors> {
    @After public void handleError() {
        for (HandleError handleError : annotation.value()) {
            if (context.getException() != null && context.getException().getClass() == handleError
                .exception() && !context.isExceptionHandled()) {
                ErrorResult err = new ErrorResult();

                context.setExceptionHandled(true);
                if (handleError.code() < 600 && handleError.code() >= 200) {
                    context.getResponse().setStatus(handleError.code());
                } else {
                    // set return status code to 400 if the error code is not validate
                    context.getResponse().setStatus(400);
                }
                err.code = handleError.code();
                String msg = context.getException().getMessage();
                if (msg == null) {
                    msg = context.getException().toString();
                }
                err.message = msg;
                context.setResult(err);
                Util.getLogger().debug("Handle error found exception: {}", context.getException());
                break;
            }
        }
    }

    @Override public void generateDoc() {
        for (HandleError handleError : annotation.value()) {
            Response response = new Response();
            response.code = handleError.code();
            response.returnType = ErrorResult.class;
            response.description = handleError.description();
            api.responses.add(response);
        }
    }
}

package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.ErrorResult;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.Util;
import com.bardframework.bard.core.doc.Response;
import com.bardframework.bard.core.marker.After;

@BindTo(HandleErrors.class)
public class HandleErrorsFilter extends Filter<HandleErrors> {
    @After public void handleError() {
        for (ErrorCase errorCase : annotation.value()) {
            if (context.getException() != null && context.getException().getClass() == errorCase
                .exception() && !context.isExceptionHandled()) {
                ErrorResult err = new ErrorResult();

                context.setExceptionHandled(true);
                context.getResponse().setStatus(errorCase.code());
                err.code = errorCase.code();
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
        for (ErrorCase errorCase : annotation.value()) {
            Response response = new Response();
            response.code = errorCase.code();
            response.returnType = ErrorResult.class;
            response.description = errorCase.description();
            api.responses.add(response);
        }
    }
}

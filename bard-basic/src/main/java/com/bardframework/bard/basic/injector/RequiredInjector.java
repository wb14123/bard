package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.basic.marker.Required;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

@BindTo(Required.class)
public class RequiredInjector extends Injector<Required> {
    @Before
    @HandleErrors({
        @ErrorCase(description = "required param doesn't provided",
            code = BardBasicError.REQUIRED_ERROR,
            exception = RequiredNullException.class, logLevel = "DEBUG")
    })
    public void validateParam() throws RequiredNullException {
        if (context.getInjectorVariable() == null) {
            String param = context.getCustom("param");
            if (param == null) {
                param = "";
            }
            if (!annotation.value().equals("")) {
                throw new RequiredNullException(annotation.value());
            } else {
                throw new RequiredNullException("Required param not found for " + param);
            }
        }
    }

    @Override public void generateDoc() {
        docParameter.limitations.put("required", true);
    }


    public static class RequiredNullException extends Exception {
        public static final long serialVersionUID = 1L;

        public RequiredNullException(String msg) {
            super(msg);
        }
    }
}

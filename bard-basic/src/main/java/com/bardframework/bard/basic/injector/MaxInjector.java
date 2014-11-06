package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

import javax.validation.ValidationException;
import javax.validation.constraints.Max;

@BindTo(Max.class)
@HandleErrors({
    @ErrorCase(code = BardBasicError.VALIDATION_ERROR, logLevel = "DEBUG",
        exception = ValidationException.class, description = "Param validation error")
})
public class MaxInjector extends Injector<Max> {
    @Before public void validate() {
        if (injectorVariable == null ||
            Long.valueOf(injectorVariable.toString()) > annotation.value()) {
            String paramName = (String) context.custom.get("param");
            if (paramName == null) {
                paramName = "";
            }
            throw new ValidationException(
                "Param \"" + paramName + "\" should be smaller than "
                    + Long.toString(annotation.value()));
        }
    }

    @Override public void generateDoc() {
        docParameter.limitations.put("max", annotation.value());
    }
}

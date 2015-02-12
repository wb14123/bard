package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.HandleError;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

import javax.validation.ValidationException;
import javax.validation.constraints.Max;

@BindTo(Max.class)
@HandleErrors({
    @HandleError(code = BardBasicError.VALIDATION_ERROR,
        exception = ValidationException.class, description = "Param validation error")
})
public class MaxInjector extends Injector<Max> {
    @Before public void validate() {
        if (context.getInjectorVariable() == null) {
            throw new ValidationException(
                "Param is null to validate: " + context.getCustom("param"));
        }
        if (Long.valueOf(context.getInjectorVariable().toString()) > annotation.value()) {
            String paramName = context.getCustom("param");
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

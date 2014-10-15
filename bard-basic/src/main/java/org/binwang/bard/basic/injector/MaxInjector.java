package org.binwang.bard.basic.injector;

import org.binwang.bard.basic.BardBasicError;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

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
            (Long) injectorVariable > annotation.value()) {
            String paramName = (String) injectContext.get("param");
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

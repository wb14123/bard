package org.binwang.bard.basic.injector;

import org.binwang.bard.basic.BardBasicError;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

@BindTo(Required.class)
public class RequiredInjector extends Injector<Required> {
    @Before
    @HandleErrors({
        @ErrorCase(description = "required param doesn't provided",
            code = BardBasicError.REQUIRED_ERROR,
            exception = RequiredNullException.class, logLevel = "DEBUG")
    })
    public void validateParam() throws RequiredNullException {
        if (injectorVariable == null) {
            throw new RequiredNullException();
        }
    }

    @Override public void generateDoc() {
        docParameter.limitations.put("required", true);
    }


    public static class RequiredNullException extends Exception {
        public static final long serialVersionUID = 1L;
    }
}

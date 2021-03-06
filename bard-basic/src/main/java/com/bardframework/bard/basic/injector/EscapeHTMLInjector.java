package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.marker.EscapeHTML;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import org.apache.commons.lang.StringEscapeUtils;

@BindTo(EscapeHTML.class)
public class EscapeHTMLInjector extends Injector<EscapeHTML> {

    @Before public void escape() {
        if (context.getInjectorVariable() != null
            && context.getInjectorVariableType() == String.class) {
            context.setInjectorVariable(
                StringEscapeUtils.escapeHtml((String) context.getInjectorVariable()));
        }
    }

    @Override public void generateDoc() {
        if (docParameter.description == null) {
            docParameter.description = "";
        }
        docParameter.description += "\nThe HTML characters will be escaped.\n";
    }
}

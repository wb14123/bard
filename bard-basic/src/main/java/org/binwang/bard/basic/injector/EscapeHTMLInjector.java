package org.binwang.bard.basic.injector;

import org.apache.commons.lang.StringEscapeUtils;
import org.binwang.bard.basic.marker.EscapeHTML;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

@BindTo(EscapeHTML.class)
public class EscapeHTMLInjector extends Injector<EscapeHTML> {

    @Before public void escape() {
        if (injectorVariable != null && injectorVariableType == String.class) {
            injectorVariable = StringEscapeUtils.escapeHtml((String) injectorVariable);
        }
    }

    @Override public void generateDoc() {
        if (docParameter.description == null) {
            docParameter.description = "";
        }
        docParameter.description += "\nThe HTML characters will be escaped.\n";
    }
}

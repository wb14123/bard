package org.binwang.bard.core.defines;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.Before;

@BindTo(AddHeaderFilter.class)
public class AddHeaderFilterDef extends Filter<AddHeaderFilter> {
    @Before public void addHeader() {
        context.response.setHeader(annotation.name(), annotation.value());
    }

    @Override public void generateDoc() {
    }
}

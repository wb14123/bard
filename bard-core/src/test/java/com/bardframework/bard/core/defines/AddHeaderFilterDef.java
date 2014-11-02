package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.marker.Before;

@BindTo(AddHeaderFilter.class)
public class AddHeaderFilterDef extends Filter<AddHeaderFilter> {
    @Before public void addHeader() {
        context.response.setHeader(annotation.name(), annotation.value());
    }

    @Override public void generateDoc() {
    }
}

package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.marker.CORSHeaders;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.marker.Before;

@BindTo(CORSHeaders.class)
public class CORSHeadersFilter extends Filter<CORSHeaders> {
    @Before public void addHeaders() {
        context.getResponse().addHeader("Access-Control-Allow-Origin", annotation.origin());
        context.getResponse().addHeader("Access-Control-Allow-Methods", annotation.methods());
        context.getResponse().addHeader("Access-Control-Allow-Headers", annotation.headers());
    }

    @Override public void generateDoc() {
    }
}

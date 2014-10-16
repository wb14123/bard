package org.binwang.bard.basic.filter;

import org.binwang.bard.basic.marker.CORSHeaders;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.marker.Before;

@BindTo(CORSHeaders.class)
public class CORSHeadersFilter extends Filter<CORSHeaders> {
    @Before public void addHeaders() {
        context.response.addHeader("Access-Control-Allow-Origin", annotation.origin());
        context.response.addHeader("Access-Control-Allow-Methods", annotation.methods());
        context.response.addHeader("Access-Control-Allow-Headers", annotation.headers());
    }

    @Override public void generateDoc() {
    }
}

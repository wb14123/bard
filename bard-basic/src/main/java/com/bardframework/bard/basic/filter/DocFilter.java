package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;

@BindTo(Doc.class)
public class DocFilter extends Filter<Doc> {
    @Override public void generateDoc() {
        api.description = annotation.value();
    }
}

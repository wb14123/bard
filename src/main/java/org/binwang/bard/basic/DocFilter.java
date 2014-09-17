package org.binwang.bard.basic;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;

@BindTo(Doc.class)
public class DocFilter extends Filter<Doc> {
    @Override public void generateDoc() {
        api.description = annotation.value();
    }
}

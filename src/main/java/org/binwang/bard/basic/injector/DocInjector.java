package org.binwang.bard.basic.injector;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;

@BindTo(Doc.class)
public class DocInjector extends Injector<Doc> {
    @Override public void generateDoc() {
        docParameter.description = annotation.value();
    }
}

package org.binwang.bard.util;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;

@BindTo(Doc.class)
public class DocInjector extends Injector<Doc> {
    @Override public void generateDoc() {
        docParameter.description = annotation.value();
    }
}

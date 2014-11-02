package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;

@BindTo(Doc.class)
public class DocInjector extends Injector<Doc> {
    @Override public void generateDoc() {
        docParameter.description = annotation.value();
    }
}

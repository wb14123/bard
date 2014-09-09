package org.binwang.bard.util;

import org.binwang.bard.core.AnnotationFilterMap;
import org.binwang.bard.core.Before;
import org.binwang.bard.core.Context;
import org.binwang.bard.core.Handler;

public class ParamsFilter<T> extends Handler<T, Params> {
    public ParamsFilter(Context context, T variable, Params annotation, AnnotationFilterMap map) {
        super(context, variable, annotation, map);
    }

    @Before
    public void getParams() {
        this.variable = null;
    }
}


package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationFilterMap {
    public Map<Class<? extends Annotation>, Class<? extends Handler>> map =
            new HashMap<Class<? extends Annotation>, Class<? extends Handler>>();
}

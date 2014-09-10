package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMapper {
    public Map<Class<? extends Annotation>, Class<? extends Filter>> filterMap =
        new HashMap<Class<? extends Annotation>, Class<? extends Filter>>();

    public Map<Class<? extends Annotation>, Class<? extends Injector>> injectorMap =
        new HashMap<Class<? extends Annotation>, Class<? extends Injector>>();

    public Map<Class<? extends Annotation>, Class<? extends Adapter>> adapterMap =
        new HashMap<Class<? extends Annotation>, Class<? extends Adapter>>();
}

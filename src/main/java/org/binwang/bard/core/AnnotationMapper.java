package org.binwang.bard.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMapper {
    public Map<Class<? extends Annotation>, Class<? extends Filter>> filterMap =
        new HashMap<>();

    public Map<Class<? extends Annotation>, Class<? extends Injector>> injectorMap =
        new HashMap<>();

    public Map<Class<? extends Annotation>, Class<? extends Adapter>> adapterMap =
        new HashMap<>();
}

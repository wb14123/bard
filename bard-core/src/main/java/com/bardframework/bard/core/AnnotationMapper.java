package com.bardframework.bard.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnnotationMapper {
    public Map<Class<? extends Annotation>, Class<? extends Filter>> filterMap =
        new HashMap<>();

    public Map<Class<? extends Annotation>, Class<? extends Injector>> injectorMap =
        new HashMap<>();

    public Map<Class<? extends Annotation>, Class<? extends Adapter>> adapterMap =
        new HashMap<>();

    public List<Class<? extends Handler>> handlers = new LinkedList<>();
}

package com.bardframework.bard.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class stores how to find handler class from annotation.
 */
public class AnnotationMapper {
    /**
     * A map from annotation to filter.
     */
    public Map<Class<? extends Annotation>, Class<? extends Filter>> filterMap =
        new HashMap<>();

    /**
     * A map from annotation to injector.
     */
    public Map<Class<? extends Annotation>, Class<? extends Injector>> injectorMap =
        new HashMap<>();

    /**
     * A map from annotation to adapter.
     */
    public Map<Class<? extends Annotation>, Class<? extends Adapter>> adapterMap =
        new HashMap<>();

    /**
     * All the handler handled by the servlet.
     */
    public List<Class<? extends Handler>> handlers = new LinkedList<>();
}

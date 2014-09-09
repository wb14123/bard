package org.binwang.bard.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private AnnotationMapper mapper = new AnnotationMapper();
    private List<Class<? extends Handler>> handlers = new LinkedList<Class<? extends Handler>>();

    public void addFilter(final Class<? extends Annotation> annotationClass,
                          final Class<? extends Filter> filterClass) {
        mapper.filterMap.put(annotationClass, filterClass);
    }

    public void addAdapter(final Class<? extends Annotation> annotationClass,
                           final Class<? extends Adapter> adapterClass) {
        mapper.adapterMap.put(annotationClass, adapterClass);
    }

    public void addInjector(final Class<? extends Annotation> annotationClass,
                            final Class<? extends Injector> injectorClass) {
        mapper.injectorMap.put(annotationClass, injectorClass);
    }

    public void addHandler(final Class<? extends Handler> handlerClass) {
        handlers.add(handlerClass);
    }

    public void serve(HttpServletRequest request, HttpServletResponse response) {
        try {
            // TODO: performance for adapter? (instead of linear time)
            for (Class<? extends Handler> handlerClass : handlers) {
                Context context = new Context(request, response);
                Handler handler = Handler.newInstance(handlerClass, context, mapper);
                Object result = handler.run();
                if (result != NoAdapter.NO_ADAPTER) {
                    return;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}


package org.binwang.bard.core;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public class Server extends org.eclipse.jetty.server.Server {

    private AnnotationFilterMap map;

    public void bind(Class<? extends Annotation> annotationClass,
                     Class<? extends Handler> handlerClass) {
        map.map.put(annotationClass, handlerClass);
    }

    public void addHandler(String method, String path, final Class<? extends Handler> handlerClass) {
        super.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s,
                               Request request,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse)
                    throws IOException, ServletException {
                Context c = new Context();
                c.target = s;
                c.baseRequest = request;
                c.request = httpServletRequest;
                c.response = httpServletResponse;

                try {
                    Handler handler = Handler.newHandlerInstance(handlerClass, c, map);
                    handler.run();
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
        });
    }
}


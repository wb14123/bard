package com.bardframework.bard.util.spring;

import com.bardframework.bard.core.GenericHandler;
import com.bardframework.bard.core.HandlerFactory;
import com.bardframework.bard.core.HandlerMeta;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

public class SpringHandlerFactory implements HandlerFactory {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        SpringHandlerFactory.context = context;
        HandlerMeta.registerHandlerFactory(Component.class, new SpringHandlerFactory());
    }

    @Override public <HandlerClass extends GenericHandler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException {
        return context.getBean(handlerClass);
    }
}

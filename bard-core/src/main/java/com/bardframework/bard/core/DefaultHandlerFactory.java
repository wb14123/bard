package com.bardframework.bard.core;

public class DefaultHandlerFactory implements HandlerFactory {
    @Override public <HandlerClass extends GenericHandler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException {
        try {
            return handlerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HandlerInitException(e);
        }
    }
}

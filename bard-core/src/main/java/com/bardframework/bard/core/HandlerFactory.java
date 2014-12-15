package com.bardframework.bard.core;

public interface HandlerFactory {
    public <HandlerClass extends GenericHandler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException;

    public static class HandlerInitException extends Exception {
        public HandlerInitException(Exception e) {
            super(e);
        }
    }
}

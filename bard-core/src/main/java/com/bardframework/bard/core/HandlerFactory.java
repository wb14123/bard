package com.bardframework.bard.core;

public interface HandlerFactory {
    public <HandlerClass extends Handler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException;

    public <AdapterClass extends Adapter> AdapterClass initAdapter(
        Class<AdapterClass> adapterClass) throws HandlerInitException;

    public <FilterClass extends Filter> FilterClass initFilter(
        Class<FilterClass> filterClass) throws HandlerInitException;

    public <InjectorClass extends Injector> InjectorClass initInjector(
        Class<InjectorClass> injectorClass) throws HandlerInitException;

    public static class HandlerInitException extends Exception {
        public HandlerInitException(Exception e) {
            super(e);
        }
    }
}

package com.bardframework.bard.core;

public class DefaultHandlerFactory implements HandlerFactory {
    @Override public <HandlerClass extends Handler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException {
        try {
            return handlerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HandlerInitException(e);
        }
    }

    @Override public <AdapterClass extends Adapter> AdapterClass initAdapter(
        Class<AdapterClass> adapterClass) throws HandlerInitException {
        try {
            return adapterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HandlerInitException(e);
        }
    }

    @Override public <FilterClass extends Filter> FilterClass initFilter(
        Class<FilterClass> filterClass) throws HandlerInitException {
        try {
            return filterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HandlerInitException(e);
        }
    }

    @Override public <InjectorClass extends Injector> InjectorClass initInjector(
        Class<InjectorClass> injectorClass) throws HandlerInitException {
        try {
            return injectorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new HandlerInitException(e);
        }
    }
}

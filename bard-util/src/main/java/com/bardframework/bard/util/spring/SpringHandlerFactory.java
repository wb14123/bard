package com.bardframework.bard.util.spring;

import com.bardframework.bard.core.*;
import org.springframework.context.ApplicationContext;

public class SpringHandlerFactory implements HandlerFactory {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        SpringHandlerFactory.context = context;
        HandlerMeta.handlerFactory = new SpringHandlerFactory();
    }

    @Override public <HandlerClass extends Handler> HandlerClass initHandler(
        Class<HandlerClass> handlerClass) throws HandlerInitException {
        return context.getBean(handlerClass);
    }

    @Override public <AdapterClass extends Adapter> AdapterClass initAdapter(
        Class<AdapterClass> adapterClass) throws HandlerInitException {
        return null;
    }

    @Override public <FilterClass extends Filter> FilterClass initFilter(
        Class<FilterClass> filterClass) throws HandlerInitException {
        return null;
    }

    @Override public <InjectorClass extends Injector> InjectorClass initInjector(
        Class<InjectorClass> injectorClass) throws HandlerInitException {
        return null;
    }
}

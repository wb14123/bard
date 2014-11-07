package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;

@BindTo(IntegerZeroInjector.class)
public class IntegerZeroInjectorDef extends Injector<IntegerZeroInjector> {
    @Before public void inject() {
        context.setInjectorVariable(0);
    }

    @Override public void generateDoc() {
    }
}

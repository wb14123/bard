package org.binwang.bard.core.defines;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

@BindTo(IntegerZeroInjector.class)
public class IntegerZeroInjectorDef extends Injector<IntegerZeroInjector> {
    @Before public void inject() {
        variable = 0;
    }

    @Override public void generateDoc() {
    }
}

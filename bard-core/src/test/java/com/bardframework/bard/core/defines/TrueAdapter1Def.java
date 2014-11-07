package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Match;

@BindTo(TrueAdapter1.class)
public class TrueAdapter1Def extends Adapter<TrueAdapter1> {
    @Match public boolean trueMatch() {
        return true;
    }

    @After public void addAfter() {
        context.getResponse().addHeader("after", "true");
    }

    @Override public void generateDoc() {
    }
}


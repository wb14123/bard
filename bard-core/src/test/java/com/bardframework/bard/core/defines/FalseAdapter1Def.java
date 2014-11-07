package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Match;

@BindTo(FalseAdapter1.class)
public class FalseAdapter1Def extends Adapter<FalseAdapter1> {
    @Match public boolean falseMatch() {
        return false;
    }

    @After public void addAfter() {
        context.getResponse().addHeader("after", "true");
    }

    @Override public void generateDoc() {
    }
}


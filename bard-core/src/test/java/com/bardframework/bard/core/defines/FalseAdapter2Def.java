package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

@BindTo(FalseAdapter2.class)
public class FalseAdapter2Def extends Adapter<FalseAdapter2> {
    @Match public boolean falseMatch() {
        return false;
    }

    @Override public void generateDoc() {
    }
}


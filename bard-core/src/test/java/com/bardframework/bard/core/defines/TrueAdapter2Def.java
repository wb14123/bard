package com.bardframework.bard.core.defines;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

@BindTo(TrueAdapter2.class)
public class TrueAdapter2Def extends Adapter<TrueAdapter2> {
    @Match public boolean trueMatch() {
        return true;
    }

    @Override public void generateDoc() {
    }
}


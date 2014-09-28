package org.binwang.bard.core.defines;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

@BindTo(TrueAdapter2.class)
public class TrueAdapter2Def extends Adapter<TrueAdapter2> {
    @Match public boolean trueMatch() {
        return true;
    }

    @Override public void generateDoc() {
    }
}


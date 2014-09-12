package org.binwang.bard.core.defines;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

@BindTo(TrueAdapter1.class)
public class TrueAdapter1Def extends Adapter<TrueAdapter1> {
    @Match public boolean trueMatch() {
        return true;
    }

    @Override public void generateDoc() {
    }
}

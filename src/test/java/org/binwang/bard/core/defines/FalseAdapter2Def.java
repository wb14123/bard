package org.binwang.bard.core.defines;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

@BindTo(FalseAdapter2.class)
public class FalseAdapter2Def extends Adapter<FalseAdapter2> {
    @Match public boolean falseMatch() {
        return false;
    }

    @Override public void generateDoc() {
    }
}


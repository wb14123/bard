package org.binwang.bard.core.defines;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

@BindTo(FalseAdapter1.class)
public class FalseAdapter1Def extends Adapter<FalseAdapter1> {
    @Match public boolean falseMatch() {
        return false;
    }

    @Override public void generateDoc() {
    }
}


package org.binwang.bard.core.defines;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Match;

@BindTo(FalseAdapter1.class)
public class FalseAdapter1Def extends Adapter<FalseAdapter1> {
    @Match public boolean falseMatch() {
        return false;
    }

    @After public void addAfter() {
        context.response.addHeader("after", "true");
    }

    @Override public void generateDoc() {
    }
}


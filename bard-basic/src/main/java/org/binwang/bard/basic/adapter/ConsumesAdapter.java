package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.Consumes;

@BindTo(Consumes.class)
public class ConsumesAdapter extends Adapter<Consumes> {
    @Match public boolean match() {
        for (String v : annotation.value()) {
            boolean result = context.request.getContentType().equals(v);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @Override public void generateDoc() {
        api.consumes = annotation.value();
    }
}

package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.Consumes;

@BindTo(Consumes.class)
public class ConsumesAdapter extends Adapter<Consumes> {
    @Match public boolean match() {
        for (String v : annotation.value()) {
            boolean result = context.getRequest().getContentType().equals(v);
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

package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.DELETE;

@BindTo(DELETE.class)
public class DeleteAdapter extends Adapter<DELETE> {
    @Match public boolean isGet() {
        return context.request.getMethod().equals("DELETE");
    }

    @Override public void generateDoc() {
        api.method = "DELETE";
    }
}

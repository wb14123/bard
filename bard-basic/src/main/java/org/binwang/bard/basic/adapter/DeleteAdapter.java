package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.DELETE;

@BindTo(DELETE.class)
public class DeleteAdapter extends Adapter<DELETE> {
    @Match public boolean isGet() {
        String method = context.request.getMethod();
        return method.equals("DELETE") || method.equals("HEAD") || method.equals("OPTIONS");
    }

    @Override public void generateDoc() {
        api.method = "DELETE";
    }
}

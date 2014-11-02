package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

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

package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.PUT;

@BindTo(PUT.class)
public class PutAdapter extends Adapter<PUT> {
    @Match public boolean isGet() {
        String method = context.getRequest().getMethod();
        return method.equals("PUT");
    }

    @Override public void generateDoc() {
        api.method = "PUT";
    }
}

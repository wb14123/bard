package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.GET;

@BindTo(GET.class)
public class GetAdapter extends Adapter<GET> {
    @Match public boolean isGet() {
        String method = context.request.getMethod();
        return method.equals("GET") || method.equals("HEAD") || method.equals("OPTIONS");
    }

    @Override public void generateDoc() {
        api.method = "GET";
    }
}

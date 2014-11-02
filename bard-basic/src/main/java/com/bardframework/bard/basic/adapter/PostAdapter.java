package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.POST;

@BindTo(POST.class)
public class PostAdapter extends Adapter<POST> {
    @Match public boolean isGet() {
        String method = context.request.getMethod();
        return method.equals("POST") || method.equals("HEAD") || method.equals("OPTIONS");
    }

    @Override public void generateDoc() {
        api.method = "POST";
    }
}

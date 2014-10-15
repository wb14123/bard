package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

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

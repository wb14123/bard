package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.GET;

@BindTo(GET.class)
public class GetAdapter extends Adapter<GET> {
    @Match public boolean isGet() {
        return context.request.getMethod().equals("GET");
    }

    @Override public void generateDoc() {
        api.method = "GET";
    }
}

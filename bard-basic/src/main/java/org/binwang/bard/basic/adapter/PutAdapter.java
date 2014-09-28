package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.PUT;

@BindTo(PUT.class)
public class PutAdapter extends Adapter<PUT> {
    @Match public boolean isGet() {
        return context.request.getMethod().equals("PUT");
    }

    @Override public void generateDoc() {
        api.method = "PUT";
    }
}

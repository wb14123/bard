package org.binwang.bard.basic.adapter;

import org.binwang.bard.core.Adapter;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.marker.Match;

import javax.ws.rs.POST;

@BindTo(POST.class)
public class PostAdapter extends Adapter<POST> {
    @Match public boolean isGet() {
        return context.request.getMethod().equals("POST");
    }

    @Override public void generateDoc() {
        api.method = "POST";
    }
}

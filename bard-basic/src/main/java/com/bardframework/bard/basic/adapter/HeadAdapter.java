package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.HEAD;

@BindTo(HEAD.class)
public class HeadAdapter extends Adapter<HEAD> {
    @Match
    public boolean match() {
        return context.getRequest().getMethod().equals("HEAD");
    }

    @Override public void generateDoc() {
        api.method = "HEAD";
    }
}

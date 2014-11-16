package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.core.Adapter;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.marker.Match;

import javax.ws.rs.OPTIONS;

@BindTo(OPTIONS.class)
public class OptionsAdapter extends Adapter<OPTIONS> {
    @Match
    public boolean match() {
        return context.getRequest().getMethod().equals("OPTIONS");
    }

    @Override public void generateDoc() {
        api.method = "OPTIONS";
    }
}

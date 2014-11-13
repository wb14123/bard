package com.bardframework.bard.example.auth;

import com.bardframework.bard.util.server.BardServer;

public class Main {
    public static void main(String[] args) {
        new BardServer(SimpleServlet.class).run();
    }
}

package com.bardframework.bard.example.crud;

import com.bardframework.bard.util.server.BardServer;

public class Main {
    public static void main(String[] args) {
        new BardServer(CrudServlet.class).run();
    }
}

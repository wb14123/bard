package org.apache.saltedpeanuts;

import com.bardframework.bard.util.server.BardServer;

public class Main {
    public static void main(String[] args) throws Exception {
        new BardServer(SimpleServlet.class).start();
    }
}

package com.bardframework.bard.example.crud;

import com.bardframework.bard.core.Util;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(CrudServlet.class, "/*");
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            Util.getLogger().error("Exception found in server: {}", e);
        }
    }
}

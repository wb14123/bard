package com.bardframework.bard.basic.server;

import com.bardframework.bard.core.Servlet;
import com.bardframework.bard.core.Util;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;

public class BardServer {
    public Server server;

    private Class<? extends Servlet> servletClass;

    public BardServer(Class<? extends Servlet> servletClass) {
        this.servletClass = servletClass;
    }

    public Server getServer(int port) {
        Server server = new Server(new QueuedThreadPool(
            Util.getConfig().getInt("bard.server.threads.max", 100),
            Util.getConfig().getInt("bard.server.threads.min", 10)));

        ServerConnector connector = new ServerConnector(server,
            Util.getConfig().getInt("bard.server.acceptors", -1),
            Util.getConfig().getInt("bard.server.selectors", -1));
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(servletClass, "/*");
        server.setHandler(handler);

        // Setup JMX
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addEventListener(mbContainer);
        server.addBean(mbContainer);

        // Add loggers MBean to server (will be picked up by MBeanContainer above)
        server.addBean(Log.getLog());
        return server;
    }

    public void start() throws Exception {
        Server server = getServer(Util.getConfig().getInt("bard.server.port", 8080));
        server.start();
    }

    public String startForTest() throws Exception {
        int port = 10000;
        while (!isPortAvailable(port)) {
            port++;
        }
        server = getServer(port);
        server.start();
        return "http://localhost:" + Integer.toString(port);
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private boolean isPortAvailable(int port) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            return true;
        } catch (IOException ignored) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Util.getLogger().error("Error while check port: {}", e);
                }
            }
        }
        return false;
    }
}

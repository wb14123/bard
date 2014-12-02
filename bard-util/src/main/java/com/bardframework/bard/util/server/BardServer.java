package com.bardframework.bard.util.server;

import com.bardframework.bard.core.Servlet;
import com.bardframework.bard.core.Util;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.lang.management.ManagementFactory;

public class BardServer {
    Server server;

    public BardServer(Class<? extends Servlet> servletClass) {
        server = new Server(new QueuedThreadPool(
            Util.getConfig().getInt("bard.server.threads.max", 100),
            Util.getConfig().getInt("bard.server.threads.min", 10)));

        ServerConnector connector = new ServerConnector(server,
            Util.getConfig().getInt("bard.server.acceptors", -1),
            Util.getConfig().getInt("bard.server.selectors", -1));
        connector.setPort(Util.getConfig().getInt("bard.server.port", 8080));
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

    }

    public void run() {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            Util.getLogger().error("Cannot start Jetty: {}", e);
        }
    }
}

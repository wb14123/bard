package com.bardframework.bard.util.db;


import com.bardframework.bard.core.Util;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBManager {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration cfg = new Configuration().configure();
            return cfg.buildSessionFactory(
                new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build());
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            Util.getLogger().error("Initial SessionFactory creation failed: {}", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

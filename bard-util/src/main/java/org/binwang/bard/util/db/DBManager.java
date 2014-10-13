package org.binwang.bard.util.db;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBManager {
    private static SessionFactory sessionFactory = null;

    public static void init(String[] pkgs, Class<?>[] classes) {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration cfg = new Configuration().configure();
            for (String pkg : pkgs) {
                cfg = cfg.addPackage(pkg);
            }
            for (Class<?> c : classes) {
                cfg = cfg.addAnnotatedClass(c);
            }
            sessionFactory = cfg.buildSessionFactory(
                new StandardServiceRegistryBuilder().build());
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

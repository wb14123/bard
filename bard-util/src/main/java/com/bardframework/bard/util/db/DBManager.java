package com.bardframework.bard.util.db;


import com.bardframework.bard.core.Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBManager {
    private static EntityManagerFactory emf = null;

    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(
                Util.getConfig().getString("bard.jpa.unit", "prod")
            );
        }
        return emf.createEntityManager();
    }
}

package com.bardframework.bard.util.db;


import com.bardframework.bard.core.Util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class DBManager {
    private static EntityManager em =
        Persistence.createEntityManagerFactory(
            Util.getConfig().getString("bard.jpa.unit", "prod")
        ).createEntityManager();

    public static EntityManager getEntityManager() {
        return em;
    }
}

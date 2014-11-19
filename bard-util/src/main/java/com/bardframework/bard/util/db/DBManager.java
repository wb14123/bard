package com.bardframework.bard.util.db;


import com.bardframework.bard.core.Util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class DBManager {
    private static EntityManager em = null;

    public static EntityManager getEntityManager() {
        if (em == null) {
            em = Persistence.createEntityManagerFactory(
                Util.getConfig().getString("bard.jpa.unit", "prod")
            ).createEntityManager();
        }
        return em;
    }
}

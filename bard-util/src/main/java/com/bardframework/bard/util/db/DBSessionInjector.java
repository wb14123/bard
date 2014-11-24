package com.bardframework.bard.util.db;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.util.db.marker.DBSession;

import javax.persistence.EntityManager;

@BindTo(DBSession.class)
public class DBSessionInjector extends Injector<DBSession> {
    private EntityManager em = DBManager.getEntityManager();

    @Before public void getSession() {
        context.setInjectorVariable(em);
        em.getTransaction().begin();

    }

    @After public void closeSession() {
        if (em == null) {
            return;
        }
        if (context.getException() != null && !context.isExceptionHandled()) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } else {
            em.getTransaction().commit();
        }
        em.close();
    }


    @Override public void generateDoc() {

    }
}

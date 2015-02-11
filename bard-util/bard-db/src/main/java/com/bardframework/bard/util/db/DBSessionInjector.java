package com.bardframework.bard.util.db;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.util.db.marker.DBSession;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.persistence.EntityManager;

@BindTo(DBSession.class)
public class DBSessionInjector extends Injector<DBSession> {
    private EntityManager em;

    @Before public void getSession()
        throws MalformedObjectNameException, NotCompliantMBeanException,
        InstanceAlreadyExistsException, MBeanRegistrationException {
        em = DBManager.getEntityManager();
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

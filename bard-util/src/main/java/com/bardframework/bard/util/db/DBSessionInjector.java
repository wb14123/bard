package com.bardframework.bard.util.db;

import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.After;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.util.db.marker.DBSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

@BindTo(DBSession.class)
public class DBSessionInjector extends Injector<DBSession> {
    private Transaction tx;

    @Before public void getSession() {
        Session session = DBManager.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        context.setInjectorVariable(session);
    }

    @After public void closeSession() {
        if (tx == null) {
            return;
        }
        if (context.getException() != null && !context.isExceptionHandled()) {
            tx.rollback();
        } else {
            tx.commit();
        }
    }


    @Override public void generateDoc() {

    }
}

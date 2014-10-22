package org.binwang.bard.util.db;

import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.After;
import org.binwang.bard.core.marker.Before;
import org.binwang.bard.util.db.marker.DBSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

@BindTo(DBSession.class)
public class DBSessionInjector extends Injector<DBSession> {
    private Transaction tx;

    @Before public void getSession() {
        Session session = DBManager.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        injectorVariable = session;
    }

    @After public void closeSession() {
        if (tx == null) {
            return;
        }
        if (context.exception != null && !context.exceptionHandled) {
            tx.rollback();
        } else {
            tx.commit();
        }
    }


    @Override public void generateDoc() {

    }
}

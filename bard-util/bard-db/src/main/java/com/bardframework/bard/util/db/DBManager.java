package com.bardframework.bard.util.db;


import com.bardframework.bard.core.Util;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.stat.Statistics;

import javax.management.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DBManager {
    private static EntityManagerFactory emf = null;

    public static EntityManager getEntityManager()
        throws MalformedObjectNameException, NotCompliantMBeanException,
        InstanceAlreadyExistsException, MBeanRegistrationException {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(
                Util.getConfig().getString("bard.jpa.unit", "prod")
            );
            final SessionFactory sessionFactory =
                ((HibernateEntityManagerFactory) emf).getSessionFactory();

            ObjectName statsName = new ObjectName("org.hibernate:type=statistics");
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

            final Statistics statistics = sessionFactory.getStatistics();
            statistics.setStatisticsEnabled(true);
            Object statisticsMBean = Proxy.newProxyInstance(DBManager.class.getClassLoader(),
                new Class<?>[] {StatisticsMXBean.class}, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {
                        return method.invoke(statistics, args);
                    }
                });

            mbeanServer.registerMBean(statisticsMBean, statsName);

        }
        return emf.createEntityManager();
    }

    @MXBean
    public static interface StatisticsMXBean extends Statistics {
    }
}

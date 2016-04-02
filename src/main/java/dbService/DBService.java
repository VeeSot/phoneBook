package dbService;


import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class DBService {
    private Class clazz;


    private final SessionFactory sessionFactory;


    public DBService(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
        String className = configuration.getClassMappings().next().getClassName();
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    @SuppressWarnings("unchecked")
    public <T> T get(long id) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            T dataSet = (T) session.get(clazz, id);
            session.close();
            return dataSet;
        } catch (HibernateException e) {
            try {
                throw new DBException(e);
            } catch (DBException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public <T> long save(T dataset) {
        Session session = sessionFactory.openSession();
        try {
            Transaction tx = session.beginTransaction();
            long id = (long) session.save(dataset);
            tx.commit();
            return id;
        } catch (ConstraintViolationException e) {//Случай с дубликатами записи
            return 0;
        } finally {
            session.close();
        }
    }


    public <T> void update(T dataset) {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.update(dataset);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            try {
                throw new DBException(e);
            } catch (DBException e1) {
                e1.printStackTrace();
            }
        }

    }


    public <T> T delete(T dataset) {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.delete(dataset);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            try {
                throw new DBException(e);
            } catch (DBException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public List<?> getAll() {
        Session session = sessionFactory.openSession();
        List result = session.createCriteria(clazz).list();
        session.close();
        return result;
    }

    public List<?> findByName(String people) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(clazz);
        try {
            return criteria.add(Restrictions.ilike("people", "%" + people + "%")).list();
        } catch (NullPointerException e) {
            return null;
        } finally {
            session.close();
        }

    }

    public class DBException extends Exception {
        public DBException(Throwable throwable) {
            super(throwable);
        }
    }

}

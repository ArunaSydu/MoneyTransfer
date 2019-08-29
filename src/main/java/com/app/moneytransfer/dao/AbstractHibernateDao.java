package com.app.moneytransfer.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;

public abstract class AbstractHibernateDao implements IDataSourceDao {

    protected Logger log = Logger.getLogger(getClass());

    protected SessionFactory sessionFactory;

    protected Class<? extends Serializable> entityClass;

    public AbstractHibernateDao(SessionFactory sessionFactory) {
       this.sessionFactory=sessionFactory;
    }

    @SuppressWarnings("unchecked")    
    public synchronized <T extends Serializable> T findById(Serializable id, Class<T> entityClass) {

        Transaction trans = getCurrentSession().beginTransaction();
        T entity = getCurrentSession().get(entityClass, id);
        trans.commit();

        return entity;
    }

    public <T extends Serializable> void save(final T entity) {
        log.debug("Save: persisting instance " + entity.toString());
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            getCurrentSession().persist(entity);
            trans.commit();
        } catch (RuntimeException re) {
            log.error("Persist failed for " + entity.toString(), re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T update(final T entity) {
        log.debug("update: instance " + entity.toString());

        T mergedEntity;
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            mergedEntity = (T) getCurrentSession().merge(entity);
            trans.commit();
        } catch (RuntimeException re) {
            log.error("Update: failed for " + entity.toString(), re);
            throw re;
        }

        return mergedEntity;
    }

    public <T extends Serializable> void delete(final T entity) {
        log.debug("Delete: instance " + entity.toString());
        try {
            Transaction trans = getCurrentSession().beginTransaction();
            getCurrentSession().delete(entity);
            trans.commit();
        } catch (RuntimeException re) {
            log.error("Remove: failed for " + entity.toString(), re);
            throw re;
        }
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}

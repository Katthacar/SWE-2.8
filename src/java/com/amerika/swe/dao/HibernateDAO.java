package com.amerika.swe.dao;

import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class HibernateDAO<T> {
  
    public Object save(T entities) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();      
            sessions.merge(entities);
            sessions.flush();
            sessions.clear();            
            transactions.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transactions != null) {
                transactions.rollback();
            }
            return e;
        }
    }

    public Object update(T entity) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();            
            sessions.update(entity);
            sessions.flush();
            sessions.clear();            
            transactions.commit();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (transactions != null) {
                transactions.rollback();
            }
            return e;
        }
    }

    public Object delete(T entity) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();
            sessions.delete(entity);
            sessions.flush();
            sessions.clear();
            transactions.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transactions != null) {
                transactions.rollback();
            }
            return e;
        }
    }

    public abstract List<T> findAll(Serializable  arg);


    public <T> T load(Class<T> entityClass, Serializable id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            return (T) sessions.load(entityClass, id);
        } catch (HibernateException e) {
            return null;
        }
    }

}
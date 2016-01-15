/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweTipoDetret;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweTipoDetretDAO extends HibernateDAO<SweTipoDetret> {

    private static final String HQL_LISTAR = "FROM SweTipoDetret s";
    private static final String HQL_BUSCAR = "FROM SweTipoDetret s WHERE s.nombre =:nombreFuncion";
    
    @Override
    public List<SweTipoDetret> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTAR);      
            return q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
     public SweTipoDetret buscar(String nombre) {
       Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR);    
            q.setParameter("nombreFuncion", nombre);            
            return (SweTipoDetret)q.uniqueResult(); 
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweParam;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweParamDAO extends HibernateDAO<SweParam>{
    
    private static final String HQL_BUSCAR_NOMBRE = "SELECT p.valor FROM SweParam p WHERE p.nombre=:nombre";
    private static final String HQL_LISTAR = "FROM SweParam p ORDER BY p.nombre";

    @Override
    public List<SweParam> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            List<SweParam> lista = (List<SweParam>) sessions.createQuery(HQL_LISTAR).list();   
            return lista;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    public BigDecimal buscar_parametro(String nombre){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR_NOMBRE);
            q.setParameter("nombre", nombre);
            return (BigDecimal)q.uniqueResult();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
}

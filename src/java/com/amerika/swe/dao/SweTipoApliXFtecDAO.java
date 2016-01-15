/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweTipoApliXFtec;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweTipoApliXFtecDAO extends HibernateDAO<SweTipoApliXFtec>{

    
    private static final String HQL_LISTAR = "FROM SweTipoApliXFtec tf ORDER BY tf.id.codigoTpoaplicacion, tf.id.codigoFacttecnico";
    private static final String HQL_LISTARxAPLICION = "FROM SweTipoApliXFtec tf WHERE tf.id.codigoTpoaplicacion=:idAplicacion ORDER BY tf.id.codigoTpoaplicacion, tf.id.codigoFacttecnico";
    
    @Override
    public List<SweTipoApliXFtec> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTAR);
            List<SweTipoApliXFtec> lista = q.list();
            for (int i = 0; i < lista.size(); i++) {
                Hibernate.initialize(lista.get(i).getSweCataByCodigoFacttecnico());
                Hibernate.initialize(lista.get(i).getSweCataByCodigoTpoaplicacion()); 
            }
            return lista;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
 
    public List<SweTipoApliXFtec> buscar_porTipoAplicacion(Integer idAplicacion){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTARxAPLICION);
            q.setParameter("idAplicacion", idAplicacion);
            List<SweTipoApliXFtec> lista = q.list();
            for (int i = 0; i < lista.size(); i++) {
                Hibernate.initialize(lista.get(i).getSweCataByCodigoFacttecnico());
                Hibernate.initialize(lista.get(i).getSweCataByCodigoTpoaplicacion()); 
            }
            return lista;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
}

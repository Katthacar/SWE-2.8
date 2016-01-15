/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweEtapaXsolDAO extends HibernateDAO<SweEtapaXsol> {

    private static final String HQL_BUSCAR_ETAPAXSOLI = " FROM SweEtapaXsol e WHERE e.sweSoli.idEstimacion=:idEstimacion";
    
    @Override
    public List<SweEtapaXsol> findAll(Serializable idEstimacion) {
       Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
       try {            
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR_ETAPAXSOLI);
            q.setParameter("idEstimacion", idEstimacion);
            List<SweEtapaXsol> l = q.list();
            for (int i = 0; i < l.size(); i++) {
               Hibernate.initialize(l.get(i).getSweCata()); 
           }
            return l;
        } catch (HibernateException e) {
            return new ArrayList<SweEtapaXsol>();
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    
}

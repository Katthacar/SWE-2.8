/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweCompXsoli;
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
public class SweCompXsoliDAO extends HibernateDAO<SweCompXsoli>{

    private static final String HQL_BUSCAR_COMPXSOLI = "FROM SweCompXsoli c WHERE c.sweSoli.idEstimacion =:idEstimacion ORDER BY c.sweCataByIdTpocomponente.idCatalogo ASC";
   
    @Override
    public List<SweCompXsoli> findAll(Serializable idEstimacion) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {            
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR_COMPXSOLI);
            q.setParameter("idEstimacion", idEstimacion);
            List<SweCompXsoli> l = q.list();
            for (int i = 0; i < l.size(); i++) {
                Hibernate.initialize(l.get(i).getSweCataByIdComplejidad());
                Hibernate.initialize(l.get(i).getSweCataByIdTpocomponente()); 
            }
            return l;
        } catch (HibernateException e) {
            return new ArrayList<SweCompXsoli>();
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }

}

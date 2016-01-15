/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweCataDetret;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author estudiante.proyectos
 */
public class SweCataDetretDAO extends HibernateDAO<SweCataDetret>{

    private static final String HQL_LISTAR = "FROM SweCataDetret c WHERE c.sweCata.idCatalogo=:idCatalogo AND c.sweTipoDetret.codigoTipoDetret=:idDetRet ORDER BY c.rangoInicial, c.rangoFinal";

        
    @Override
    public List<SweCataDetret> findAll(Serializable arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<SweCataDetret> listar(Integer idComponente, Integer idDetRet){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTAR);
            q.setParameter("idCatalogo", idComponente);
            q.setParameter("idDetRet", idDetRet);
            List<SweCataDetret> lista = q.list();
            for (int i = 0; i < lista.size(); i++) {
                Hibernate.initialize(lista.get(i).getSweCata());
                Hibernate.initialize(lista.get(i).getSweTipoDetret());
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
    
    public List<SweCataDetret> listar_PorDetRet(){
        return null;
    }
    
    
    public Object agregar(List<SweCataDetret> catalogos){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();
            for (int i = 0; i < catalogos.size(); i++) {                           
                sessions.saveOrUpdate(catalogos.get(i)); 
            }
            sessions.flush();
            sessions.clear();            
            transactions.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transactions != null) {
                transactions.rollback();
            }
            if(sessions.isOpen()){
                sessions.close();
            }        
            return e;
        }
    }
    
    public Object editar(SweCataDetret... catalogos){
        return null;
    }
}

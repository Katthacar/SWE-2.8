/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCompXPfuncion;
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
public class SweCompXPfuncionDAO extends HibernateDAO<SweCompXPfuncion> {

     
    private static final String HQL_COMPLEJIDADxPUNTOFUNCION = "SELECT cpf.sweCata FROM  SweCompXPfuncion cpf  " +
                                                                "WHERE cpf.sweCataDetretByIdDet = (FROM SweCataDetret cdr WHERE cdr.sweCata.idCatalogo =:idComponente AND cdr.sweTipoDetret.codigoTipoDetret =:idDet AND cdr.rangoInicial <=:valDet AND (cdr.rangoFinal IS NULL OR cdr.rangoFinal >=:valDet ))  " +
                                                                "AND cpf.sweCataDetretByIdRetftr = (FROM SweCataDetret cdr WHERE cdr.sweCata.idCatalogo =:idComponente AND cdr.sweTipoDetret.codigoTipoDetret =:idRetFtr AND cdr.rangoInicial <=:valRetFtr AND (cdr.rangoFinal IS NULL OR cdr.rangoFinal >=:valRetFtr ))";
   

    public static final String HQL_LISTAR = "FROM SweCompXPfuncion CPF WHERE CPF.sweCataDetretByIdRetftr.sweCata.idCatalogo =:idComponente OR CPF.sweCataDetretByIdDet.sweCata.idCatalogo =:idComponente";
    
    @Override
    public List<SweCompXPfuncion> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
         try{
             sessions.beginTransaction();
             Query q = sessions.createQuery(HQL_LISTAR);
             q.setParameter("idComponente", arg);
             List<SweCompXPfuncion> lista = q.list();
             for (int i = 0; i < lista.size(); i++) {
                 Hibernate.initialize(lista.get(i).getSweCata());
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdDet());
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdRetftr());
                 
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdDet().getSweCata());  
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdRetftr().getSweCata()); 
                 
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdDet().getSweTipoDetret()); 
                 Hibernate.initialize(lista.get(i).getSweCataDetretByIdRetftr().getSweTipoDetret());
             }
             return lista;
         }catch(HibernateException e){
             return null;
         }finally{
             if(sessions.isOpen()){
                 sessions.close();
             }
         }
    }

    public Object guardar(List<SweCompXPfuncion> list_compXpFun) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();
            for (int i = 0; i < list_compXpFun.size(); i++){
                if ( (i>0) && (i % 20 == 0) ) {
                    sessions.flush();
                    sessions.clear();
                }
                sessions.save(list_compXpFun.get(i));
            }            
            sessions.flush();
            sessions.clear();
            transactions.commit();
            return true;
        } catch (HibernateException e) {
            if (transactions != null) {
                transactions.rollback();
            }
            
            if(sessions.isOpen()){
                sessions.close();
            }        
            return e;
        }
    }
    
    
    /**
     * Este metodo consulta la complejidad asignada a un punto de función
     * conociendo el componente, los id del tipo de función y los valores
     * comprendidos en los intervalos (valDet, valRetFtr) 
     */
    public SweCata buscar_complejidad(Integer idComponente, Integer idDet, Integer idRetFtr,Integer valDet, Integer valRetFtr){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_COMPLEJIDADxPUNTOFUNCION);
            q.setParameter("idComponente", idComponente);
            q.setParameter("idDet", idDet);
            q.setParameter("idRetFtr", idRetFtr);
            q.setParameter("valDet", valDet);
            q.setParameter("valRetFtr", valRetFtr);
            return (SweCata)q.uniqueResult();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    
    }
}

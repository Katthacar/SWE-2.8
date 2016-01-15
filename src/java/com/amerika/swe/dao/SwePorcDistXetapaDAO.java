/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SwePorcDistXetapa;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.Calendar;
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
public class SwePorcDistXetapaDAO extends HibernateDAO<SwePorcDistXetapa> implements Historico<SwePorcDistXetapa>{
 
    private static final String HQL_VIGENTES  = "FROM SwePorcDistXetapa p WHERE p.vigenciaFinal = NULL ORDER BY p.sweCata.idCatalogo ASC";
    private static final String HQL_ULTIMO    = "FROM SwePorcDistXetapa p WHERE p.sweCata.idCatalogo =:idEtapa AND p.vigenciaFinal = NULL";
    private static final String HQL_HISTORICO = "FROM SwePorcDistXetapa p WHERE p.sweCata.idCatalogo =:idEtapa AND p.vigenciaFinal != NULL ORDER BY p.vigenciaFinal DESC";
    
    
    /* ESTE METODO MODIFICA LOS PORCENTAGES DE DISTRIBUCIÓN POR ETAPAS VIGENTES,
     * ES DECIR SE CAMBIA SU FECHA DE VIGENCIA FINAL Y SE AGREGAN LOS NUEVOS VALORES
     * QUE SERÁN VIGENTES.
     * 
     * NOTA: Se espera completar con exito todo el metodo para confirmar la transacción,
     * es por esta razon que no se llaman los metodos save y update de la clase padre
     */
    public Object guardar(SwePorcDistXetapa... porcDistxEtapa){ 
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;       
        try{
            transactions = sessions.beginTransaction();
            for (int i = 0; i < porcDistxEtapa.length; i++) {
                Query q =  sessions.createQuery(HQL_ULTIMO);
                q.setParameter("idEtapa", porcDistxEtapa[i].getSweCata().getIdCatalogo());
                SwePorcDistXetapa ultimo = (SwePorcDistXetapa)q.uniqueResult();
                
                if(ultimo instanceof SwePorcDistXetapa ){
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, -1);
                    ultimo.setVigenciaFinal(c.getTime()); 
                    sessions.update(ultimo);                    
                }
                sessions.save(porcDistxEtapa[i]); 
            }
            sessions.flush();
            sessions.clear();            
            transactions.commit();
            return true;                
        }catch(Exception e){
            if (transactions != null) {
                transactions.rollback();
            }
            return e;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    @Override
    public List<SwePorcDistXetapa> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_VIGENTES);            
            List<SwePorcDistXetapa> lista = q.list();
            for (int i = 0; i < lista.size(); i++) {
                Hibernate.initialize(lista.get(i).getSweCata()); 
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

    @Override
    public SwePorcDistXetapa consultar_ultimo(Integer... id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
         try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_ULTIMO);
            q.setParameter("idEtapa", id[0]);
            return (SwePorcDistXetapa)q.uniqueResult();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }

    @Override
    public List<SwePorcDistXetapa> consultar_historial(Integer... id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_HISTORICO);  
            q.setParameter("idEtapa", id[0]);
            return (List<SwePorcDistXetapa>)q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
}

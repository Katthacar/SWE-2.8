/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweTcompXComp;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweTcompXCompDAO extends HibernateDAO<SweTcompXComp> implements Historico<SweTcompXComp> {

    private static final String HQL_VIGENTES  = "FROM SweTcompXComp c WHERE c.sweCataByIdTpocomplejidad.idCatalogo =:idComplejidad AND c.vigenciaFinal = NULL ORDER BY c.sweCataByIdTpocomplejidad.idCatalogo ASC, c.sweCataByIdTpocomponente.idCatalogo ASC";
    private static final String HQL_ULTIMO    = "FROM SweTcompXComp c WHERE c.sweCataByIdTpocomplejidad.idCatalogo =:idComplejidad AND c.sweCataByIdTpocomponente.idCatalogo =:idComponente AND c.vigenciaFinal = NULL";
    private static final String HQL_HISTORICO = "FROM SweTcompXComp c WHERE c.sweCataByIdTpocomplejidad.idCatalogo =:idComplejidad AND c.sweCataByIdTpocomponente.idCatalogo =:idComponente AND c.vigenciaFinal != NULL ORDER BY c.vigenciaFinal DESC";
    private static final String HQL_PESOS_VIGENTES = "SELECT c.sweCataByIdTpocomponente.idCatalogo, c.sweCataByIdTpocomplejidad.idCatalogo, c.peso FROM SweTcompXComp c WHERE  c.vigenciaFinal = NULL ORDER BY c.sweCataByIdTpocomponente.idCatalogo ASC, c.sweCataByIdTpocomplejidad.idCatalogo ASC";
    
    
    @Override
    public List<SweTcompXComp> findAll(Serializable idComplejidad) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            sessions.clear();
            Query q = sessions.createQuery(HQL_VIGENTES);
            q.setParameter("idComplejidad", idComplejidad);
            List<SweTcompXComp> lista = q.list();
            for (int i = 0; i < lista.size(); i++) {
                Hibernate.initialize(lista.get(i).getSweCataByIdTpocomplejidad());
                Hibernate.initialize(lista.get(i).getSweCataByIdTpocomponente()); 
                
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
    
    /**
     * Este metodo lista los pesos de los componentes por complejidad
     * y retorna un Map donde la primera llave es el id del componente
     * y la segunda llave es el id de la complejidad y el último valor
     * es el peso.     * 
     */
    public Map<Integer,Map<Integer,Integer>> listar_pesos(){
        
        Map<Integer,Map<Integer,Integer>> mapaComponente = new HashMap<Integer, Map<Integer, Integer>>();
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            sessions.clear();
            Query q = sessions.createQuery(HQL_PESOS_VIGENTES);
           
            Iterator ite = q.iterate();
            while (ite.hasNext()) {
                 Object[] tupla =  (Object[]) ite.next();
                 
                 if(mapaComponente.containsKey((Integer)tupla[0])){
                    Map<Integer,Integer> mapaComplejidad = mapaComponente.get((Integer)tupla[0]);                  
                    mapaComplejidad.put((Integer)tupla[1], (Integer) tupla[2]);
                 }else{
                     Map<Integer,Integer> mapaComplejidad = new HashMap<Integer, Integer>();
                     mapaComplejidad.put((Integer)tupla[1], (Integer) tupla[2]);
                     mapaComponente.put((Integer)tupla[0], mapaComplejidad);
                 }
            }
            return mapaComponente;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    
    public Object guardar(SweTcompXComp sweTcompXComp){
        
        SweTcompXComp ultimo = this.consultar_ultimo(sweTcompXComp.getSweCataByIdTpocomplejidad().getIdCatalogo(), 
                                sweTcompXComp.getSweCataByIdTpocomponente().getIdCatalogo());
        
        if(ultimo instanceof SweTcompXComp ){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            ultimo.setVigenciaFinal(c.getTime()); 
            this.update(ultimo);            
        }
        sweTcompXComp.getSweCataByIdTpocomplejidad().setNombre(this.load(sweTcompXComp.getSweCataByIdTpocomplejidad().getClass(), sweTcompXComp.getSweCataByIdTpocomplejidad().getIdCatalogo()).getNombre());
        sweTcompXComp.getSweCataByIdTpocomponente().setNombre(this.load(sweTcompXComp.getSweCataByIdTpocomponente().getClass(), sweTcompXComp.getSweCataByIdTpocomponente().getIdCatalogo()).getNombre()); 
        return this.save(sweTcompXComp);
    
    }
    
    @Override
   public SweTcompXComp consultar_ultimo(Integer...idTipo){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_ULTIMO);
            q.setParameter("idComplejidad", idTipo[0]);
            q.setParameter("idComponente", idTipo[1]);
            return (SweTcompXComp)q.uniqueResult();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }

    @Override
    public List<SweTcompXComp> consultar_historial(Integer... id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_HISTORICO);  
            q.setParameter("idComplejidad", id[0]);
            q.setParameter("idComponente", id[1]);
            return (List<SweTcompXComp>)q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }

    
}

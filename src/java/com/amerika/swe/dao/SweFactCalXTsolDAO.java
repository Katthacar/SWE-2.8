/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweFactCalXTsol;
import com.amerika.swe.util.DataBaseConnector;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweFactCalXTsolDAO extends HibernateDAO<SweFactCalXTsol> implements Historico<SweFactCalXTsol>{
    
    private static final String HQL_ULTIMO    = "FROM SweFactCalXTsol f WHERE f.sweCata.idCatalogo=:idTipoCatalogo AND  f.vigenciaFinal = NULL";
    private static final String HQL_VIGENTES  = "FROM SweFactCalXTsol c WHERE c.vigenciaFinal = NULL ORDER BY c.sweCata.idCatalogo ASC";
    private static final String HQL_HISTORICO = "FROM SweFactCalXTsol c WHERE c.sweCata.idCatalogo =:idTipoCata AND c.vigenciaFinal != NULL ORDER BY c.vigenciaFinal DESC";
    
    public SweFactCalXTsolDAO(){
    
    }

    @Override
    public List<SweFactCalXTsol> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_VIGENTES);
            List<SweFactCalXTsol> lista = q.list();
            for (int i = 0; i < lista.size(); i++)
                Hibernate.initialize(lista.get(i).getSweCata());
            return lista;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
                
    }
    
    
    public Object guardar(SweFactCalXTsol factor){
        
        SweFactCalXTsol ultimoFC = consultar_ultimo(factor.getSweCata().getIdCatalogo());
        
        if(ultimoFC instanceof SweFactCalXTsol ){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            ultimoFC.setVigenciaFinal(c.getTime());  
            this.update(ultimoFC);            
        }
        factor.getSweCata().setNombre(this.load(factor.getSweCata().getClass(), factor.getSweCata().getIdCatalogo()).getNombre());
        return this.save(factor);
    }
    
    @Override
    public SweFactCalXTsol consultar_ultimo(Integer... idTipoSolicitud){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_ULTIMO);
            q.setParameter("idTipoCatalogo", idTipoSolicitud[0]);
            SweFactCalXTsol facCal = (SweFactCalXTsol)q.uniqueResult();
            if(facCal!=null){
                Hibernate.initialize(facCal.getSweCata()); 
            }            
            return facCal;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                try{
                    sessions.close();
                }catch(Exception e){}
            }
        }
    }
    
    @Override
     public List<SweFactCalXTsol> consultar_historial(Integer...idTipoSolicitud){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_HISTORICO);  
            q.setParameter("idTipoCata", idTipoSolicitud[0]);
            return (List<SweFactCalXTsol>)q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    
    }
     
     /*Método que llama al procedimiento P_CalculaFactorCal*/
    public String calcFactCalibracion(){
        /*String que almacenará el estado de la consulta 
        obtenido del procedimiento almacenado*/
        String mensaje = "";
        DataBaseConnector connector = new DataBaseConnector();
        CallableStatement callableStatement = null;
        
        try {
            connector.Connection();
            callableStatement = connector.getConexion().prepareCall("call pk_sweestimacion.P_CalcularFactorCal(?)");
            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
            callableStatement.execute();
            mensaje = callableStatement.getString(1);
            return mensaje;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }finally{
            try {
                if(callableStatement != null) callableStatement.close();
                if(connector.getConexion() != null) connector.closeConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
        }
    }
     
}
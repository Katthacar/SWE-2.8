/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweCata;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author estudiante.proyectos
 */
public class SweCataDAO extends HibernateDAO<SweCata>{
    
    private static final String HQL_LISTAR_POR = "FROM SweCata c WHERE c.sweTipoCata.codigoTipo = (SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre=:nombre) AND (c.estado IS NULL OR c.estado='A')";
    private static final String HQL_NOMBRES_CATALOGOS = "SELECT c.nombre FROM SweCata c WHERE c.sweTipoCata.nombre =:nombreTipo";
    private static final String HQL_LISTAR = "FROM SweCata c WHERE c.sweTipoCata.codigoTipo =:codigoTipoCataParam AND (c.estado IS NULL OR c.estado='A')";
    private static final String SQL_EDITAR = "UPDATE SWE_CATA C SET C.NOMBRE=:nombre, C.DESCRIPCION=:descripcion, C.ESTADO=:estado, C.VALOR=:valor WHERE C.ID_CATALOGO=:id";
    
    public SweCataDAO(){
    
    }

    @Override
    public List<SweCata> findAll(Serializable idTipoCata) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {     
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTAR);
            q.setParameter("codigoTipoCataParam", idTipoCata);
            return q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    
    
    /**
     * Este metodo lista los catalogos de acuerdo al nombre del
     * tipo de catalogo recibido como parametro
     */
    public List<SweCata> listarPOR(String arg){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_LISTAR_POR);
            q.setParameter("nombre", arg);
            return (List<SweCata>)q.list();
        }catch(HibernateException e){
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    
    public List<String> nombresCatalogos(String arg){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_NOMBRES_CATALOGOS);
            q.setParameter("nombreTipo", arg);
            return (List<String>)q.list();
        }catch(HibernateException e){
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    
    }
    
    public Object modificarCatalogo(SweCata catalogo){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transanction = sessions.beginTransaction();
        
        try{ 
             SQLQuery q = sessions.createSQLQuery(SQL_EDITAR);
             q.setParameter("nombre", catalogo.getNombre());
             q.setParameter("descripcion", catalogo.getDescripcion());
             q.setParameter("estado", catalogo.getEstado());
             q.setParameter("valor", catalogo.getValor());
             q.setParameter("id", catalogo.getIdCatalogo());                
             q.executeUpdate();
             sessions.flush();             
             sessions.clear();
             transanction.commit();
             return true;
        }catch(Exception e){
            if(transanction!=null){
                transanction.rollback();
            }
            if(sessions.isOpen()){
                sessions.close();
            }        
            return e;
        }
    }
//    public Object modificarCatalogo(SweCata catalogo){
//        
//        PreparedStatement pStatement = null;
//        DataBaseConnector connector = new DataBaseConnector();
//        try{
//            connector.Connection();
//            connector.getConexion().setAutoCommit(false);
//            pStatement = connector.getConexion().prepareStatement(SQL_EDITAR);
//            pStatement.setString(1, catalogo.getNombre());
//            pStatement.setString(2, catalogo.getDescripcion());
//            pStatement.setString(3, catalogo.getEstado());
//            pStatement.setBigDecimal(4, catalogo.getValor());
//            pStatement.setInt(5, catalogo.getIdCatalogo());
//            
//            pStatement.executeUpdate();
//            
//            connector.getConexion().commit();
//            connector.getConexion().setAutoCommit(true); 
//            return true;
//        }catch(SQLException e){
//            return e;
//        }finally{
//            try {
//                if (pStatement != null) {
//                    pStatement.close();
//                }
//                if (connector.getConexion() != null) {
//                    connector.closeConnection();
//                    connector.nullConector();
//                }
//            } catch (SQLException e){}
//        }
//    }
}

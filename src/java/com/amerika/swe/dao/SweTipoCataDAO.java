/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweTipoCata;
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
public class SweTipoCataDAO extends HibernateDAO<SweTipoCata> {

    private static final String HQL_NUMERO_CATA = "SELECT COUNT(*) FROM SweCata c WHERE c.sweTipoCata.codigoTipo =:idTipoCatalogo AND (c.estado IS NULL OR c.estado='A')";
    private static final String HQL_TIPO_CATA_ACTIVOS = "FROM SweTipoCata t WHERE t.estado!='I' ORDER BY t.codigoTipo ASC";
    private static final String SQL_EDITAR = "UPDATE SWE_TIPO_CATA TC SET TC.NOMBRE=:nombre, TC.DESCRIPCION=:descripcion, TC.MOSTRAR_ESTADO=:mostrar_stado, TC.MOSTRAR_VALOR=:mostrar_valor, TC.ESTADO=:estado WHERE TC.CODIGO_TIPO=:id";
    
    @Override
    public List<SweTipoCata> findAll(Serializable arg) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
         try {
            sessions.beginTransaction();
            List<SweTipoCata> lista = (List<SweTipoCata>) sessions.createQuery(HQL_TIPO_CATA_ACTIVOS).list();            
            return lista;
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    public Long numeroCatalogos(Integer idTipoCatalogo){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_NUMERO_CATA);
            q.setParameter("idTipoCatalogo", idTipoCatalogo);
            return (Long)q.uniqueResult();
        }catch(Exception e){
            return (long)0;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    
    public Object modificarCatalogo(SweTipoCata tipocatalogo){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transanction = sessions.beginTransaction();
      
        try{ 
             SQLQuery q = sessions.createSQLQuery(SQL_EDITAR);
             q.setParameter("nombre", tipocatalogo.getNombre());
             q.setParameter("descripcion", tipocatalogo.getDescripcion());
             q.setParameter("mostrar_stado", tipocatalogo.getMostrarEstado());
             q.setParameter("mostrar_valor", tipocatalogo.getMostrarValor());
             q.setParameter("estado", tipocatalogo.getEstado());
             q.setParameter("id", tipocatalogo.getCodigoTipo());
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
    
    
//    public Object modificarCatalogo(SweTipoCata catalogo){
//        
//        PreparedStatement pStatement = null;
//        DataBaseConnector connector = new DataBaseConnector();
//        try{
//            connector.Connection();
//            connector.getConexion().setAutoCommit(false);
//            pStatement = connector.getConexion().prepareStatement(SQL_EDITAR);
//            pStatement.setString(1, catalogo.getNombre());
//            pStatement.setString(2, catalogo.getDescripcion());
//            pStatement.setString(3, catalogo.getMostrarEstado());
//            pStatement.setString(4, catalogo.getMostrarValor());
//            pStatement.setString(5, catalogo.getEstado());
//            pStatement.setInt(6, catalogo.getCodigoTipo()); 
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

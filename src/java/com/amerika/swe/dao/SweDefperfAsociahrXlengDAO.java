/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;


import com.amerika.swe.model.SweDefperfAsociahrXleng;
import com.amerika.swe.model.util.TablaPefilxLenguaje;
import com.amerika.swe.util.DataBaseConnector;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author estudiante.proyectos
 */
public class SweDefperfAsociahrXlengDAO extends HibernateDAO<SweDefperfAsociahrXleng> implements Historico<SweDefperfAsociahrXleng>{

    private static final String HQL_ULTIMO    = "FROM SweDefperfAsociahrXleng c WHERE c.sweCataByIdLenguaje.idCatalogo =:idLenguaje AND c.sweCataByIdPerfil.idCatalogo =:idPerfil AND c.vigenciaFinal = NULL";
    private static final String HQL_VIGENTES  = "FROM SweDefperfAsociahrXleng c WHERE c.sweCataByIdLenguaje.idCatalogo =:idLenguaje AND c.vigenciaFinal = NULL ORDER BY c.sweCataByIdLenguaje.idCatalogo ASC";
    private static final String HQL_HISTORICO = "FROM SweDefperfAsociahrXleng c WHERE c.sweCataByIdLenguaje.idCatalogo =:idLenguaje AND c.sweCataByIdPerfil.idCatalogo =:idPerfil AND c.vigenciaFinal != NULL ORDER BY c.vigenciaFinal DESC";
        
    private static final String SQL_VIGENTES = "SELECT LENGUAJE, SUM(SENIOR) SENIOR, SUM(SEMI_SENIOR) SEMI_SENIOR, SUM(JUNIOR) JUNIOR " +
                                                "FROM(" +
                                                "     SELECT NOMBRE LENGUAJE, DECODE(ID_PERFIL, 75, HORAS, 0) SENIOR, " +
                                                "            DECODE(ID_PERFIL, 76, HORAS, 0) SEMI_SENIOR, DECODE(ID_PERFIL, 77, HORAS, 0) JUNIOR " +
                                                "            FROM SWE_DEFPERF_ASOCIAHR_XLENG LEFT JOIN SWE_CATA " +
                                                "            ON SWE_CATA.ID_CATALOGO = SWE_DEFPERF_ASOCIAHR_XLENG.ID_LENGUAJE WHERE VIGENCIA_FINAL IS NULL) " +
                                                "GROUP BY LENGUAJE " +
                                                "ORDER BY LENGUAJE";
    
    
    
    @Override
    public List<SweDefperfAsociahrXleng> findAll(Serializable idLenguaje) {  
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
         try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_VIGENTES);
            q.setParameter("idLenguaje", idLenguaje);
            List<SweDefperfAsociahrXleng> lista = q.list();
             for (int i = 0; i < lista.size(); i++) {
                 Hibernate.initialize(lista.get(i).getSweCataByIdPerfil());
                 Hibernate.initialize(lista.get(i).getSweCataByIdLenguaje().getNombre()); 
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

    public Object guardar(SweDefperfAsociahrXleng pefilXlenguaje){        
        SweDefperfAsociahrXleng ultimo = this.consultar_ultimo(pefilXlenguaje.getSweCataByIdLenguaje().getIdCatalogo(), 
                                pefilXlenguaje.getSweCataByIdPerfil().getIdCatalogo());
        
        if(ultimo instanceof SweDefperfAsociahrXleng ){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            ultimo.setVigenciaFinal(c.getTime()); 
            this.update(ultimo);            
        }        
        return this.save(pefilXlenguaje);
    }
    
    
    
    public List<TablaPefilxLenguaje> verTabla_Vigente(){
        List<TablaPefilxLenguaje> l = new ArrayList<TablaPefilxLenguaje>();
        TablaPefilxLenguaje perfilXlenguaje;
        
        PreparedStatement pStatement = null;
        DataBaseConnector connector = new DataBaseConnector();
        ResultSet rs;
        
        try{
            connector.Connection(); 
            pStatement = connector.getConexion().prepareStatement(SQL_VIGENTES); 
            rs = pStatement.executeQuery();
            
            while (rs.next()) {
                 perfilXlenguaje = new TablaPefilxLenguaje();
                 perfilXlenguaje.setLenguaje(rs.getString(1));
                 perfilXlenguaje.setHoras_senior(rs.getBigDecimal(2)); 
                 perfilXlenguaje.setHoras_semiSenior(rs.getBigDecimal(3)); 
                 perfilXlenguaje.setHoras_junior(rs.getBigDecimal(4));
                 perfilXlenguaje.setVigencia_inicial(new Date());
                 l.add(perfilXlenguaje); 
            }
            return l;
        }catch(SQLException e){            
            return null;
        }finally{
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                    connector.nullConector();
                }
            } catch (SQLException ex) {}
        }
    }
    
    
    @Override
    public SweDefperfAsociahrXleng consultar_ultimo(Integer... id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_ULTIMO);
            q.setParameter("idLenguaje", id[0]);
            q.setParameter("idPerfil", id[1]);
            return (SweDefperfAsociahrXleng)q.uniqueResult();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }

    @Override
    public List<SweDefperfAsociahrXleng> consultar_historial(Integer... id) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q =  sessions.createQuery(HQL_HISTORICO);  
            q.setParameter("idLenguaje", id[0]);
            q.setParameter("idPerfil", id[1]);
            return (List<SweDefperfAsociahrXleng>)q.list();
        } catch (HibernateException e) {
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
}

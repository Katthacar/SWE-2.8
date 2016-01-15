/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweMenu;
import com.amerika.swe.model.SweUsuarios;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author estudiante.proyectos
 */
public class UsuariosDAO extends HibernateDAO<SweUsuarios>{

    public static final String SQL_LOGIN = "SELECT * FROM SWE_USUARIOS U WHERE U.CODIGO=:username AND U.PASS=RAWTOHEX(UTL_RAW.CAST_TO_RAW(sys.dbms_obfuscation_toolkit.md5(input_string => :password))) AND U.ESTADO='A'";
    public static final String HQL_MENU = "FROM SweMenu m WHERE m.codigo IN (SELECT mp.id.codigoMenu "
                                            + " FROM SweMenuPerfil mp WHERE mp.id.codigoPerfil=:codPerfil) AND m.sweMenu=null ORDER BY m.codigo";
    public static final String HQL_MENU_ITEM = "FROM SweMenu m WHERE m.codigo IN (SELECT mp.id.codigoMenu "
                                                + " FROM SweMenuPerfil mp WHERE mp.id.codigoPerfil=:codPerfil) AND m.sweMenu.codigo=:codPadre ORDER BY m.codigo";   
    public static final String SQL_CHANGE_PASS = "UPDATE SWE_USUARIOS U SET U.PASS = RAWTOHEX(UTL_RAW.CAST_TO_RAW(sys.dbms_obfuscation_toolkit.md5(input_string => :contrasena ))) WHERE U.CODIGO = :codUsuario";

    @Override
    public List<SweUsuarios> findAll(Serializable arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public SweUsuarios login(String username, String password){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            SQLQuery q = sessions.createSQLQuery(SQL_LOGIN).addEntity(SweUsuarios.class);
            q.setParameter("username", username);
            q.setParameter("password", password);
            SweUsuarios u = (SweUsuarios)q.uniqueResult();
            if(u!=null){
                Hibernate.initialize(u.getSweUsuarioPerfils()); 
            }            
            return u;
        }catch(Exception e){
            return null;
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    
    public List<SweMenu> lista_menu(String codigoPerfil){
       Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_MENU);
            q.setParameter("codPerfil", codigoPerfil);
            List<SweMenu> menu = q.list();
            return menu;
        }catch(Exception e){
            return new ArrayList<SweMenu>();
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    
    }
      
    public List<SweMenu> lista_menu(String codigoPerfil, Integer codPadre){
         Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_MENU_ITEM);
            q.setParameter("codPerfil", codigoPerfil);
            q.setParameter("codPadre", codPadre); 
            List<SweMenu> menu = q.list();
            return menu;
        }catch(Exception e){
            return new ArrayList<SweMenu>();
        }finally{
            if(sessions.isOpen()){
                sessions.close();
            }
        }
    }
    public Object cambiarConstrasena(String username, String contrasena, String nuevaContrasena){
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transantion = null;
        Integer aux = 0;
        try{
            transantion = sessions.beginTransaction();
            SQLQuery q = sessions.createSQLQuery(SQL_LOGIN).addEntity(SweUsuarios.class);
            q.setParameter("username", username);
            q.setParameter("password", contrasena);
            SweUsuarios u = (SweUsuarios)q.uniqueResult();
            
            if(u!=null){
                SQLQuery sql =  sessions.createSQLQuery(SQL_CHANGE_PASS);
                sql.setParameter("contrasena", nuevaContrasena);
                sql.setParameter("codUsuario", username);
                aux = sql.executeUpdate();
            }else{
                return false;
            }      
            transantion.commit();
            return aux;
        }catch(Exception e){
            if(transantion!=null){
                transantion.rollback();
            }
            return e;
        }finally{            
            if(sessions.isOpen()){
                sessions.close();
            }
        }    
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Genera la conexión de la aplicación con la base de datos, utilizando el controlador JDBC de ORACLE
 * 
 * @author <b> Anders Barrios Escorcia</b>
 */
public class DataBaseConnector {
    
    /**
     *Contiene la instancia Connection de la base de datos
     */
    private Connection conexion = null; 
    
    /**
     *Establece la conexión con la base de datos
     */
    public void Connection(){
       try{
            
            Class.forName("oracle.jdbc.OracleDriver");
            this.conexion = DriverManager.getConnection("jdbc:oracle:thin:@10.6.6.18:1521:desarrollo", "sweamerika", "amerikadesa");
            
        }catch (ClassNotFoundException  ex){
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {           
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    /**
     * Retorna el objeto conexion                                                                                                                                                                                                       n
     * @return Connection
     */
    public Connection getConexion() {
        return conexion;
    }
    
    /**
     * Vuelve nula la instancia de la conexión
     */
    public void nullConector(){
        this.conexion = null;
    }
    
    /**
     * Cierra la conexión con el servidor de base de datos
     */
    public void closeConnection(){
        try{
             this.getConexion().close();
        }catch(SQLException e){
            e.printStackTrace();
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}

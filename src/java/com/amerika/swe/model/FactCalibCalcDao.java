package com.amerika.swe.model;

import com.amerika.swe.util.DataBaseConnector;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author estudiante.soporte
 */
public class FactCalibCalcDao implements Serializable {

    public FactCalibCalcDao(){
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
            callableStatement = connector.getConexion().prepareCall("PK_SWEESTIMACION.P_CalculaFactorCal(?)");
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
                Logger.getLogger(FactCalibCalcDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

}

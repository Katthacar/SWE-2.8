package com.amerika.swe.dao;

import com.amerika.swe.model.SwePronostico;
import com.amerika.swe.util.DataBaseConnector;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author estudiante.soporte
 */
public class SwePronosticoDao extends HibernateDAO<SwePronostico> {

    private static final String SQL_ID_SOLICITUD = "select id_lenguaje, id_perfil, APF"
            + " from SWE_SOLI where num_solicitud = ?";

    public SwePronosticoDao() {
    }

    @Override
    public List<SwePronostico> findAll(Serializable arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*método que llama al procedimiento P_PronosticarHoras,
     se obtiene el valor de las horas pronosticadas y este valor
     se retorna al llamante
     */
    public BigDecimal ejec_P_PronosticarHoras(int idLenguaje, int idPerfil, BigDecimal apf, String idUsuario) {

        BigDecimal horasPronosticadas = BigDecimal.ZERO;

        //llamando procedimiento P_PronosticarHoras
        DataBaseConnector connector = new DataBaseConnector();
        CallableStatement callableStatement = null;
        String mensaje = "";
        try {
            connector.Connection();
            callableStatement = connector.getConexion().prepareCall("{call PK_SWEESTIMACION.P_PronosticarHoras(?, ?, ?, ?, ?, ?)}");
            callableStatement.setInt(1, idLenguaje);
            callableStatement.setInt(2, idPerfil);
            callableStatement.setBigDecimal(3, apf);
            callableStatement.setString(4, idUsuario);
            callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
            callableStatement.execute();
            horasPronosticadas = callableStatement.getBigDecimal(5);
            mensaje = callableStatement.getString(6);

            return horasPronosticadas;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                    connector.nullConector();
                }
            } catch (SQLException ex) {
                ex.getMessage();
            }
        }

    }

}

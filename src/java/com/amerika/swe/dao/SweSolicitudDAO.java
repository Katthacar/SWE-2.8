/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.dao;

import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SwePronostico;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.util.ReporteHoras;
import com.amerika.swe.util.DataBaseConnector;
import com.amerika.swe.util.HibernateUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author estudiante.proyectos
 */
public class SweSolicitudDAO extends HibernateDAO<SweSoli> {

    private static final String HQL_BUSCAR_ESTADO = "FROM SweCata c WHERE c.sweTipoCata.codigoTipo = (SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre=:nombre) AND (c.estado IS NULL OR c.estado='A') AND c.nombre=:estado";

    private static final String SQL_SOLICITUD = "SELECT SO.NOMCLI CLIENTE, SO.DESC_CORTA NOMBRE, U.NOMBRE ANALISTA, SO.FECHA FECHA, TIPO_QUEJA.DESCRIPCION TIPO_SOLICITUD, SO.FECRES, SO.FECHA_ANULACION "
            + "FROM SERVIDESK.SOLICITUD_OBRAS SO INNER JOIN SERVIDESK.TIPO_QUEJA ON SO.TPQ_CODIGO = TIPO_QUEJA.CODIGO "
            + "LEFT JOIN INICIAR.USUARIOS U ON U.CODIGO = SO.USU_GENERA_OT "
            + "WHERE SO.CODIGO = ? ";

    private static final String SQL_VER_SOLICITUD = "SELECT SO.NOMCLI CLIENTE, SO.DESC_CORTA NOMBRE, U.NOMBRE ANALISTA, SO.FECHA FECHA, TIPO_QUEJA.DESCRIPCION TIPO_SOLICITUD "
            + "FROM SERVIDESK.SOLICITUD_OBRAS SO INNER JOIN SERVIDESK.TIPO_QUEJA ON SO.TPQ_CODIGO = TIPO_QUEJA.CODIGO "
            + "LEFT JOIN INICIAR.USUARIOS U ON U.CODIGO = SO.USU_GENERA_OT "
            + "WHERE SO.CODIGO = ? ";

    private static final String HQL_BUSCAR_ESTIMACION = "FROM SweSoli s WHERE s.numSolicitud =:idSolicitud AND s.sweCataByIdEstado.idCatalogo = "
            + "(SELECT c.idCatalogo FROM SweCata c WHERE c.sweTipoCata.codigoTipo = (SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre=:nombreTipoCata) AND (c.estado IS NULL OR c.estado='A') AND c.nombre=:nombreEstado))";

    /**
     * ***********************************************************************************
     */
    static final String HQL_BUSCAR_ESTIMACION_NO_APROBADA = "FROM SweSoli s WHERE s.numSolicitud =:idSolicitud AND s.sweCataByIdEstado.idCatalogo = "
            + "(SELECT c.idCatalogo FROM SweCata c WHERE c.sweTipoCata.codigoTipo = (SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre=:nombreTipoCata) AND (c.estado IS NULL OR c.estado='A') AND c.nombre=:nombreEstado))";

    static final String HQL_BUSCAR_ESTIMACION_NO_APROBADA_SIN_COD = "FROM SweSoli s WHERE s.sweCataByIdEstado.idCatalogo = "
            + "(SELECT c.idCatalogo FROM SweCata c WHERE c.sweTipoCata.codigoTipo = "
            + "(SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre=:nombreTipoCata) "
            + "And c.nombre =:nombreEstado) And s.aprobada = 'N'  And s.numSolicitud NOT IN (SELECT  s1.numSolicitud FROM SweSoli s1 WHERE s1.aprobada = 'S') "
            + "Order By s.numSolicitud,s.idEstimacion ";

    /*FROM SweSoli s WHERE s.sweCataByIdEstado.idCatalogo = 
     (SELECT c.idCatalogo FROM SweCata c WHERE c.sweTipoCata.codigoTipo =
     (SELECT t.codigoTipo FROM SweTipoCata t WHERE t.nombre='Estados')
     And c.nombre = 'Cerrado') And s.aprobada = 'N'*/
    /**
     * ***********************************************************************************
     */
    private static final String SQL_HORAS_TIPO_SOLI = "SELECT COUNT(DISTINCT SOL.ID_ESTIMACION) ESTIMACIONES, SOL.ID_TPOSOLICITUD, CA.NOMBRE, "
            + "SUM(ETS.HORAS_ESTIMADAS) HE, SUM(ETS.HORAS_REALES) HR, ROUND(AVG(SOL.FCE),2) AVG_FCE, ROUND(AVG(SOL.FCR),2) AVG_FCR "
            + "FROM (SWE_SOLI SOL INNER JOIN SWE_CATA CA ON CA.ID_CATALOGO = SOL.ID_TPOSOLICITUD) "
            + "INNER JOIN SWE_ETAPA_XSOL ETS ON ETS.ID_ESTIMACION = SOL.ID_ESTIMACION "
            + "WHERE SOL.ID_ESTADO = (SELECT ESTADO.ID_CATALOGO FROM SWE_CATA ESTADO WHERE ESTADO.NOMBRE = 'Cerrado') AND SOL.APROBADA = 'S'"
            + "AND SOL.FECHA_APROBACION  BETWEEN (:fechaInicial-1) AND (:fechaFinal + 1) "
            + "GROUP BY (SOL.ID_TPOSOLICITUD, CA.NOMBRE) "
            + "ORDER BY SOL.ID_TPOSOLICITUD ASC";

    private static final String SQL_HORAS_ETAPA = "SELECT CA.ID_CATALOGO ID_ETAPA, CA.NOMBRE ETAPA, SUM(ET.HORAS_ESTIMADAS) HE, SUM(ET.HORAS_REALES) HR "
            + "FROM ((SWE_ETAPA_XSOL ET INNER JOIN SWE_CATA CA ON ET.CODIGO_ETAPA = CA.ID_CATALOGO) "
            + "      INNER JOIN SWE_SOLI SOL ON SOL.ID_ESTIMACION = ET.ID_ESTIMACION) "
            + "WHERE SOL.ID_ESTADO = (SELECT ESTADO.ID_CATALOGO FROM SWE_CATA ESTADO WHERE ESTADO.NOMBRE = 'Cerrado') "
            + "      AND SOL.ID_TPOSOLICITUD = :idTipoSolicitud AND "
            + "      SOL.FECHA_APROBACION  BETWEEN (:fechaInicial - 1) AND (:fechaFinal + 1) "
            + "GROUP BY(CA.ID_CATALOGO, CA.NOMBRE) "
            + "ORDER BY CA.ID_CATALOGO ASC";

    private static final String HQL_ESTIMACION_APROBADA = "FROM SweSoli s WHERE s.numSolicitud =:idSolicitud AND s.aprobada=:aprobacion";

    @Override
    public List<SweSoli> findAll(Serializable arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object[] buscarSolicitud(Integer idSolicitud, String estadoEstimacion) {
        PreparedStatement pStatement = null;
        DataBaseConnector connector = new DataBaseConnector();
        ResultSet rs;
        Object[] obj = new Object[8];
        try {
            connector.Connection();
            pStatement = connector.getConexion().prepareStatement(SQL_SOLICITUD);
            pStatement.setInt(1, idSolicitud);
            rs = pStatement.executeQuery();

            while (rs.next()) {
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getDate(4);
                obj[4] = rs.getString(5);
                obj[5] = rs.getDate(6);
                obj[6] = rs.getDate(7);
            }

            /*
             * Verificamos que no haya estimación aprobada con este código de solicitud.
             * Si no existe estimación aprobada con esta solicitud, se busca la estimación
             * con el estado enviado como parámetro
             */
            obj[7] = buscarEstimacionXaprobacion(idSolicitud, "S");
            if (obj[7] == null || ((List) obj[7]).isEmpty()) {
                obj[7] = buscar_Estimacion(idSolicitud, estadoEstimacion);
            }

            return obj;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                    connector.nullConector();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public Object[] verEstimacion(Integer idSolicitud, String estadoEstimacion) {
        PreparedStatement pStatement = null;
        DataBaseConnector connector = new DataBaseConnector();
        ResultSet rs;
        Object[] obj = new Object[6];
        try {
            connector.Connection();
            pStatement = connector.getConexion().prepareStatement(SQL_VER_SOLICITUD);
            pStatement.setInt(1, idSolicitud);
            rs = pStatement.executeQuery();

            while (rs.next()) {
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getDate(4);
                obj[4] = rs.getString(5);
            }

            obj[5] = buscar_Estimacion(idSolicitud, estadoEstimacion);
            return obj;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                    connector.nullConector();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public List<SweSoli> buscar_Estimacion(Integer idSolicitud, String estado) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR_ESTIMACION);
            q.setParameter("idSolicitud", idSolicitud);
            q.setParameter("nombreTipoCata", "Estados");
            q.setParameter("nombreEstado", estado);
            List<SweSoli> l = q.list();

            for (int i = 0; i < l.size(); i++) {
                Hibernate.initialize(l.get(i).getSweCataByIdAplicacion());
                Hibernate.initialize(l.get(i).getSweCataByIdEstado());
                Hibernate.initialize(l.get(i).getSweCataByIdLenguaje());
                Hibernate.initialize(l.get(i).getSweCataByIdPerfil());
                Hibernate.initialize(l.get(i).getSweCataByIdTposolicitud());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_estima());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_cierra());
                Hibernate.initialize(l.get(i).getSwePronosticos());
            }
            return l;
        } catch (HibernateException e) {
            return new ArrayList<SweSoli>();
        } finally {
            if (sessions.isOpen()) {
                sessions.close();
            }
        }

    }

    /**
     * **Buscando estimaciones no aprobadas***
     */
    public List<SweSoli> buscar_Estimacion_no_aprobada(String estado) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_BUSCAR_ESTIMACION_NO_APROBADA_SIN_COD);
            q.setParameter("nombreTipoCata", "Estados");
            q.setParameter("nombreEstado", estado);
            List<SweSoli> l = q.list();

            for (int i = 0; i < l.size(); i++) {
                Hibernate.initialize(l.get(i).getSweCataByIdAplicacion());
                Hibernate.initialize(l.get(i).getSweCataByIdEstado());
                Hibernate.initialize(l.get(i).getSweCataByIdLenguaje());
                Hibernate.initialize(l.get(i).getSweCataByIdPerfil());
                Hibernate.initialize(l.get(i).getSweCataByIdTposolicitud());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_estima());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_cierra());
            }
            return l;
        } catch (HibernateException e) {
            return new ArrayList<SweSoli>();
        } finally {
            if (sessions.isOpen()) {
                sessions.close();
            }
        }

    }

    /**
     * **Fin busqueda estimaciones no aprobadas***
     */
    /**
     * Este método consulta la estimación dependiendo del estado de aprobación.
     * Recibe como parametro el id de la solicitud y la aprobación 'S' o 'N'
     */
    public List<SweSoli> buscarEstimacionXaprobacion(Integer idSolicitud, String aprobacion) {
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();
            Query q = sessions.createQuery(HQL_ESTIMACION_APROBADA);
            q.setParameter("idSolicitud", idSolicitud);
            q.setParameter("aprobacion", aprobacion);
            List<SweSoli> l = q.list();

            for (int i = 0; i < l.size(); i++) {
                Hibernate.initialize(l.get(i).getSweCataByIdAplicacion());
                Hibernate.initialize(l.get(i).getSweCataByIdEstado());
                Hibernate.initialize(l.get(i).getSweCataByIdLenguaje());
                Hibernate.initialize(l.get(i).getSweCataByIdPerfil());
                Hibernate.initialize(l.get(i).getSweCataByIdTposolicitud());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_estima());
                Hibernate.initialize(l.get(i).getSweUsuariosByIdUsu_cierra());
            }
            return l;
        } catch (Exception e) {
            return null;
        } finally {
            if (sessions.isOpen()) {
                sessions.close();
            }
        }
    }

    /**
     * Este metodo es el que guarda la estimación de la solicitud recibe como
     * parametro la estimacion (SweSoli), una lista con los componentes
     * asociados a la solicitud, una lista de factores tecnicos, una lista de
     * factores ambientales y la lista con la distribucion de las etapas
     *
     */
    public Object guardarEstimacion(SweSoli estimacion, Collection<List<SweCompXsoli>> componentesXsolicitud,
            List<SweFactAmbiXsol> factorAmbSolicitud, List<SweFtecnXsol> factorTecSolicitud,
            List<SweEtapaXsol> etapaSolicitud, SwePronostico pronostico) {

        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;
        try {
            transactions = sessions.beginTransaction();

            Query q = sessions.createQuery(HQL_BUSCAR_ESTADO);
            q.setParameter("nombre", "Estados");
            q.setParameter("estado", "Abierto");
            estimacion.setSweCataByIdEstado((SweCata) q.uniqueResult());

            sessions.saveOrUpdate(estimacion);

            if (pronostico != null) {
                //Seteando valores de la estimacion, error y error pronostico al cuadrado
                pronostico.setSweSoli(estimacion);
                sessions.saveOrUpdate(pronostico);
            }

            /*Registramos los componenentes*/
            for (List<SweCompXsoli> list_Comp : componentesXsolicitud) {
                for (int i = 0; i < list_Comp.size(); i++) {
                    list_Comp.get(i).setSweSoli(estimacion);
                    sessions.saveOrUpdate(list_Comp.get(i));
                    if ((i > 0) && (i % 20 == 0)) {
                        sessions.flush();
                        sessions.clear();
                    }
                }
            }

            /*Registramos los factores ambientales*/
            for (int i = 0; i < factorAmbSolicitud.size(); i++) {
                factorAmbSolicitud.get(i).getId().setIdEstimacion(estimacion.getIdEstimacion());
                factorAmbSolicitud.get(i).setSweSoli(estimacion);
                sessions.saveOrUpdate(factorAmbSolicitud.get(i));
                if ((i > 0) && (i % 20 == 0)) {
                    sessions.flush();
                    sessions.clear();
                }
            }

            /*Registramos los fatores tecnicos*/
            for (int i = 0; i < factorTecSolicitud.size(); i++) {
                factorTecSolicitud.get(i).getId().setIdEstimacion(estimacion.getIdEstimacion());
                factorTecSolicitud.get(i).setSweSoli(estimacion);
                sessions.saveOrUpdate(factorTecSolicitud.get(i));
                if ((i > 0) && (i % 20 == 0)) {
                    sessions.flush();
                    sessions.clear();
                }
            }

            /*Registramos las etapas*/
            for (int i = 0; i < etapaSolicitud.size(); i++) {
                etapaSolicitud.get(i).getId().setIdEstimacion(estimacion.getIdEstimacion());
                etapaSolicitud.get(i).getId().setIdEstimacion(estimacion.getIdEstimacion());
                sessions.saveOrUpdate(etapaSolicitud.get(i));
                if ((i > 0) && (i % 20 == 0)) {
                    sessions.flush();
                    sessions.clear();
                }
            }

            sessions.flush();
            sessions.clear();
            transactions.commit();
            return true;
        } catch (Exception e) {
            if (transactions != null) {
                transactions.rollback();
            }
            if (sessions.isOpen()) {
                sessions.close();
            }
            return e;
        }
    }

    /**
     * Este metodo cierra la estimación, cambiando su estado de abierto a
     * cerrado
     */
    public Object cambiarEstadoEstimacion(SweSoli estimacion, String estado) {

        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transactions = null;

        try {
            transactions = sessions.beginTransaction();

            Query q = sessions.createQuery(HQL_BUSCAR_ESTADO);
            q.setParameter("nombre", "Estados");
            q.setParameter("estado", estado);
            estimacion.setSweCataByIdEstado((SweCata) q.uniqueResult());

            sessions.saveOrUpdate(estimacion);
            sessions.flush();
            sessions.clear();
            transactions.commit();
            return true;
        } catch (Exception e) {
            if (transactions != null) {
                transactions.rollback();
            }
            if (sessions.isOpen()) {
                sessions.close();
            }
            return e;
        }
    }

    public List<ReporteHoras> consultarReporte(Date fechaInicial, Date fechaFinal) {

        List<ReporteHoras> lista = new ArrayList<ReporteHoras>();
        Session sessions = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            sessions.beginTransaction();

            SQLQuery query = sessions.createSQLQuery(SQL_HORAS_TIPO_SOLI);
            SQLQuery subQuery = sessions.createSQLQuery(SQL_HORAS_ETAPA);

            query.setParameter("fechaInicial", fechaInicial);
            query.setParameter("fechaFinal", fechaFinal);

            subQuery.setParameter("fechaInicial", fechaInicial);
            subQuery.setParameter("fechaFinal", fechaFinal);

            List<Object[]> rsQuery = query.list();
            for (Object[] objects : rsQuery) {
                ReporteHoras filaReporte = new ReporteHoras((BigDecimal) objects[0], (BigDecimal) objects[1], "" + objects[2], (BigDecimal) objects[3], (BigDecimal) objects[4], (BigDecimal) objects[5], (BigDecimal) objects[6]);
                filaReporte.setSubReportHrEtapas(new ArrayList<ReporteHoras.ReporteHorasEtapa>());

                subQuery.setParameter("idTipoSolicitud", (BigDecimal) objects[1]);
                List<Object[]> iteratorSub = subQuery.list();
                for (Object[] objSub : iteratorSub) {
                    ReporteHoras.ReporteHorasEtapa filaSubReporte = new ReporteHoras.ReporteHorasEtapa((BigDecimal) objSub[0], "" + objSub[1], (BigDecimal) objSub[2],
                            (BigDecimal) objSub[3], BigDecimal.ZERO, BigDecimal.ZERO);
                    try {
                        BigDecimal porcHE = ((BigDecimal) objSub[2]).divide(filaReporte.getHr_estimadas(), 2, RoundingMode.HALF_EVEN);
                        filaSubReporte.setPorcDistrHE(porcHE);
                    } catch (Exception e) {
                    }
                    try {
                        BigDecimal porHR = ((BigDecimal) objSub[3]).divide(filaReporte.getHr_reales(), 2, RoundingMode.HALF_EVEN);
                        filaSubReporte.setPorcDistrHR(porHR);
                    } catch (Exception e) {
                    }
                    filaReporte.getSubReportHrEtapas().add(filaSubReporte);
                }
                lista.add(filaReporte);
            }
            return lista;
        } catch (Exception e) {
            return null;
        } finally {
            if (sessions.isOpen()) {
                sessions.close();
            }
        }
    }

    /**
     * Este metodo llama al procedimiento almacenado pr_cargar_hr_swe que
     * actualiza las horas de las etapas y el total de horas reales de la
     * estimación.
     */
    public Object actualizarHorasSWE(Date fechaInicial, Date fechaFinal) {

        DataBaseConnector connector = new DataBaseConnector();
        CallableStatement callableStatement = null;
        Integer estActualizadas;
        try {
            connector.Connection();
            callableStatement = connector.getConexion().prepareCall("call pr_cargar_hr_swe(?,?,?)");
            callableStatement.setDate(1, new java.sql.Date(fechaInicial.getTime()));
            callableStatement.setDate(2, new java.sql.Date(fechaFinal.getTime()));
            callableStatement.registerOutParameter(3, java.sql.Types.NUMERIC);

            callableStatement.executeUpdate();
            estActualizadas = callableStatement.getInt(3);
            return estActualizadas;
        } catch (SQLException e) {
            return e;
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                    connector.nullConector();
                }
            } catch (SQLException e) {
            }
        }
    }
}

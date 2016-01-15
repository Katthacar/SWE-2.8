/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.controller;

import com.amerika.swe.dao.SweCataDAO;
import com.amerika.swe.dao.SweCataDetretDAO;
import com.amerika.swe.dao.SweCompXPfuncionDAO;
import com.amerika.swe.dao.SweCompXsoliDAO;
import com.amerika.swe.dao.SweDefperfAsociahrXlengDAO;
import com.amerika.swe.dao.SweEtapaXsolDAO;
import com.amerika.swe.dao.SweFactAmbiXsolDAO;
import com.amerika.swe.dao.SweFactCalXTsolDAO;
import com.amerika.swe.dao.SweFtecnXsolDAO;
import com.amerika.swe.dao.SweParamDAO;
import com.amerika.swe.dao.SwePorcDistXetapaDAO;
import com.amerika.swe.dao.SwePronosticoDao;
import com.amerika.swe.dao.SweSolicitudDAO;
import com.amerika.swe.dao.SweTcompXCompDAO;
import com.amerika.swe.dao.SweTipoApliXFtecDAO;
import com.amerika.swe.dao.SweTipoCataDAO;
import com.amerika.swe.dao.SweTipoDetretDAO;
import com.amerika.swe.dao.UsuariosDAO;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCataDetret;
import com.amerika.swe.model.SweCompXPfuncion;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweDefperfAsociahrXleng;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFactCalXTsol;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SweMenu;
import com.amerika.swe.model.SweParam;
import com.amerika.swe.model.SwePorcDistXetapa;
import com.amerika.swe.model.SwePronostico;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.SweTcompXComp;
import com.amerika.swe.model.SweTipoApliXFtec;
import com.amerika.swe.model.SweTipoCata;
import com.amerika.swe.model.SweTipoDetret;
import com.amerika.swe.model.SweUsuarios;
import com.amerika.swe.model.util.ReporteHoras;
import com.amerika.swe.model.util.TablaPefilxLenguaje;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author estudiante.proyectos
 */
public class Controller implements Serializable {

    /**
     * *******************************
     * -------- PARAMETROS --------- * *******************************
     */
    public Object agregar_parametro(SweParam parametros) {
        return new SweParamDAO().save(parametros);
    }

    public Object editar_parametro(SweParam parametros) {
        return new SweParamDAO().update(parametros);
    }

    public Object eliminar_parametro(SweParam parametro) {
        return new SweParamDAO().delete(parametro);
    }

    public List<SweParam> listar_parametros() {
        return new SweParamDAO().findAll(null);
    }

    public BigDecimal buscar_parametro(String nombreParametro) {
        return new SweParamDAO().buscar_parametro(nombreParametro);
    }

    /**
     * *******************************
     * ------- TIPO CATALOGOS ------ * *******************************
     */
    public Object agregar_tipoCatalogo(SweTipoCata tipos_catalogo) {
        return new SweTipoCataDAO().save(tipos_catalogo);
    }

    public Object editar_tipoCatalogo(SweTipoCata tipos_catalogo) {
        return new SweTipoCataDAO().modificarCatalogo(tipos_catalogo);
    }

    public Object eliminar_tipoCatalogo(SweTipoCata tipo_catalogo) {
        return new SweTipoCataDAO().modificarCatalogo(tipo_catalogo);
    }

    public List<SweTipoCata> listar_tipoCatalogos() {
        return new SweTipoCataDAO().findAll(null);
    }

    public SweTipoCata buscar_tipoCatalogo(Integer id) {
        return new SweTipoCataDAO().load(SweTipoCata.class, id);
    }

    /*Este metodo retorna la cantidad de catalogo relacionados con un tipo
     *de catalogo especifico
     */
    public Long num_catalogos_tipo(Integer idTipoCatalogo) {
        return new SweTipoCataDAO().numeroCatalogos(idTipoCatalogo);
    }

    /**
     * *******************************
     * -------- CATALOGOS ---------- * *******************************
     */
    public Object agregar_catalogo(SweCata catalogos) {
        return new SweCataDAO().save(catalogos);
    }

    public Object editar_catalogo(SweCata catalogos) {
        return new SweCataDAO().modificarCatalogo(catalogos);
    }

    public List<SweCata> listar_catalogos(Integer idTipoCatalogo) {
        return new SweCataDAO().findAll(idTipoCatalogo);
    }

    public List<SweCata> listarPOR(String arg) {
        return new SweCataDAO().listarPOR(arg);
    }

    public SweCata buscar_catalogo(Integer Idcatalogo) {
        return (SweCata) new SweCataDAO().load(SweCata.class, Idcatalogo);
    }

    public List<String> nombres_catalogos(String arg) {
        return new SweCataDAO().nombresCatalogos(arg);
    }

    /**
     * *******************************************
     * -------- FACTOR DE CALIBRACIÓN ---------- *
     * ******************************************
     */
    public List<SweFactCalXTsol> listar_factorCalibracion_vigentes() {
        return new SweFactCalXTsolDAO().findAll(null);
    }

    public Object agregar_factorCalibracion(SweFactCalXTsol factor) {
        return new SweFactCalXTsolDAO().guardar(factor);
    }

    public List<SweFactCalXTsol> historial_fCalibracion(Integer idTipoSolicitud) {
        return new SweFactCalXTsolDAO().consultar_historial(idTipoSolicitud);
    }

    public SweFactCalXTsol consultar_factorCalibracion_tpSolicitud(Integer idTipoSolicitud) {
        return new SweFactCalXTsolDAO().consultar_ultimo(idTipoSolicitud);
    }

    /**
     * ******************************************************
     * -------- TIPO APLICACIÓN X FACTOR TÉCNICO ---------- *
     * *****************************************************
     */
    public Object agregar_TipoApliXFacTec(SweTipoApliXFtec tipoAplixFacTec) {
        return new SweTipoApliXFtecDAO().save(tipoAplixFacTec);
    }

    public Object editar_TipoApliXFactec(SweTipoApliXFtec tipoAplixFacTec) {
        return new SweTipoApliXFtecDAO().update(tipoAplixFacTec);
    }

    public List<SweTipoApliXFtec> listar_TipoApliXFactec() {
        return new SweTipoApliXFtecDAO().findAll(null);
    }

    public List<SweTipoApliXFtec> listar_TipoApliXFactec(Integer idAplicacion) {
        return new SweTipoApliXFtecDAO().buscar_porTipoAplicacion(idAplicacion);
    }

    /**
     * ******************************************************
     * -------- TIPO DE COMPONENTE X COMPLEJIDAD ---------- *
     * *****************************************************
     */
    public Object agegar_TipoComponenteXComplejidad(SweTcompXComp compXComplejidad) {
        return new SweTcompXCompDAO().guardar(compXComplejidad);
    }

    public List<SweTcompXComp> historial_TipoComponenteXComplejidad(Integer idTipoComplejidad, Integer idComponente) {
        return new SweTcompXCompDAO().consultar_historial(idTipoComplejidad, idComponente);
    }

    public List<SweTcompXComp> listar_TipoCompXCompl(Integer idComplejidad) {
        return new SweTcompXCompDAO().findAll(idComplejidad);
    }

    public Map<Integer, Map<Integer, Integer>> listar_pesos() {
        return new SweTcompXCompDAO().listar_pesos();
    }

    /**
     * *****************************************
     * -------- PERFILES POR LENGUAJE -------- *
     * *****************************************
     */
    public Object agregar_perfilXlenguaje(SweDefperfAsociahrXleng pefilXlenguaje) {
        return new SweDefperfAsociahrXlengDAO().guardar(pefilXlenguaje);
    }

    public List<SweDefperfAsociahrXleng> listarVigentes_perfilXlenguaje(Integer idLenguaje) {
        return new SweDefperfAsociahrXlengDAO().findAll(idLenguaje);
    }

    public List<TablaPefilxLenguaje> listarVigentes_perfilXlenguaje() {
        return new SweDefperfAsociahrXlengDAO().verTabla_Vigente();
    }

    public List<SweDefperfAsociahrXleng> historial_perfilXlenguaje(Integer idLenguaje, Integer idPerfil) {
        return new SweDefperfAsociahrXlengDAO().consultar_historial(idLenguaje, idPerfil);
    }

    public SweDefperfAsociahrXleng consultar_perfilLenguaje_vigente(Integer idLenguaje, Integer idPerfil) {
        return new SweDefperfAsociahrXlengDAO().consultar_ultimo(idLenguaje, idPerfil);
    }

    /**
     * **********************************************************
     * -------- PORCENTAJE DE DISTRIBUCIÓN POR ETAPA ---------- *
     * *********************************************************
     */
    public Object agregar_porcDistxEtapa(SwePorcDistXetapa... porDistxEtapa) {
        return new SwePorcDistXetapaDAO().guardar(porDistxEtapa);
    }

    public List<SwePorcDistXetapa> listarVigentes_porcDistxEtapa() {
        return new SwePorcDistXetapaDAO().findAll(null);
    }

    public List<SwePorcDistXetapa> historial_porcDistxEtapa(Integer idEtapa) {
        return new SwePorcDistXetapaDAO().consultar_historial(idEtapa);
    }

    /**
     * ******************************************
     * -------- TIPOS DET, RET Y FTR ---------- *
     * ******************************************
     */
    public Object agregar_DetRet(SweTipoDetret sweTipoDetret) {
        return new SweTipoDetretDAO().save(sweTipoDetret);
    }

    public List<SweTipoDetret> listar_DetRet() {
        return new SweTipoDetretDAO().findAll(null);
    }

    /**
     * **************************************
     * -------- CATALOGO DET RET ---------- *
     * **************************************
     */
    public Object agregar_catalogoDetRet(List<SweCataDetret> catalogos) {
        return new SweCataDetretDAO().agregar(catalogos);
    }

    public List<SweCataDetret> listar(Integer idComponente, Integer idDetRet) {
        return new SweCataDetretDAO().listar(idComponente, idDetRet);
    }

    /**
     * *******************************************************
     * -------- COMPLEJIDAD POR PUNTOS DE FUNCIÓN ---------- *
     * *******************************************************
     */
    public Object agregar_complejidaXpfuncion(List<SweCompXPfuncion> list_compXpFun) {
        return new SweCompXPfuncionDAO().guardar(list_compXpFun);
    }

    public SweCata consultarComp_puntoFuncion(Integer idComponente, Integer idDet, Integer idRetFtr, Integer valDet, Integer valRetFtr) {
        return new SweCompXPfuncionDAO().buscar_complejidad(idComponente, idDet, idRetFtr, valDet, valRetFtr);
    }

    public List<SweCompXPfuncion> listar_complejidadXpFuncion(Integer idTipoComponente) {
        return new SweCompXPfuncionDAO().findAll(idTipoComponente);
    }

    public Object eliminar_compXpfuncion(SweCompXPfuncion compXpFun) {
        return new SweCompXPfuncionDAO().delete(compXpFun);
    }

    /**
     * *******************************************************
     * -------- TIPO DE FUNCIÓN DET, RET Y FTR ------------- *
     * *******************************************************
     */
    public SweTipoDetret buscar_funcion(String nombre) {
        return new SweTipoDetretDAO().buscar(nombre);
    }

    /**
     * *******************************************************
     * ------------------ SOLICITUDES ---------------------- *
     * *******************************************************
     */
    public Object[] buscarSilicitudOT(Integer idSolicitud, String estadoEstimacion) {
        return new SweSolicitudDAO().buscarSolicitud(idSolicitud, estadoEstimacion);
    }

    public Object[] verEstimaciones(Integer idSolicitud, String estadoEstimacion) {
        return new SweSolicitudDAO().verEstimacion(idSolicitud, estadoEstimacion);
    }

    public List<SweSoli> buscarEstimaciones(Integer idSolicitud, String estado) {
        return new SweSolicitudDAO().buscar_Estimacion(idSolicitud, estado);
    }

    /**
     * *
     * Este meotodo carga todas la estimaciones asociadas a una solicitud.
     * Primero se cargan las estimaciones cerradas, luego se verifica si existe
     * una estimación abierta.
     */
    public List<SweSoli> buscarEstimaciones_solicitud(Integer idSolicitud) {
        SweSolicitudDAO solDAO = new SweSolicitudDAO();
        List<SweSoli> estimaciones = null;

        if (idSolicitud != null) {
            estimaciones = solDAO.buscar_Estimacion(idSolicitud, "Cerrado");
            List<SweSoli> abiertas = solDAO.buscar_Estimacion(idSolicitud, "Abierto");

            if (!abiertas.isEmpty()) {
                estimaciones.addAll(abiertas);
            }

        } else {
            estimaciones = solDAO.buscar_Estimacion_no_aprobada("Cerrado");
        }

        return estimaciones;
    }

    public Object guardarEstimacion(SweSoli estimacion, Collection<List<SweCompXsoli>> componentesXsolicitud,
            List<SweFactAmbiXsol> factorAmbSolicitud, List<SweFtecnXsol> factorTecSolicitud,
            List<SweEtapaXsol> etapaSolicitud, SwePronostico pronostico) {

        return new SweSolicitudDAO().guardarEstimacion(estimacion, componentesXsolicitud, factorAmbSolicitud, factorTecSolicitud, etapaSolicitud, pronostico);

    }

    public Object aprobarEstimacion(SweSoli estimacion) {
        return new SweSolicitudDAO().update(estimacion);
    }

    public Object cambiarEstadoEstimacion(SweSoli estimacion, String estado) {
        return new SweSolicitudDAO().cambiarEstadoEstimacion(estimacion, estado);
    }

    public List<ReporteHoras> consultarReporte(Date fechaInicial, Date fechaFinal) {
        return new SweSolicitudDAO().consultarReporte(fechaInicial, fechaFinal);
    }

    public Object actualizarHorasSWE(Date fechaInicial, Date fechaFinal) {
        return new SweSolicitudDAO().actualizarHorasSWE(fechaInicial, fechaFinal);
    }

    /**
     * *******************************************************
     * ----------- COMPONENTES X SOLICITUD ----------------- *
     * *******************************************************
     */
    public List<SweCompXsoli> buscar_CompSolicitud(Integer idEstimacion) {
        return new SweCompXsoliDAO().findAll(idEstimacion);
    }

    public Object eliminar_ComponenteSolicitud(SweCompXsoli componenteSoli) {
        return new SweCompXsoliDAO().delete(componenteSoli);
    }

    /**
     * *******************************************************
     * ---------- FACTORES AMBIENTAL X SOLICITUD ----------- *
     * *******************************************************
     */
    public List<SweFactAmbiXsol> buscar_FacAmbSolicitud(Integer idEstimacion) {
        return new SweFactAmbiXsolDAO().findAll(idEstimacion);
    }

    public Object elimnar_FacAmbSolicitud(SweFactAmbiXsol facAmbiental) {
        return new SweFactAmbiXsolDAO().delete(facAmbiental);
    }

    /**
     * *******************************************************
     * ---------- FACTORES TECNICOS X SOLICITUD ------------ *
     * *******************************************************
     */
    public List<SweFtecnXsol> buscar_FacTecSolicitud(Integer idEstimacion) {
        return new SweFtecnXsolDAO().findAll(idEstimacion);
    }

    /**
     * *******************************************************
     * ---------------- ETAPAS X SOLICITUD ------------------ *
     * *******************************************************
     */
    public List<SweEtapaXsol> buscar_EtapaSolicitud(Integer idEstimacion) {
        return new SweEtapaXsolDAO().findAll(idEstimacion);
    }

    /**
     * ***********************************************
     * ---------------- PRONOSTICO ------------------ *
     * ***********************************************
     */
    public BigDecimal ejec_P_PronosticarHoras(int idLenguaje, int idPerfil, BigDecimal apf, String idUsuario) {
        return new SwePronosticoDao().ejec_P_PronosticarHoras(idLenguaje, idPerfil, apf, idUsuario);
    }

    /**
     * **********************************************
     * ----------- FACTOR DE CALIBRACIÓN ----------- *
     * **********************************************
     */
    public String calcFactCal() {
        return new SweFactCalXTsolDAO().calcFactCalibracion();
    }

    /**
     * *************************************************
     * ---------------- INTERVALO_HR ------------------ *
     * *************************************************
     */
    public SweUsuarios login(String username, String password) {
        return new UsuariosDAO().login(username, password);
    }

    public List<SweMenu> lista_menu(String codigoPerfil, Integer codPadre) {
        if (codPadre == 0) {
            return new UsuariosDAO().lista_menu(codigoPerfil);
        } else {
            return new UsuariosDAO().lista_menu(codigoPerfil, codPadre);
        }

    }

    public Object cambiarContrasena(String username, String password, String nuevaContrasena) {
        return new UsuariosDAO().cambiarConstrasena(username, password, nuevaContrasena);
    }

    public Object actualizarUsuario(SweUsuarios usuario) {
        return new UsuariosDAO().update(usuario);
    }

}

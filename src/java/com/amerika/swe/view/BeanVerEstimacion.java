/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.SweTipoDetret;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanVerEstimacion implements Serializable {

    private final Controller controller;
    private List<SweSoli> list_estimaciones;
    private SweSoli estimacion_seleccionada;
    private List<SweEtapaXsol> list_etapas;
    private String nombreCliente;
    private String nombreProyecto;
    private String analista;
    private String fechaSolicitud;
    private Integer idSolicitud;
    private Integer idEstimacionSelected;
    private BigDecimal diasXmeses;
    private BigDecimal productividadxHora;
    /**
     * Los atributos a continuación son usados para la pestaña de puntos
     * funcionales no ajustados
     *
     */
    private final List<SweCata> list_TipoComponentes;
    private SweCata tipoComponente_actual;
    private final Map<Integer, Map<Integer, Integer>> mapa_pesosVigentes;
    private Map<Integer, List<SweCompXsoli>> mapa_listas;
    private Map<Integer, Map<Integer, Integer>> mapa_conteo;
    private Map<Integer, BigDecimal> porcentajes_PF;
    private List<SweCompXsoli> list_puntosFuncionales;
    private final List<SweCata> list_Complejidades;
    private SweTipoDetret retFtr;
    private String tituloDialog;
    private int indicePanel = 0;
    private int TOTAL_PF;
    private BigDecimal TOTAL_POCENTAJES;
    /**
     * Atributos para visualizar el factor de ajuste
     */
    private List<SweFtecnXsol> factoresTecnicos;
    private List<SweFactAmbiXsol> factoresAmbientales;
    private BigDecimal k1FactorAmbiental;
    private BigDecimal k2FactorAmbiental;
    private BigDecimal k1;
    private BigDecimal k2;
    private float totalInfluenciaAmbiental;
    /*Distribucón por etapa*/
    private BigDecimal pfPersonaMes;
    private BigDecimal horasXdia;
    private BigDecimal porcentajeProductividadhr;
    private BigDecimal totalPorcentajesEst;
    private BigDecimal totalPorcentajeReal;
    private BigDecimal totalHorasEstimadas;
    private BigDecimal totalHorasReales;

    public BeanVerEstimacion() {
        this.controller = new Controller();
        list_estimaciones = new ArrayList<SweSoli>();
        list_etapas = new ArrayList<SweEtapaXsol>();
        inicializarTotales();
        estimacion_seleccionada = new SweSoli();
        this.list_TipoComponentes = this.controller.listarPOR("Tipo De Componente");
        this.list_Complejidades = this.controller.listarPOR("Complejidad");
        this.mapa_pesosVigentes = this.controller.listar_pesos();

        k1 = this.controller.buscar_parametro("K1 VAF");
        k2 = this.controller.buscar_parametro("K2 VAF");

        k1FactorAmbiental = this.controller.buscar_parametro("K1 Infl Amb");
        k2FactorAmbiental = this.controller.buscar_parametro("K2 Infl Amb");

        pfPersonaMes = this.controller.buscar_parametro("PF persona mes");
        diasXmeses = this.controller.buscar_parametro("Días x mes");
        horasXdia = this.controller.buscar_parametro("Horas x día");
        porcentajeProductividadhr = this.controller.buscar_parametro("Productividad h");
        productividadxHora = horasXdia.multiply(porcentajeProductividadhr);
    }

    /**
     * @return the nombreCliente
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Este metodo consulta información de la solicitud con el código ingresado
     * por el usuario.
     *
     */
    public void buscarSolicitud() {

        list_estimaciones.clear();
        if (idSolicitud instanceof Integer) {
            Object[] obj = this.controller.verEstimaciones(idSolicitud, "Cerrado");

            if (obj != null && obj[0] != null && obj[1] != null && obj[2] != null || obj[3] != null && obj[4] != null) {
                nombreCliente = ("" + obj[0]).toUpperCase();
                nombreProyecto = ("" + obj[1]).toUpperCase();
                analista = ("" + obj[2]).toUpperCase();
                fechaSolicitud = DateFormat.getDateInstance(DateFormat.MEDIUM).format((java.util.Date) obj[3]);
                if (!((List<SweSoli>) obj[5]).isEmpty()) {
                    list_estimaciones = (List<SweSoli>) obj[5];
                    mostrarAprobada();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "La solicitud no ha sido estimada"));
                }
            } else {
                nombreCliente = "";
                nombreProyecto = "";
                analista = "";
                fechaSolicitud = null;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La solicitud no ha sido registrada"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Ingrese el código de la solicitud"));
        }
        estimacion_seleccionada = new SweSoli();
        list_etapas.clear();
    }

    /**
     * Este metodo busca dentro de las estimaciones consultadas cual se
     * encuentra aprobada para resaltarla y mostrar sus valores
     */
    public void mostrarAprobada() {
        for (int i = 0; i < list_estimaciones.size(); i++) {
            if (list_estimaciones.get(i).getAprobada().equals("S")) {
                estimacion_seleccionada = list_estimaciones.get(i);
                ajax_mostrarEstimacion();
                break;
            }
        }
    }

    public void ajax_mostrarEstimacion() {
        inicializarTotales();
        if (estimacion_seleccionada != null) {
            cargarDistribucionEtapa();
            cargarPuntosFuncion();
            cargarFactorAjuste();
        } else {
            estimacion_seleccionada = new SweSoli();
            list_etapas.clear();
        }
    }

    /**
     * Este metodo carga la distribución porcentual de las horas de la solicitud
     * seleccionada
     */
    private void cargarDistribucionEtapa() {

        list_etapas = this.controller.buscar_EtapaSolicitud(estimacion_seleccionada.getIdEstimacion());

        for (int i = 0; i < list_etapas.size(); i++) {

            totalPorcentajesEst = totalPorcentajesEst.add(list_etapas.get(i).getPorcenEstimado());
            totalPorcentajeReal = totalPorcentajeReal.add(list_etapas.get(i).getPorcenReal() != null ? list_etapas.get(i).getPorcenReal() : BigDecimal.ZERO);
            totalHorasEstimadas = totalHorasEstimadas.add(list_etapas.get(i).getHorasEstimadas());
            totalHorasReales = totalHorasReales.add(list_etapas.get(i).getHorasReales() != null ? list_etapas.get(i).getHorasReales() : BigDecimal.ZERO);
        }
        if (totalPorcentajeReal.compareTo(BigDecimal.valueOf(100.00f)) > 0) {
            totalPorcentajesEst = totalPorcentajesEst.setScale(0, RoundingMode.DOWN);
        }
        if (totalPorcentajeReal.compareTo(BigDecimal.valueOf(100.00f)) > 0) {
            totalPorcentajeReal = totalPorcentajeReal.setScale(0, RoundingMode.DOWN);
        }

    }

    /**
     * Este método carga los puntos de función clasificados por tipo
     */
    private void cargarPuntosFuncion() {
        List<SweCompXsoli> l = this.controller.buscar_CompSolicitud(estimacion_seleccionada.getIdEstimacion());
        if (!l.isEmpty()) {
            int idFrom = 0;
            int idAnterior = 0;
            for (int i = 0; i < l.size(); i++) {
                if (i == 0) {
                    idAnterior = l.get(i).getSweCataByIdTpocomponente().getIdCatalogo();
                } else if (idAnterior != l.get(i).getSweCataByIdTpocomponente().getIdCatalogo()) {
                    mapa_listas.put(l.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), l.subList(idFrom, i));
                    tipoComponente_actual = l.get(idFrom).getSweCataByIdTpocomponente();
                    list_puntosFuncionales = l.subList(idFrom, i);
                    actualizarValores();
                    idFrom = i;
                    idAnterior = l.get(i).getSweCataByIdTpocomponente().getIdCatalogo();
                }
            }
            mapa_listas.put(l.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), l.subList(idFrom, l.size()));
            tipoComponente_actual = l.get(idFrom).getSweCataByIdTpocomponente();
            list_puntosFuncionales = l.subList(idFrom, l.size());
            actualizarValores();
        }

    }

    private void cargarFactorAjuste() {
        this.factoresTecnicos = this.controller.buscar_FacTecSolicitud(estimacion_seleccionada.getIdEstimacion());
        this.factoresAmbientales = this.controller.buscar_FacAmbSolicitud(estimacion_seleccionada.getIdEstimacion());
        BigDecimal b = BigDecimal.ZERO;
        for (int i = 0; i < factoresAmbientales.size(); i++) {
            b = b.add(factoresAmbientales.get(i).getPeso().multiply(new BigDecimal(factoresAmbientales.get(i).getInfluencia())));
        }
        totalInfluenciaAmbiental = b.floatValue();
    }

    private void inicializarTotales() {
        this.mapa_listas = new HashMap<Integer, List<SweCompXsoli>>();
        this.mapa_conteo = new HashMap<Integer, Map<Integer, Integer>>();
        this.porcentajes_PF = new HashMap<Integer, BigDecimal>();
        this.TOTAL_POCENTAJES = BigDecimal.ZERO;
        this.TOTAL_PF = 0;
        totalPorcentajesEst = BigDecimal.ZERO;
        totalPorcentajeReal = BigDecimal.ZERO;
        totalHorasEstimadas = BigDecimal.ZERO;
        totalHorasReales = BigDecimal.ZERO;
    }

    public void verPF(SweCata tipoComponente) {
        this.list_puntosFuncionales = new ArrayList<SweCompXsoli>();
        if (mapa_listas.containsKey(tipoComponente.getIdCatalogo())) {
            this.list_puntosFuncionales = this.mapa_listas.get(tipoComponente.getIdCatalogo());
        }
        tipoComponente_actual = tipoComponente;
        this.retFtr = (tipoComponente.getNombre().equals("ILF") || tipoComponente.getNombre().equals("EIF") ? this.controller.buscar_funcion("RET") : this.controller.buscar_funcion("FTR"));
        this.tituloDialog = tipoComponente.getNombre() + " - " + tipoComponente.getDescripcion();
    }

    public void actualizarValores() {
        Map<Integer, Integer> conteo = new HashMap<Integer, Integer>();
        this.mapa_conteo.put(tipoComponente_actual.getIdCatalogo(), conteo);
        for (int i = 0; i < this.list_puntosFuncionales.size(); i++) {

            if (conteo.containsKey(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo())) {
                Integer val = conteo.get(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo());
                val++;
                conteo.remove(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo());
                conteo.put(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo(), val);
            } else {
                conteo.put(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo(), new Integer(1));
            }

        }
        this.TOTAL_PF = 0;
        int valores = 0;
        List<Integer> subTotales = new ArrayList<Integer>();
        for (int i = 0; i < this.list_TipoComponentes.size(); i++) {
            for (int j = 0; j < this.list_Complejidades.size(); j++) {
                try {
                    valores += mapa_conteo.get(this.list_TipoComponentes.get(i).getIdCatalogo()).get(this.list_Complejidades.get(j).getIdCatalogo())
                            * mapa_pesosVigentes.get(list_TipoComponentes.get(i).getIdCatalogo()).get(list_Complejidades.get(j).getIdCatalogo());
                } catch (NullPointerException e) {
                }
            }
            subTotales.add(valores);
            this.TOTAL_PF += valores;
            valores = 0;
        }

        /**
         * Actualizamos los porcentajes de los puntos de función por componente
         */
        this.porcentajes_PF.clear();
        this.TOTAL_POCENTAJES = BigDecimal.ZERO;
        for (int i = 0; i < subTotales.size(); i++) {
            BigDecimal n = BigDecimal.ZERO;
            if (TOTAL_PF != 0) {
                n = BigDecimal.valueOf((double) subTotales.get(i)).divide(BigDecimal.valueOf(TOTAL_PF), 5, RoundingMode.FLOOR);
                n = n.multiply(BigDecimal.valueOf(100));
                n = n.divide(BigDecimal.ONE, 1, RoundingMode.FLOOR);
            }
            this.porcentajes_PF.put(this.list_TipoComponentes.get(i).getIdCatalogo(), n);
            this.TOTAL_POCENTAJES = this.TOTAL_POCENTAJES.add(n).setScale(1, RoundingMode.UP);

        }
        this.TOTAL_POCENTAJES = this.TOTAL_POCENTAJES.divide(BigDecimal.ONE, 0, RoundingMode.UP);
    }

    /**
     * @param nombreCliente the nombreCliente to set
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * @return the nombreProyecto
     */
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    /**
     * @param nombreProyecto the nombreProyecto to set
     */
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    /**
     * @return the analista
     */
    public String getAnalista() {
        return analista;
    }

    /**
     * @param analista the analista to set
     */
    public void setAnalista(String analista) {
        this.analista = analista;
    }

    /**
     * @return the fechaSolicitud
     */
    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    /**
     * @param fechaSolicitud the fechaSolicitud to set
     */
    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    /**
     * @return the idSolicitud
     */
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * @param idSolicitud the idSolicitud to set
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * @return the idEstimacionSelected
     */
    public Integer getIdEstimacionSelected() {
        return idEstimacionSelected;
    }

    /**
     * @param idEstimacionSelected the idEstimacionSelected to set
     */
    public void setIdEstimacionSelected(Integer idEstimacionSelected) {
        this.idEstimacionSelected = idEstimacionSelected;
    }

    /**
     * @return the list_estimaciones
     */
    public List<SweSoli> getList_estimaciones() {
        return list_estimaciones;
    }

    /**
     * @param list_estimaciones the list_estimaciones to set
     */
    public void setList_estimaciones(List<SweSoli> list_estimaciones) {
        this.list_estimaciones = list_estimaciones;
    }

    /**
     * @return the estimacion_seleccionada
     */
    public SweSoli getEstimacion_seleccionada() {
        return estimacion_seleccionada;
    }

    /**
     * @param estimacion_seleccionada the estimacion_seleccionada to set
     */
    public void setEstimacion_seleccionada(SweSoli estimacion_seleccionada) {
        this.estimacion_seleccionada = estimacion_seleccionada;
    }

    /**
     * @return the list_etapas
     */
    public List<SweEtapaXsol> getList_etapas() {
        return list_etapas;
    }

    /**
     * @param list_etapas the list_etapas to set
     */
    public void setList_etapas(List<SweEtapaXsol> list_etapas) {
        this.list_etapas = list_etapas;
    }

    /**
     * @return the productividadxHora
     */
    public BigDecimal getProductividadxHora() {
        return productividadxHora;
    }

    /**
     * @param productividadxHora the productividadxHora to set
     */
    public void setProductividadxHora(BigDecimal productividadxHora) {
        this.productividadxHora = productividadxHora;
    }

    /**
     * @return the diasXmeses
     */
    public BigDecimal getDiasXmeses() {
        return diasXmeses;
    }

    /**
     * @param diasXmeses the diasXmeses to set
     */
    public void setDiasXmeses(BigDecimal diasXmeses) {
        this.diasXmeses = diasXmeses;
    }

    /**
     * @return the list_TipoComponentes
     */
    public List<SweCata> getList_TipoComponentes() {
        return list_TipoComponentes;
    }

    /**
     * @return the mapa_pesosVigentes
     */
    public Map<Integer, Map<Integer, Integer>> getMapa_pesosVigentes() {
        return mapa_pesosVigentes;
    }

    /**
     * @return the list_Complejidades
     */
    public List<SweCata> getList_Complejidades() {
        return list_Complejidades;
    }

    /**
     * @return the indicePanel
     */
    public int getIndicePanel() {
        indicePanel++;
        return indicePanel - 1;
    }

    /**
     * @param indicePanel the indicePanel to set
     */
    public void setIndicePanel(int indicePanel) {
        this.indicePanel = indicePanel;
    }

    /**
     * @return the mapa_listas
     */
    public Map<Integer, List<SweCompXsoli>> getMapa_listas() {
        return mapa_listas;
    }

    /**
     * @param mapa_listas the mapa_listas to set
     */
    public void setMapa_listas(Map<Integer, List<SweCompXsoli>> mapa_listas) {
        this.mapa_listas = mapa_listas;
    }

    /**
     * @return the list_puntosFuncionales
     */
    public List<SweCompXsoli> getList_puntosFuncionales() {
        return list_puntosFuncionales;
    }

    /**
     * @param list_puntosFuncionales the list_puntosFuncionales to set
     */
    public void setList_puntosFuncionales(List<SweCompXsoli> list_puntosFuncionales) {
        this.list_puntosFuncionales = list_puntosFuncionales;
    }

    /**
     * @return the tipoComponente_actual
     */
    public SweCata getTipoComponente_actual() {
        return tipoComponente_actual;
    }

    /**
     * @param tipoComponente_actual the tipoComponente_actual to set
     */
    public void setTipoComponente_actual(SweCata tipoComponente_actual) {
        this.tipoComponente_actual = tipoComponente_actual;
    }

    /**
     * @return the retFtr
     */
    public SweTipoDetret getRetFtr() {
        return retFtr;
    }

    /**
     * @param retFtr the retFtr to set
     */
    public void setRetFtr(SweTipoDetret retFtr) {
        this.retFtr = retFtr;
    }

    /**
     * @return the tituloDialog
     */
    public String getTituloDialog() {
        return tituloDialog;
    }

    /**
     * @param tituloDialog the tituloDialog to set
     */
    public void setTituloDialog(String tituloDialog) {
        this.tituloDialog = tituloDialog;
    }

    /**
     * @return the mapa_conteo
     */
    public Map<Integer, Map<Integer, Integer>> getMapa_conteo() {
        return mapa_conteo;
    }

    /**
     * @param mapa_conteo the mapa_conteo to set
     */
    public void setMapa_conteo(Map<Integer, Map<Integer, Integer>> mapa_conteo) {
        this.mapa_conteo = mapa_conteo;
    }

    /**
     * @return the porcentajes_PF
     */
    public Map<Integer, BigDecimal> getPorcentajes_PF() {
        return porcentajes_PF;
    }

    /**
     * @param porcentajes_PF the porcentajes_PF to set
     */
    public void setPorcentajes_PF(Map<Integer, BigDecimal> porcentajes_PF) {
        this.porcentajes_PF = porcentajes_PF;
    }

    /**
     * @return the TOTAL_PF
     */
    public int getTOTAL_PF() {
        return TOTAL_PF;
    }

    /**
     * @param TOTAL_PF the TOTAL_PF to set
     */
    public void setTOTAL_PF(int TOTAL_PF) {
        this.TOTAL_PF = TOTAL_PF;
    }

    /**
     * @return the TOTAL_POCENTAJES
     */
    public BigDecimal getTOTAL_POCENTAJES() {
        return TOTAL_POCENTAJES;
    }

    /**
     * @param TOTAL_POCENTAJES the TOTAL_POCENTAJES to set
     */
    public void setTOTAL_POCENTAJES(BigDecimal TOTAL_POCENTAJES) {
        this.TOTAL_POCENTAJES = TOTAL_POCENTAJES;
    }

    /**
     * @return the factoresTecnicos
     */
    public List<SweFtecnXsol> getFactoresTecnicos() {
        return factoresTecnicos;
    }

    /**
     * @param factoresTecnicos the factoresTecnicos to set
     */
    public void setFactoresTecnicos(List<SweFtecnXsol> factoresTecnicos) {
        this.factoresTecnicos = factoresTecnicos;
    }

    /**
     * @return the factoresAmbientales
     */
    public List<SweFactAmbiXsol> getFactoresAmbientales() {
        return factoresAmbientales;
    }

    /**
     * @param factoresAmbientales the factoresAmbientales to set
     */
    public void setFactoresAmbientales(List<SweFactAmbiXsol> factoresAmbientales) {
        this.factoresAmbientales = factoresAmbientales;
    }

    /**
     * @return the k1FactorAmbiental
     */
    public BigDecimal getK1FactorAmbiental() {
        return k1FactorAmbiental;
    }

    /**
     * @param k1FactorAmbiental the k1FactorAmbiental to set
     */
    public void setK1FactorAmbiental(BigDecimal k1FactorAmbiental) {
        this.k1FactorAmbiental = k1FactorAmbiental;
    }

    /**
     * @return the k2FactorAmbiental
     */
    public BigDecimal getK2FactorAmbiental() {
        return k2FactorAmbiental;
    }

    /**
     * @param k2FactorAmbiental the k2FactorAmbiental to set
     */
    public void setK2FactorAmbiental(BigDecimal k2FactorAmbiental) {
        this.k2FactorAmbiental = k2FactorAmbiental;
    }

    /**
     * @return the k1
     */
    public BigDecimal getK1() {
        return k1;
    }

    /**
     * @param k1 the k1 to set
     */
    public void setK1(BigDecimal k1) {
        this.k1 = k1;
    }

    /**
     * @return the k2
     */
    public BigDecimal getK2() {
        return k2;
    }

    /**
     * @param k2 the k2 to set
     */
    public void setK2(BigDecimal k2) {
        this.k2 = k2;
    }

    /**
     * @return the totalInfluenciaAmbiental
     */
    public float getTotalInfluenciaAmbiental() {
        return totalInfluenciaAmbiental;
    }

    /**
     * @param totalInfluenciaAmbiental the totalInfluenciaAmbiental to set
     */
    public void setTotalInfluenciaAmbiental(float totalInfluenciaAmbiental) {
        this.totalInfluenciaAmbiental = totalInfluenciaAmbiental;
    }

    /**
     * @return the pfPersonaMes
     */
    public BigDecimal getPfPersonaMes() {
        return pfPersonaMes;
    }

    /**
     * @param pfPersonaMes the pfPersonaMes to set
     */
    public void setPfPersonaMes(BigDecimal pfPersonaMes) {
        this.pfPersonaMes = pfPersonaMes;
    }

    /**
     * @return the porcentajeProductividadhr
     */
    public BigDecimal getPorcentajeProductividadhr() {
        return porcentajeProductividadhr;
    }

    /**
     * @param porcentajeProductividadhr the porcentajeProductividadhr to set
     */
    public void setPorcentajeProductividadhr(BigDecimal porcentajeProductividadhr) {
        this.porcentajeProductividadhr = porcentajeProductividadhr;
    }

    /**
     * @return the horasXdia
     */
    public BigDecimal getHorasXdia() {
        return horasXdia;
    }

    /**
     * @param horasXdia the horasXdia to set
     */
    public void setHorasXdia(BigDecimal horasXdia) {
        this.horasXdia = horasXdia;
    }

    /**
     * @return the totalPorcentajesEst
     */
    public BigDecimal getTotalPorcentajesEst() {
        return totalPorcentajesEst;
    }

    /**
     * @param totalPorcentajesEst the totalPorcentajesEst to set
     */
    public void setTotalPorcentajesEst(BigDecimal totalPorcentajesEst) {
        this.totalPorcentajesEst = totalPorcentajesEst;
    }

    /**
     * @return the totalPorcentajeReal
     */
    public BigDecimal getTotalPorcentajeReal() {
        return totalPorcentajeReal;
    }

    /**
     * @param totalPorcentajeReal the totalPorcentajeReal to set
     */
    public void setTotalPorcentajeReal(BigDecimal totalPorcentajeReal) {
        this.totalPorcentajeReal = totalPorcentajeReal;
    }

    /**
     * @return the totalHorasEstimadas
     */
    public BigDecimal getTotalHorasEstimadas() {
        return totalHorasEstimadas;
    }

    /**
     * @param totalHorasEstimadas the totalHorasEstimadas to set
     */
    public void setTotalHorasEstimadas(BigDecimal totalHorasEstimadas) {
        this.totalHorasEstimadas = totalHorasEstimadas;
    }

    /**
     * @return the totalHorasReales
     */
    public BigDecimal getTotalHorasReales() {
        return totalHorasReales;
    }

    /**
     * @param totalHorasReales the totalHorasReales to set
     */
    public void setTotalHorasReales(BigDecimal totalHorasReales) {
        this.totalHorasReales = totalHorasReales;
    }
}
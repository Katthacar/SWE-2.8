/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SwePorcDistXetapa;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.util.EsfuerzoEtapa;
import com.amerika.swe.view.estimacion.BeanFactorAjuste;
import com.amerika.swe.view.estimacion.BeanPFNoAjustados;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanEstimacionModelo {

    private final Controller controller;
    private List<SweSoli> estimaciones_anteriores;
    private SweSoli estimacion_modelo;

    public BeanEstimacionModelo() {
        this.controller = new Controller();
    }

    public void buscarAnteriores() {
        FacesContext context = FacesContext.getCurrentInstance();
        BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context, "#{beanEstimacion}", Object.class);
        estimaciones_anteriores = this.controller.buscarEstimaciones(estimacion.getIdSolicitud(), "Cerrado");
        RequestContext.getCurrentInstance().execute("dialog_est_modelo.show()");
    }

    public void usarModelo() {
        FacesContext context = FacesContext.getCurrentInstance();

        List<SweCompXsoli> componentes = this.controller.buscar_CompSolicitud(this.estimacion_modelo.getIdEstimacion());

        BeanPFNoAjustados pfNoAjustados = (BeanPFNoAjustados) context.getApplication().evaluateExpressionGet(context, "#{beanPFNoAjustados}", Object.class);

        /**
         * A continuación agregamos los componentes separados por listas en el
         * Bean pfNoAjustados
         */
        pfNoAjustados.reiniciarValores(); // Reiniciamos las listas y los mapas
        if (!componentes.isEmpty()) {
            int idFrom = 0;
            int idAnterior = 0;
            for (int i = 0; i < componentes.size(); i++) {
                if (i == 0) {
                    idAnterior = componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo();
                } else if (idAnterior != componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo()) {                    
                    List<SweCompXsoli> pf_modelos = separarPF(componentes.subList(idFrom, i));                    
                    pfNoAjustados.getMapa_listas().put(componentes.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), pf_modelos);
                    pfNoAjustados.setTipoComponente_actual(componentes.get(idFrom).getSweCataByIdTpocomponente());
                    pfNoAjustados.setList_puntosFuncionales(pf_modelos);
                    pfNoAjustados.actualizarValores();
                    idFrom = i;
                    idAnterior = componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo();

                }

            }
            List<SweCompXsoli> pf_modelos = separarPF(componentes.subList(idFrom, componentes.size()));
            pfNoAjustados.getMapa_listas().put(componentes.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), pf_modelos);
            pfNoAjustados.setTipoComponente_actual(componentes.get(idFrom).getSweCataByIdTpocomponente());
            pfNoAjustados.setList_puntosFuncionales(pf_modelos);
            pfNoAjustados.actualizarValores();
        }

        /**
         * A continuación obtenemos el BeanFactorAjuste para cargar los factores
         * técnicos y factores ambientales de la solicitud
         */
        BeanFactorAjuste factorAjuste = (BeanFactorAjuste) context.getApplication().evaluateExpressionGet(context, "#{beanFactorAjuste}", Object.class);

        /*Obtenemos los factores técnicos y los cargamos*/
        List<SweFtecnXsol> factoresTecnicos = this.controller.buscar_FacTecSolicitud(estimacion_modelo.getIdEstimacion());
        factorAjuste.setList_apliXfact(factoresTecnicos);
        factorAjuste.setTotalInfluencia(estimacion_modelo.getCt());   // Actualizamos el atributo Total Influencia        
        factorAjuste.setVAF(estimacion_modelo.getVaf());              // Actualizamos el VAF en el beanFactorAjuste

        /*Obtenemos los factores ambientales seleccionados en la estimación*/
        List<SweFactAmbiXsol> factoresAmbientales = this.controller.buscar_FacAmbSolicitud(estimacion_modelo.getIdEstimacion());
        SweFactAmbiXsol[] facAmb_seleccionados = new SweFactAmbiXsol[factoresAmbientales.size()];
        facAmb_seleccionados = factoresAmbientales.toArray(facAmb_seleccionados);
        BigDecimal b = BigDecimal.ZERO;
        if (facAmb_seleccionados != null) {
            for (int i = 0; i < facAmb_seleccionados.length; i++) {
                facAmb_seleccionados[i] = factoresAmbientales.get(i);
                facAmb_seleccionados[i].getId().setIdEstimacion(0);
                facAmb_seleccionados[i].setSweSoli(null); 
                for (int j = 0; j < factorAjuste.getList_factorAmbiental().size(); j++) {
                    if (facAmb_seleccionados[i].getSweCata().getIdCatalogo() == factorAjuste.getList_factorAmbiental().get(j).getSweCata().getIdCatalogo()) {
                        factorAjuste.getList_factorAmbiental().get(j).setInfluencia(facAmb_seleccionados[i].getInfluencia());
                        b = b.add(facAmb_seleccionados[i].getPeso().multiply(new BigDecimal(facAmb_seleccionados[i].getInfluencia())));                        
                        break;
                    }
                }

            }
        }

        factorAjuste.setTotalInfluenciaAmb(b.floatValue()); 
        factorAjuste.setFacAmbCalculado(estimacion_modelo.getFa());        
//      factorAjuste.setFacAmbCalculado(factorAjuste.getK1FactorAmbiental().subtract(BigDecimal.valueOf(factorAjuste.getTotalInfluenciaAmb()).multiply(factorAjuste.getK2FactorAmbiental())));
        factorAjuste.setFacAmb_seleccionados(facAmb_seleccionados);


        BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context, "#{beanEstimacion}", Object.class);
       
        /*Cargamos los porcentajes de distribución de las etapas*/
        List<SweEtapaXsol> esfuerEtapa = this.controller.buscar_EtapaSolicitud(estimacion_modelo.getIdEstimacion());
        estimacion.setTotalEsfuerzoHoras(BigDecimal.ZERO);
        estimacion.setTotalEsfuerzoDias(BigDecimal.ZERO);
        estimacion.setTotalEsfuerzoMeses(BigDecimal.ZERO);
        estimacion.setTotalPorcDistribucion(BigDecimal.ZERO);

        estimacion.getList_esfuerzoxEtapa().clear();
        for (int i = 0; i < esfuerEtapa.size(); i++) {
            SwePorcDistXetapa porcDistribucion = new SwePorcDistXetapa();
            porcDistribucion.setSweCata(esfuerEtapa.get(i).getSweCata());
            porcDistribucion.setPorcentaje(esfuerEtapa.get(i).getPorcenEstimado());
            
            EsfuerzoEtapa esfuerzo = new EsfuerzoEtapa(porcDistribucion);
            esfuerzo.setnHoras(esfuerEtapa.get(i).getHorasEstimadas());
            esfuerzo.setnDias(esfuerEtapa.get(i).getHorasEstimadas().divide(estimacion.getHorasXdia(), 2, RoundingMode.UP));
            esfuerzo.setnMeses(esfuerzo.getnDias().divide(estimacion.getDiasXmeses(), 2, RoundingMode.UP));

            estimacion.setTotalEsfuerzoHoras(estimacion.getTotalEsfuerzoHoras().add(esfuerzo.getnHoras()));
            estimacion.setTotalEsfuerzoDias(estimacion.getTotalEsfuerzoDias().add(esfuerzo.getnDias()));
            estimacion.setTotalEsfuerzoMeses(estimacion.getTotalEsfuerzoMeses().add(esfuerzo.getnMeses()));
            estimacion.setTotalPorcDistribucion(estimacion.getTotalPorcDistribucion().add(esfuerEtapa.get(i).getPorcenEstimado()));

            estimacion.getList_esfuerzoxEtapa().add(esfuerzo);
        }
        estimacion.setEstados_estimacion((byte) 2);   // Este estado refiere que la solicitud ha sido guardada
        estimacion.setDisableTabview(false);

        // CARGAMOS LOS VALORES DE LA ESTIMACION MODELO A AL NUEVA ESTIMACIÓN
        estimacion.getEstimacionSolicitud().setSweCataByIdAplicacion(estimacion_modelo.getSweCataByIdAplicacion());
        estimacion.getEstimacionSolicitud().setSweCataByIdLenguaje(estimacion_modelo.getSweCataByIdLenguaje());
        estimacion.getEstimacionSolicitud().setSweCataByIdPerfil(estimacion_modelo.getSweCataByIdPerfil());
        estimacion.getEstimacionSolicitud().setSweCataByIdTposolicitud(estimacion_modelo.getSweCataByIdTposolicitud()); 
        
        estimacion.getEstimacionSolicitud().setUpf(estimacion_modelo.getUpf());
        estimacion.getEstimacionSolicitud().setVaf(estimacion_modelo.getVaf());
        estimacion.getEstimacionSolicitud().setApf(estimacion_modelo.getApf());
        estimacion.getEstimacionSolicitud().setFce(estimacion_modelo.getFce());
        estimacion.getEstimacionSolicitud().setHe(estimacion_modelo.getHe());
        estimacion.getEstimacionSolicitud().setCt(estimacion_modelo.getCt());
        estimacion.getEstimacionSolicitud().setTpf(estimacion_modelo.getTpf());
        estimacion.getEstimacionSolicitud().setHepf(estimacion_modelo.getHepf()); 
        estimacion.getEstimacionSolicitud().setHrje(estimacion_modelo.getHrje());
        estimacion.getEstimacionSolicitud().setFa(estimacion_modelo.getFa());
        estimacion.getEstimacionSolicitud().setDescripcion(estimacion_modelo.getDescripcion()); 
        
        estimacion.buscarFactorCalibracion();
        RequestContext.getCurrentInstance().execute("dialog_est_modelo.hide()");
    }

    /**
     * Este metodo toma los puntos funcionales creados de una solicitud
     * y los encapsula en una nueva lista como nuevos valores para 
     * ser reutilizados en una nueva estimación. Se retorna una lista con los objetos 
     * sin su id y sin id de la estimación
     */
    public List<SweCompXsoli> separarPF(List<SweCompXsoli> sublista) {
        List<SweCompXsoli> pf_modelos = new ArrayList<SweCompXsoli>();
        for (int i = 0; i < sublista.size(); i++) {
            SweCompXsoli c = new SweCompXsoli();
            c.setCantidadDet(sublista.get(i).getCantidadDet());
            c.setCantidadRetftr(sublista.get(i).getCantidadRetftr());
            c.setNombre(sublista.get(i).getNombre());
            c.setObservacion(sublista.get(i).getObservacion());
            c.setPeso(sublista.get(i).getPeso());
            c.setSweCataByIdComplejidad(sublista.get(i).getSweCataByIdComplejidad());
            c.setSweCataByIdTpocomponente(sublista.get(i).getSweCataByIdTpocomponente());
            pf_modelos.add(c);
        }
        return pf_modelos;
    }

    /**
     * @return the estimaciones_anteriores
     */
    public List<SweSoli> getEstimaciones_anteriores() {
        return estimaciones_anteriores;
    }

    /**
     * @param estimaciones_anteriores the estimaciones_anteriores to set
     */
    public void setEstimaciones_anteriores(List<SweSoli> estimaciones_anteriores) {
        this.estimaciones_anteriores = estimaciones_anteriores;
    }

    /**
     * @return the estimacion_modelo
     */
    public SweSoli getEstimacion_modelo() {
        return estimacion_modelo;
    }

    /**
     * @param estimacion_modelo the estimacion_modelo to set
     */
    public void setEstimacion_modelo(SweSoli estimacion_modelo) {
        this.estimacion_modelo = estimacion_modelo;
    }
}

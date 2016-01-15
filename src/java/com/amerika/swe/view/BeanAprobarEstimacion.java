/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweSoli;
import java.util.Date;
import java.util.List;
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
public class BeanAprobarEstimacion {

    private final Controller controller;
    private List<SweSoli> lista_estimaciones;
    private List<SweEtapaXsol> lista_etapas;
    private Integer codigoSolicitud;
    private SweSoli solSeleccionada;

    public BeanAprobarEstimacion() {
        this.controller = new Controller();
    }

    public void buscarEstimaciones() {

        /*
         validando que exista un valor para codigoSolicitud, es decir,
         que se halla insertado un cod. de solicitud en el inputText codSolicitud
         */
        //if (codigoSolicitud != null) {

            lista_estimaciones = this.controller.buscarEstimaciones_solicitud(codigoSolicitud);
            solSeleccionada = null;

            // Verificamos que haya al menos una estimación con esa solicitud
            if (!lista_estimaciones.isEmpty()) {
                /*Verificamos si el ultimo elemento de la lista esta en estado abierto, 
                 *ya que el metodo buscarEstimaciones_solicitud primero agrega las estimaciones
                 *cerradas y luego agrega una abierta en caso de existir*/
                if (lista_estimaciones.get(lista_estimaciones.size() - 1).getSweCataByIdEstado().getNombre().toUpperCase().equals("ABIERTO")) {
                    solSeleccionada = lista_estimaciones.get(lista_estimaciones.size() - 1);
                    ajax_cargarEtapas();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "Existe una estimación abierta con esta solicitud. Por favor asegúrese de cerrar todas las estimaciones antes de aprobar alguna."));
                } else {
                    /* 
                     * A continuación verificamos que no se haya escogido como valida 
                     * alguna estimación. En caso de haberla se selecciona para que el usuario
                     * la identifique en la lista
                     */
                    for (int i = 0; i < lista_estimaciones.size(); i++) {
                        if (lista_estimaciones.get(i).getAprobada().toUpperCase().equals("S")) {
                            solSeleccionada = lista_estimaciones.get(i);
                            break;
                        }
                    }

                    if (solSeleccionada != null) {
                        ajax_cargarEtapas();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "Ya se ha aprobado una estimación con esta solicitud. No se puede aprobar otra estimación"));
                    }
                }
            } else {
                //RequestContext.getCurrentInstance().execute("Parametros.hide()"); 	
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No hay estimaciones asociadas con esa solicitud"));
            }
            //Si no se insertó un cód. de solicitud se buscan todas las estimaciones pendientes por aprobar
        //} else {
          //  lista_estimaciones = this.controller.buscarEstimaciones_solicitud(null);
       // }
    }

    public void aprobarEstimacion() {
        FacesContext context = FacesContext.getCurrentInstance();
        BeanVerEstimacion verEstimacion = (BeanVerEstimacion) context.getApplication().evaluateExpressionGet(context, "#{beanVerEstimacion}", Object.class);

        solSeleccionada = verEstimacion.getEstimacion_seleccionada();
        solSeleccionada.setAprobada("S");
        solSeleccionada.setFechaAprobacion(new Date());
        Object obj = this.controller.aprobarEstimacion(solSeleccionada);

        if (obj instanceof Boolean) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "La estimación ha sido aprobada con éxito"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo aprobar la estimación"));
        }
    }

    public void ajax_cargarEtapas() {
        lista_etapas = controller.buscar_EtapaSolicitud(solSeleccionada.getIdEstimacion());
        FacesContext context = FacesContext.getCurrentInstance();
        BeanVerEstimacion verEstimacion = (BeanVerEstimacion) context.getApplication().evaluateExpressionGet(context, "#{beanVerEstimacion}", Object.class);
        verEstimacion.ajax_mostrarEstimacion();
    }

    /**
     * @return the lista_estimaciones
     */
    public List<SweSoli> getLista_estimaciones() {
        return lista_estimaciones;
    }

    /**
     * @param lista_estimaciones the lista_estimaciones to set
     */
    public void setLista_estimaciones(List<SweSoli> lista_estimaciones) {
        this.lista_estimaciones = lista_estimaciones;
    }

    /**
     * @return the codigoSolicitud
     */
    public Integer getCodigoSolicitud() {
        return codigoSolicitud;
    }

    /**
     * @param codigoSolicitud the codigoSolicitud to set
     */
    public void setCodigoSolicitud(Integer codigoSolicitud) {
        this.codigoSolicitud = codigoSolicitud;
    }

    /**
     * @return the solSeleccionada
     */
    public SweSoli getSolSeleccionada() {
        return solSeleccionada;
    }

    /**
     * @param solSeleccionada the solSeleccionada to set
     */
    public void setSolSeleccionada(SweSoli solSeleccionada) {
        this.solSeleccionada = solSeleccionada;
    }

    /**
     * @return the lista_etapas
     */
    public List<SweEtapaXsol> getLista_etapas() {
        return lista_etapas;
    }

    /**
     * @param lista_etapas the lista_etapas to set
     */
    public void setLista_etapas(List<SweEtapaXsol> lista_etapas) {
        this.lista_etapas = lista_etapas;
    }
}

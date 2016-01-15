/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweSoli;
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
public class BeanAbrirEstimacion {

    private Controller controller;
    private List<SweSoli> lista_estimaciones;
    private Integer codigoSolicitud;
    private SweSoli solSeleccionada;
    
    public BeanAbrirEstimacion() {
        this.controller = new Controller();
    }

    public void buscarEstimaciones() {
        lista_estimaciones = this.controller.buscarEstimaciones_solicitud(codigoSolicitud);
        solSeleccionada = null;

        // Verificamos que haya al menos una estimación con esa solicitud
        if (!lista_estimaciones.isEmpty()) {

            /*Verificamos si el ultimo elemento de la lista esta en estado abierto, 
             *ya que el metodo buscarEstimaciones_solicitud primero agrega las estimaciones
             *cerradas y luego agrega una abierta en caso de existir*/
            if (lista_estimaciones.get(lista_estimaciones.size() - 1).getSweCataByIdEstado().getNombre().toUpperCase().equals("ABIERTO")) {
                solSeleccionada = lista_estimaciones.get(lista_estimaciones.size() - 1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia!", "Ya existe una estimación abierta con esta solicitud. No puede abrir otra estimación."));
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
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia!", "Ya se ha aprobado una estimación con esta solicitud. Ya no puede abir ninguna estimación"));
                }
            }
        } else {
//       RequestContext.getCurrentInstance().execute("Parametros.hide()"); 	
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No hay estimaciones asociadas con esa solicitud"));
        }


    }

    public void abrirEstimacion() {

        if (solSeleccionada!=null) {
            Object obj = this.controller.cambiarEstadoEstimacion(solSeleccionada, "Abierto");

            if (obj instanceof Boolean) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "La estimación " + solSeleccionada.getIdEstimacion() + " ha sido abierta"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La estimación " + solSeleccionada.getIdEstimacion() + " No se pudo abrir."));
            }

        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "Seleccione la estimación que desea abrir"));
        }
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
}

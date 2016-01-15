/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweFactCalXTsol;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanFactorCalibracion implements Serializable {

    private List<SweFactCalXTsol> lista_fCalibracion;
    private List<SweFactCalXTsol> historico_fCalibracion;
    private SweFactCalXTsol nuevoFactor;
    private final Controller controller;
    private List<SweCata> tiposSolicitud = new ArrayList<SweCata>();

    public BeanFactorCalibracion() {
        this.controller = new Controller();
        this.lista_fCalibracion = this.controller.listar_factorCalibracion_vigentes();
    }

    public void guardarFactor() {

        if (nuevoFactor.getValor().compareTo(BigDecimal.TEN) < 0) {
            Object obj = this.controller.agregar_factorCalibracion(nuevoFactor);

            if (obj instanceof Boolean) {
                lista_fCalibracion = this.controller.listar_factorCalibracion_vigentes();
                RequestContext.getCurrentInstance().execute("Factor_Calibracion.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Factor de calibración agregado con éxito"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo agregar el factor de calibración"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El valor debe ser menor que diez"));

        }

    }

    //Método para calcular factor de calibración
    public void calcularFactor() {
        String mensaje;
        //falta llamar al procedimiento almacenado
        mensaje = controller.calcFactCal();
        lista_fCalibracion = this.controller.listar_factorCalibracion_vigentes();
        if (mensaje.indexOf("Error") < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", mensaje));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", mensaje));
        }

    }

    public void consultar_historial(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            this.historico_fCalibracion = this.controller.historial_fCalibracion(((SweFactCalXTsol) event.getData()).getSweCata().getIdCatalogo());
        }
    }

    public void agregarTiposSolicitud() {
        this.nuevoFactor = new SweFactCalXTsol();
        this.nuevoFactor.setVigenciaInicial(Calendar.getInstance().getTime());
        this.nuevoFactor.setSweCata(new SweCata());

        if (tiposSolicitud.isEmpty()) {
            List<SweCata> catalogos = this.controller.listarPOR("Tipo Solicitud");
            for (int i = 0; i < catalogos.size(); i++) {
                this.tiposSolicitud.add(catalogos.get(i));

            }
        }
    }

    /**
     * @return the lista_fCalibracion
     */
    public List<SweFactCalXTsol> getLista_fCalibracion() {
        return lista_fCalibracion;
    }

    /**
     * @param lista_fCalibracion the lista_fCalibracion to set
     */
    public void setLista_fCalibracion(List<SweFactCalXTsol> lista_fCalibracion) {
        this.lista_fCalibracion = lista_fCalibracion;
    }

    /**
     * @return the historico_fCalibracion
     */
    public List<SweFactCalXTsol> getHistorico_fCalibracion() {
        return historico_fCalibracion;
    }

    /**
     * @param historico_fCalibracion the historico_fCalibracion to set
     */
    public void setHistorico_fCalibracion(List<SweFactCalXTsol> historico_fCalibracion) {
        this.historico_fCalibracion = historico_fCalibracion;
    }

    /**
     * @return the tiposSolicitud
     */
    public List<SweCata> getTiposSolicitud() {
        return tiposSolicitud;
    }

    /**
     * @param tiposSolicitud the tiposSolicitud to set
     */
    public void setTiposSolicitud(List<SweCata> tiposSolicitud) {
        this.tiposSolicitud = tiposSolicitud;
    }

    /**
     * @return the nuevoFactor
     */
    public SweFactCalXTsol getNuevoFactor() {
        return nuevoFactor;
    }

    /**
     * @param nuevoFactor the nuevoFactor to set
     */
    public void setNuevoFactor(SweFactCalXTsol nuevoFactor) {
        this.nuevoFactor = nuevoFactor;
    }
}

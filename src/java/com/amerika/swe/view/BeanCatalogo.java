/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.model.SweTipoCata;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author estudiante.soporte
 */
@ManagedBean
@ViewScoped
public class BeanCatalogo implements Serializable {

    private List<SweTipoCata> tipos_catalogo;
    private List<SweCata> catalogos;
    private SweTipoCata selected_TipoCatalogo;
    private SweTipoCata nuevoTipoCatalogo;
    private SweCata selected_Catalogo;
    private SweCata nuevoCatalogo;
    private boolean num_catalogos_tipo = false;
    private Controller controller;

    public BeanCatalogo() {
        this.controller = new Controller();
        this.tipos_catalogo = this.controller.listar_tipoCatalogos();
        this.catalogos = new ArrayList<SweCata>();
        this.nuevoCatalogo = new SweCata();
    }

    public void btnCrearTipoCatlogo() {
        nuevoTipoCatalogo.setEstado("A");
        Object obj = this.controller.agregar_tipoCatalogo(nuevoTipoCatalogo);

        if (obj instanceof Boolean) {
            this.tipos_catalogo = this.controller.listar_tipoCatalogos();
            RequestContext.getCurrentInstance().execute("DialogCrearTipoCata.hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipo Catalogo " + nuevoTipoCatalogo.getNombre() + " agregado con éxito", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el Tipo de Catalogo \nCausa: ", null));
        }
    }

    public void btnModificarTipoCatlogo() {
        Object obj = this.controller.editar_tipoCatalogo(nuevoTipoCatalogo);

        if (obj instanceof Boolean) {
            this.tipos_catalogo = this.controller.listar_tipoCatalogos();
            RequestContext.getCurrentInstance().execute("DialogModificarTipoCata.hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tipo Catalogo " + nuevoTipoCatalogo.getNombre() + " se actualizó con éxito", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo actualizar el Tipo de Catalogo \nCausa: " + ((HibernateException) obj).getMessage(), null));
        }
    }

    public void btnEliminarTipoCatlogo() {

        if (this.controller.num_catalogos_tipo(selected_TipoCatalogo.getCodigoTipo()) != 0 ) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El Tipo de Catalogo " + selected_TipoCatalogo.getNombre() + " tiene registros asociados activos"));
        } else {
            selected_TipoCatalogo.setEstado("I");
            Object obj = this.controller.editar_tipoCatalogo(selected_TipoCatalogo);
            if (obj instanceof Boolean) {
                this.tipos_catalogo.remove(selected_TipoCatalogo);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "El Tipo de Catalogo " + selected_TipoCatalogo.getNombre() + " ha sido eliminado"));
            } else {
                String mensaje = "" + ((SQLException) obj).getCause();
                if (mensaje.indexOf("SWEAMERIKA.FK_CATALOGO_TIPO") > -1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El Tipo de Catalogo " + selected_TipoCatalogo.getNombre() + " no se pudo eliminar ", "Causa: Se ha encontrado un registro asociado al Tipo de catalogo"));
                }
            }
        }
    }

    public void crearCatalogo() {

        if (selected_TipoCatalogo.getMostrarEstado().equals("N")) {
            nuevoCatalogo.setEstado(null);
        }
        if (selected_TipoCatalogo.getMostrarValor().equals("N")) {
            nuevoCatalogo.setValor(BigDecimal.ZERO);
        }        
        if (nuevoCatalogo.getValor().compareTo(BigDecimal.TEN) >= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El valor debe ser menor que 10 "));
        } else {
            Object obj = this.controller.agregar_catalogo(nuevoCatalogo);//   editar_tipoCatalogo(selected_TipoCatalogo);

            if (obj instanceof Boolean) {
                onRowSelect();
                RequestContext.getCurrentInstance().execute("DialogCata.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Catalogo " + nuevoCatalogo.getNombre() + " agregado con éxito", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el Catalogo \nCausa: ", null));
            }
        }
    }

    public void modificarCatalogo() {
        
        if (nuevoCatalogo.getValor().compareTo(BigDecimal.TEN) >= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El valor debe ser menor que 10 "));
        } else {
            Object obj = this.controller.editar_catalogo(nuevoCatalogo);

            if (obj instanceof Boolean) {
                onRowSelect();
                RequestContext.getCurrentInstance().execute("DialogModificarCatalogo.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El Catalogo " + nuevoCatalogo.getNombre() + " se actualizó con éxito", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo actualizar el Catalogo \nCausa: ", null));
            }
        }
    }

    public void eliminarCatalogo() {

        selected_Catalogo.setEstado("I");
        Object obj = this.controller.editar_catalogo(selected_Catalogo);

        if (obj instanceof Boolean) {
            this.catalogos.remove(selected_Catalogo);
            RequestContext.getCurrentInstance().execute("DialogEliminarCata.hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El Catalogo " + selected_Catalogo.getNombre() + " ha sido eliminado", null));
            selected_Catalogo = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo deshabilitar el catalogo \nCausa: ", null));
        }
    }

    /*Se cargan los catalogos dependiendo del tipo de catalogo seleccionado*/
    public void onRowSelect() {
        this.catalogos.clear();
        this.catalogos = this.controller.listar_catalogos(selected_TipoCatalogo.getCodigoTipo());
    }

    /*Este metodo es incado cada vez que se quiere editar un Tipo de catalogo
     o Catalogo maestro.*/
    public void num_catalogos(SweTipoCata tipoCata) {        
        num_catalogos_tipo = (Long) this.controller.num_catalogos_tipo(tipoCata.getCodigoTipo()) > 0;
        nuevoTipoCatalogo = new SweTipoCata();
        nuevoTipoCatalogo.setCodigoTipo(tipoCata.getCodigoTipo()); 
        nuevoTipoCatalogo.setDescripcion(tipoCata.getDescripcion());
        nuevoTipoCatalogo.setEstado(tipoCata.getEstado()); 
        nuevoTipoCatalogo.setMostrarEstado(tipoCata.getMostrarEstado()); 
        nuevoTipoCatalogo.setMostrarValor(tipoCata.getMostrarValor());
        nuevoTipoCatalogo.setNombre(tipoCata.getNombre());         
    }

    public void inicializar() {
        this.nuevoTipoCatalogo = new SweTipoCata();
    }

    public void inicializarCatalogo() {

        nuevoCatalogo = new SweCata();
        nuevoCatalogo.setSweTipoCata(selected_TipoCatalogo);
        RequestContext.getCurrentInstance().execute("DialogCata.show()");
    }

    public void btnActualizar_catalogo(SweCata c){
        nuevoCatalogo = new SweCata();
        nuevoCatalogo.setIdCatalogo(c.getIdCatalogo());
        nuevoCatalogo.setNombre(c.getNombre());
        nuevoCatalogo.setDescripcion(c.getDescripcion());
        nuevoCatalogo.setEstado(c.getEstado());         
        nuevoCatalogo.setValor(c.getValor()); 
        nuevoCatalogo.setSweTipoCata(c.getSweTipoCata()); 
    }

    /**
     * @return the tipos_catalogo
     */
    public List<SweTipoCata> getTipos_catalogo() {
        return tipos_catalogo;
    }

    /**
     * @param tipos_catalogo the tipos_catalogo to set
     */
    public void setTipos_catalogo(List<SweTipoCata> tipos_catalogo) {
        this.tipos_catalogo = tipos_catalogo;
    }

    /**
     * @return the selected_TipoCatalogo
     */
    public SweTipoCata getSelected_TipoCatalogo() {
        return selected_TipoCatalogo;
    }

    /**
     * @param selected_TipoCatalogo the selected_TipoCatalogo to set
     */
    public void setSelected_TipoCatalogo(SweTipoCata selected_TipoCatalogo) {
        this.selected_TipoCatalogo = selected_TipoCatalogo;
    }

    /**
     * @return the num_catalogos_tipo
     */
    public boolean isNum_catalogos_tipo() {
        return num_catalogos_tipo;
    }

    /**
     * @param num_catalogos_tipo the num_catalogos_tipo to set
     */
    public void setNum_catalogos_tipo(boolean num_catalogos_tipo) {
        this.num_catalogos_tipo = num_catalogos_tipo;
    }

    /**
     * @return the catalogos
     */
    public List<SweCata> getCatalogos() {
        return catalogos;
    }

    /**
     * @param catalogos the catalogos to set
     */
    public void setCatalogos(List<SweCata> catalogos) {
        this.catalogos = catalogos;
    }

    /**
     * @return the selected_Catalogo
     */
    public SweCata getSelected_Catalogo() {
        return selected_Catalogo;
    }

    /**
     * @param selected_Catalogo the selected_Catalogo to set
     */
    public void setSelected_Catalogo(SweCata selected_Catalogo) {
        this.selected_Catalogo = selected_Catalogo;
    }

    /**
     * @return the nuevoTipoCatalogo
     */
    public SweTipoCata getNuevoTipoCatalogo() {
        return nuevoTipoCatalogo;
    }

    /**
     * @param nuevoTipoCatalogo the nuevoTipoCatalogo to set
     */
    public void setNuevoTipoCatalogo(SweTipoCata nuevoTipoCatalogo) {
        this.nuevoTipoCatalogo = nuevoTipoCatalogo;
    }

    /**
     * @return the nuevoCatalogo
     */
    public SweCata getNuevoCatalogo() {
        return nuevoCatalogo;
    }

    /**
     * @param nuevoCatalogo the nuevoCatalogo to set
     */
    public void setNuevoCatalogo(SweCata nuevoCatalogo) {
        this.nuevoCatalogo = nuevoCatalogo;
    }
}
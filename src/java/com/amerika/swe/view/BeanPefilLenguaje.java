/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweDefperfAsociahrXleng;
import com.amerika.swe.model.util.TablaPefilxLenguaje;
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

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanPefilLenguaje implements Serializable {

    private Controller controller;
    private List<TablaPefilxLenguaje> list_perfilLenguaje;
    private List<SweDefperfAsociahrXleng> list_perXleng;
    
    private SweDefperfAsociahrXleng nuevo_perXlen;
    private SweDefperfAsociahrXleng seleccionado;
    private List<SweCata> lenguajes;
    private List<SweCata> lenguajes_historial;
    private List<SweCata> perfiles;
    private List<SweDefperfAsociahrXleng> historico;
    
    private int idLenguajeSeleccionado;
    
    public BeanPefilLenguaje() {
        this.controller = new Controller();
        this.list_perfilLenguaje = this.controller.listarVigentes_perfilXlenguaje();
        this.lenguajes_historial = this.controller.listarPOR("Lenguaje");
        list_perXleng = new ArrayList<SweDefperfAsociahrXleng>();
    }

    public void inicializar() {
        
        SweCata lenguaje = new SweCata();
        SweCata perfil = new SweCata();
        
        lenguaje.setIdCatalogo(idLenguajeSeleccionado);    
        if(seleccionado != null ){
            perfil.setIdCatalogo(seleccionado.getSweCataByIdPerfil().getIdCatalogo());
        }
       
        this.nuevo_perXlen = new SweDefperfAsociahrXleng();
        nuevo_perXlen.setVigenciaInicial(Calendar.getInstance().getTime());
        nuevo_perXlen.setSweCataByIdLenguaje(lenguaje); 
        nuevo_perXlen.setSweCataByIdPerfil(perfil);
        
        if(this.lenguajes == null && this.perfiles == null){
            this.perfiles = this.controller.listarPOR("Perfiles");
            this.lenguajes = this.controller.listarPOR("Lenguaje");
        }  	
    }

    public void guardar() {
        
        if(idLenguajeSeleccionado==0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un lenguaje"));
        }else if(nuevo_perXlen.getSweCataByIdPerfil().getIdCatalogo()==0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un perfil"));
        }else if(nuevo_perXlen.getHoras()==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Digite la cantidad de horas"));
        }else if(nuevo_perXlen.getHoras().compareTo(BigDecimal.ZERO)<=0 ){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La cantidad de horas debe ser mayor que cero"));
        }else{
            if (nuevo_perXlen.getHoras().compareTo(BigDecimal.valueOf(1000)) < 0) {
                if (nuevo_perXlen.getHoras().scale() <= 2) {
                    Object obj = this.controller.agregar_perfilXlenguaje(nuevo_perXlen);

                    if (obj instanceof Boolean) {
                        seleccionado = null;
                        this.list_perfilLenguaje = this.controller.listarVigentes_perfilXlenguaje(); 
                        this.list_perXleng.clear();
                        this.list_perXleng = this.controller.listarVigentes_perfilXlenguaje(idLenguajeSeleccionado);
                        RequestContext.getCurrentInstance().execute("dialog.hide()");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "El registro se completó con éxito"));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo hacer el registro"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Digite un valor con dos decimales máximo"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Digite un valor menor que 1000"));
            }
        }
    }

    
    public void lenguaje_seleccinado(){
        seleccionado = null;
        this.list_perXleng.clear();
       if(idLenguajeSeleccionado != 0){
           this.list_perXleng = this.controller.listarVigentes_perfilXlenguaje(idLenguajeSeleccionado);
       }
    }
    
    
    public void historial(){
        this.historico = this.controller.historial_perfilXlenguaje(seleccionado.getSweCataByIdLenguaje().getIdCatalogo(), seleccionado.getSweCataByIdPerfil().getIdCatalogo());    
    }
    
    /**
     * @return the list_perfilLenguaje
     */
    public List<TablaPefilxLenguaje> getList_perfilLenguaje() {
        return list_perfilLenguaje;
    }

    /**
     * @param list_perfilLenguaje the list_perfilLenguaje to set
     */
    public void setList_perfilLenguaje(List<TablaPefilxLenguaje> list_perfilLenguaje) {
        this.list_perfilLenguaje = list_perfilLenguaje;
    }

    /**
     * @return the nuevo_perXlen
     */
    public SweDefperfAsociahrXleng getNuevo_perXlen() {
        return nuevo_perXlen;
    }

    /**
     * @param nuevo_perXlen the nuevo_perXlen to set
     */
    public void setNuevo_perXlen(SweDefperfAsociahrXleng nuevo_perXlen) {
        this.nuevo_perXlen = nuevo_perXlen;
    }

    /**
     * @return the lenguajes
     */
    public List<SweCata> getLenguajes() {
        return lenguajes;
    }

    /**
     * @param lenguajes the lenguajes to set
     */
    public void setLenguajes(List<SweCata> lenguajes) {
        this.lenguajes = lenguajes;
    }

    /**
     * @return the perfiles
     */
    public List<SweCata> getPerfiles() {
        return perfiles;
    }

    /**
     * @param perfiles the perfiles to set
     */
    public void setPerfiles(List<SweCata> perfiles) {
        this.perfiles = perfiles;
    }

    /**
     * @return the idLenguajeSeleccionado
     */
    public int getIdLenguajeSeleccionado() {
        return idLenguajeSeleccionado;
    }

    /**
     * @param idLenguajeSeleccionado the idLenguajeSeleccionado to set
     */
    public void setIdLenguajeSeleccionado(int idLenguajeSeleccionado) {
        this.idLenguajeSeleccionado = idLenguajeSeleccionado;
    }

    /**
     * @return the list_perXleng
     */
    public List<SweDefperfAsociahrXleng> getList_perXleng() {
        return list_perXleng;
    }

    /**
     * @param list_perXleng the list_perXleng to set
     */
    public void setList_perXleng(List<SweDefperfAsociahrXleng> list_perXleng) {
        this.list_perXleng = list_perXleng;
    }

    /**
     * @return the seleccionado
     */
    public SweDefperfAsociahrXleng getSeleccionado() {
        return seleccionado;
    }

    /**
     * @param seleccionado the seleccionado to set
     */
    public void setSeleccionado(SweDefperfAsociahrXleng seleccionado) {
        this.seleccionado = seleccionado;
    }

    /**
     * @return the lenguajes_historial
     */
    public List<SweCata> getLenguajes_historial() {
        return lenguajes_historial;
    }

    /**
     * @param lenguajes_historial the lenguajes_historial to set
     */
    public void setLenguajes_historial(List<SweCata> lenguajes_historial) {
        this.lenguajes_historial = lenguajes_historial;
    }

    /**
     * @return the historico
     */
    public List<SweDefperfAsociahrXleng> getHistorico() {
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(List<SweDefperfAsociahrXleng> historico) {
        this.historico = historico;
    }



}
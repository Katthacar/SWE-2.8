/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;


import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweTcompXComp;
import java.io.Serializable;
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
public class BeanCompl_Comp implements Serializable {

    private Controller controller;
    private List<SweCata> list_complejidad;
    private List<SweTcompXComp> list_complXcomp;  
    
    
    private List<SweCata> list_componentes;
    private SweTcompXComp nevo_TcompXComp;
    
    private List<SweTcompXComp> historico;
    private SweTcompXComp seleccionado;
    private Integer complejidadSeleccionada;
    
    public BeanCompl_Comp() {
        
        this.controller = new Controller();
        complejidadSeleccionada = -1;       
        this.list_complejidad = this.controller.listarPOR("Complejidad");

    }

    /**
     * Este método se hace con el proposito de agrupar los  
     * tipos de componentes por complejidad para ser mostrados
     * en la dataTable
     */

    public void complejidad_seleccinada(){
        seleccionado = null;
       if(getComplejidadSeleccionada() == -1){
           this.list_complXcomp = new ArrayList<SweTcompXComp>();
       }else{
           this.list_complXcomp = this.controller.listar_TipoCompXCompl(complejidadSeleccionada);
       }
    }
    
    
    public void inicializar(){
        nevo_TcompXComp = new SweTcompXComp();
        nevo_TcompXComp.setVigenciaInicial(Calendar.getInstance().getTime()); 
        nevo_TcompXComp.setSweCataByIdTpocomplejidad(new SweCata());
        nevo_TcompXComp.setSweCataByIdTpocomponente(new SweCata()); 
        nevo_TcompXComp.getSweCataByIdTpocomplejidad().setIdCatalogo(getComplejidadSeleccionada());
       
        if(list_complejidad==null || list_componentes==null){
            this.list_complejidad = this.controller.listarPOR("Complejidad");
            this.list_componentes = this.controller.listarPOR("Tipo De Componente");
        }
        
    }
    
    
    public void guardar(){
    
        if(nevo_TcompXComp.getSweCataByIdTpocomplejidad().getIdCatalogo() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un tipo de complejidad"));
        }else if(nevo_TcompXComp.getSweCataByIdTpocomponente().getIdCatalogo() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un componente"));
        }else{
            Object obj = this.controller.agegar_TipoComponenteXComplejidad(nevo_TcompXComp);

            if(obj instanceof Boolean){
                this.list_complXcomp = this.controller.listar_TipoCompXCompl(getComplejidadSeleccionada());
                seleccionado = null;
                RequestContext.getCurrentInstance().execute("dialog.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Se ha registrado un nuevo componente por complejidad"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se puedo agregar el registro"));
            }
        }
    }
    
   
    public void consultar_historial(){
        this.historico = this.controller.historial_TipoComponenteXComplejidad(seleccionado.getSweCataByIdTpocomplejidad().getIdCatalogo(), seleccionado.getSweCataByIdTpocomponente().getIdCatalogo());        
    }


    /**
     * @return the list_complejidad
     */
    public List<SweCata> getList_complejidad() {
        return list_complejidad;
    }

    /**
     * @param list_complejidad the list_complejidad to set
     */
    public void setList_complejidad(List<SweCata> list_complejidad) {
        this.list_complejidad = list_complejidad;
    }

    /**
     * @return the list_componentes
     */
    public List<SweCata> getList_componentes() {
        return list_componentes;
    }

    /**
     * @param list_componentes the list_componentes to set
     */
    public void setList_componentes(List<SweCata> list_componentes) {
        this.list_componentes = list_componentes;
    }

    /**
     * @return the nevo_TcompXComp
     */
    public SweTcompXComp getNevo_TcompXComp() {
        return nevo_TcompXComp;
    }

    /**
     * @param nevo_TcompXComp the nevo_TcompXComp to set
     */
    public void setNevo_TcompXComp(SweTcompXComp nevo_TcompXComp) {
        this.nevo_TcompXComp = nevo_TcompXComp;
    }

    /**
     * @return the historico
     */
    public List<SweTcompXComp> getHistorico() {
        return historico;
    }

    /**
     * @param historico the historico to set
     */
    public void setHistorico(List<SweTcompXComp> historico) {
        this.historico = historico;
    }

    /**
     * @return the seleccionado
     */
    public SweTcompXComp getSeleccionado() {
        return seleccionado;
    }

    /**
     * @param seleccionado the seleccionado to set
     */
    public void setSeleccionado(SweTcompXComp seleccionado) {
        this.seleccionado = seleccionado;
    }

    /**
     * @return the list_complXcomp
     */
    public List<SweTcompXComp> getList_complXcomp() {
        return list_complXcomp;
    }

    /**
     * @param list_complXcomp the list_complXcomp to set
     */
    public void setList_complXcomp(List<SweTcompXComp> list_complXcomp) {
        this.list_complXcomp = list_complXcomp;
    }

    /**
     * @return the complejidadSeleccionada
     */
    public Integer getComplejidadSeleccionada() {
        return complejidadSeleccionada;
    }

    /**
     * @param complejidadSeleccionada the complejidadSeleccionada to set
     */
    public void setComplejidadSeleccionada(Integer complejidadSeleccionada) {
        this.complejidadSeleccionada = complejidadSeleccionada;
    }

  
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweTipoApliXFtec;
import com.amerika.swe.model.SweTipoApliXFtecId;
import java.io.Serializable;
import java.util.ArrayList;
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
public class BeanTipoApliXFtec implements Serializable {

    private Controller controller;
//    private List<SweTipoApliXFtec> lista_TipoApliXFtec;
    private List<SweTipoApliXFtec> list_DataTable;
    
    private SweTipoApliXFtec nuevo_SweTipoApliXFtec;    
    private List<SweCata> tipo_aplicaciones;
    private List<SweCata> list_aplicaciones;
    private List<SweCata> factores_tecnicos;
    private List<Integer> influencias;
    
    private SweTipoApliXFtec sweTipoApliXFtec_selected;
    private int id_TpApSeleccionada;
    
    public BeanTipoApliXFtec() {
        
        this.controller = new Controller();
//        this.lista_TipoApliXFtec = this.controller.listar_TipoApliXFactec();
        list_aplicaciones = this.controller.listarPOR("Tipo Aplicación");
        list_DataTable = new ArrayList<SweTipoApliXFtec>();
    }

    /*Este metodo se ejecuta cuando se presiona el boton  Agregar nuevo
     con la intención de inicializar el objeto nuevo_SweTipoApliXFtec */
    public void inicializar(){
        SweCata aplicacion = new SweCata();
        aplicacion.setIdCatalogo(id_TpApSeleccionada);
        
        this.nuevo_SweTipoApliXFtec = new SweTipoApliXFtec();
        this.nuevo_SweTipoApliXFtec.setSweCataByCodigoFacttecnico(new SweCata());
        this.nuevo_SweTipoApliXFtec.setSweCataByCodigoTpoaplicacion(aplicacion); 
        
        if(! (tipo_aplicaciones instanceof List)){
            tipo_aplicaciones = list_aplicaciones;
            factores_tecnicos = this.controller.listarPOR("Factor Técnico");
            influencias = new ArrayList<Integer>(5);

            for (int i = 0; i <= 5; i++) {
                influencias.add(i); 
            }
        }        
    }
    
    public void guardar(){
        if(this.nuevo_SweTipoApliXFtec.getSweCataByCodigoTpoaplicacion().getIdCatalogo() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un tipo de aplicación"));
        }else if(this.nuevo_SweTipoApliXFtec.getSweCataByCodigoFacttecnico().getIdCatalogo() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un factor técnico"));
        }else if(this.nuevo_SweTipoApliXFtec.getGradoInfluencia() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un grado de influencia"));
        }else{
            
            nuevo_SweTipoApliXFtec.setId(new SweTipoApliXFtecId(nuevo_SweTipoApliXFtec.getSweCataByCodigoFacttecnico().getIdCatalogo(), nuevo_SweTipoApliXFtec.getSweCataByCodigoTpoaplicacion().getIdCatalogo()));
            Object obj = this.controller.agregar_TipoApliXFacTec(nuevo_SweTipoApliXFtec);
            
            if(obj instanceof Boolean){
//                this.lista_TipoApliXFtec.clear();
//                this.lista_TipoApliXFtec = this.controller.listar_TipoApliXFactec();
                ajax_AplSeleccionada();
                RequestContext.getCurrentInstance().execute("dialog.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Registro agregado con éxito"));
            }else{                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo agregar el registro"));
            }
        }
        
    }
    
    public void modificar(){
        if(nuevo_SweTipoApliXFtec.getGradoInfluencia() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un grado de influencia"));
        }else{
            Object obj = this.controller.editar_TipoApliXFactec(nuevo_SweTipoApliXFtec);            
            if(obj instanceof Boolean){
                list_DataTable.remove(sweTipoApliXFtec_selected); 
                list_DataTable.add(nuevo_SweTipoApliXFtec); 
//                this.lista_TipoApliXFtec = this.controller.listar_TipoApliXFactec();
                RequestContext.getCurrentInstance().execute("dialog_editar.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Registro editado con éxito"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo editar el registro"));
            }
        }
        
    }
    
    
    public void btn_iniciarModificacion(){     
        
        nuevo_SweTipoApliXFtec = new SweTipoApliXFtec();
        nuevo_SweTipoApliXFtec.setGradoInfluencia(sweTipoApliXFtec_selected.getGradoInfluencia());   
        nuevo_SweTipoApliXFtec.setId(sweTipoApliXFtec_selected.getId());         
        nuevo_SweTipoApliXFtec.setSweCataByCodigoFacttecnico(sweTipoApliXFtec_selected.getSweCataByCodigoFacttecnico()); 
        nuevo_SweTipoApliXFtec.setSweCataByCodigoTpoaplicacion(sweTipoApliXFtec_selected.getSweCataByCodigoTpoaplicacion());
        
        if(! (tipo_aplicaciones instanceof List)){
            tipo_aplicaciones = list_aplicaciones;
            factores_tecnicos = this.controller.listarPOR("Factor Técnico");
            influencias = new ArrayList<Integer>(5);

            for (int i = 1; i <= 5; i++) {
                influencias.add(i); 
            }
        }  
    }
    
    
    /*Este metodo se ejecuta al seleccionar un elemento de la lista
     de tipo de aplicaiones*/
    public void ajax_AplSeleccionada(){
        list_DataTable.clear();
        sweTipoApliXFtec_selected = null;
        if(id_TpApSeleccionada>0){        
            list_DataTable = this.controller.listar_TipoApliXFactec(id_TpApSeleccionada);
//            for (int i = 0; i < lista_TipoApliXFtec.size(); i++) {
//                if (id_TpApSeleccionada == lista_TipoApliXFtec.get(i).getSweCataByCodigoTpoaplicacion().getIdCatalogo()) {
//                    list_DataTable.add(lista_TipoApliXFtec.get(i));
//                }            
//            }
        }
    }
    
    
    /**
     * @return the lista_TipoApliXFtec
     */
//    public List<SweTipoApliXFtec> getLista_TipoApliXFtec() {
//        return lista_TipoApliXFtec;
//    }
//
//    /**
//     * @param lista_TipoApliXFtec the lista_TipoApliXFtec to set
//     */
//    public void setLista_TipoApliXFtec(List<SweTipoApliXFtec> lista_TipoApliXFtec) {
//        this.lista_TipoApliXFtec = lista_TipoApliXFtec;
//    }

    /**
     * @return the nuevo_SweTipoApliXFtec
     */
    public SweTipoApliXFtec getNuevo_SweTipoApliXFtec() {
        return nuevo_SweTipoApliXFtec;
    }

    /**
     * @param nuevo_SweTipoApliXFtec the nuevo_SweTipoApliXFtec to set
     */
    public void setNuevo_SweTipoApliXFtec(SweTipoApliXFtec nuevo_SweTipoApliXFtec) {
        this.nuevo_SweTipoApliXFtec = nuevo_SweTipoApliXFtec;
    }

    /**
     * @return the factores_tecnicos
     */
    public List<SweCata> getFactores_tecnicos() {
        return factores_tecnicos;
    }

    /**
     * @param factores_tecnicos the factores_tecnicos to set
     */
    public void setFactores_tecnicos(List<SweCata> factores_tecnicos) {
        this.factores_tecnicos = factores_tecnicos;
    }

    /**
     * @return the tipo_aplicaciones
     */
    public List<SweCata> getTipo_aplicaciones() {
        return tipo_aplicaciones;
    }

    /**
     * @param tipo_aplicaciones the tipo_aplicaciones to set
     */
    public void setTipo_aplicaciones(List<SweCata> tipo_aplicaciones) {
        this.tipo_aplicaciones = tipo_aplicaciones;
    }

    /**
     * @return the influencias
     */
    public List<Integer> getInfluencias() {
        return influencias;
    }

    /**
     * @param influencias the influencias to set
     */
    public void setInfluencias(List<Integer> influencias) {
        this.influencias = influencias;
    }

    /**
     * @return the sweTipoApliXFtec_selected
     */
    public SweTipoApliXFtec getSweTipoApliXFtec_selected() {
        return sweTipoApliXFtec_selected;
    }

    /**
     * @param sweTipoApliXFtec_selected the sweTipoApliXFtec_selected to set
     */
    public void setSweTipoApliXFtec_selected(SweTipoApliXFtec sweTipoApliXFtec_selected) {
        this.sweTipoApliXFtec_selected = sweTipoApliXFtec_selected;
    }

    /**
     * @return the id_TpApSeleccionada
     */
    public int getId_TpApSeleccionada() {
        return id_TpApSeleccionada;
    }

    /**
     * @param id_TpApSeleccionada the id_TpApSeleccionada to set
     */
    public void setId_TpApSeleccionada(int id_TpApSeleccionada) {
        this.id_TpApSeleccionada = id_TpApSeleccionada;
    }

    /**
     * @return the list_aplicaciones
     */
    public List<SweCata> getList_aplicaciones() {
        return list_aplicaciones;
    }

    /**
     * @param list_aplicaciones the list_aplicaciones to set
     */
    public void setList_aplicaciones(List<SweCata> list_aplicaciones) {
        this.list_aplicaciones = list_aplicaciones;
    }

    /**
     * @return the list_DataTable
     */
    public List<SweTipoApliXFtec> getList_DataTable() {
        return list_DataTable;
    }

    /**
     * @param list_DataTable the list_DataTable to set
     */
    public void setList_DataTable(List<SweTipoApliXFtec> list_DataTable) {
        this.list_DataTable = list_DataTable;
    }
}

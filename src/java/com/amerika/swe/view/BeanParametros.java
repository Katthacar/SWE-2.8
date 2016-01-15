/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweParam;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class BeanParametros implements Serializable {

    private List<SweParam> parametros;
    private SweParam parametro_seleccionado;
    private SweParam parametro_nuevo;
    
    private Controller controller;
    
    /*Esta bandera sirve para ocultar o mostrar 
     * los furmularios de registro y edición de 
     * los parámetros*/
    private boolean btn = false; 
    
    
    public BeanParametros() {
        this.controller = new Controller();
        this.parametros = this.controller.listar_parametros();
        
    }

    public void BtnCrearParametro(){
       
        if (parametro_nuevo.getValor().compareTo(BigDecimal.valueOf(10000.00))>=0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El campo valor debe contener una cifra menor que diez mil"));            
        }else{
            Object obj = this.controller.agregar_parametro(parametro_nuevo);       
            if(obj instanceof Boolean){
                this.parametros.add(parametro_nuevo); 
                btn = true;
                RequestContext.getCurrentInstance().execute("Parametros.hide()"); 	
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!","El Parametro "+parametro_nuevo.getNombre()+" ha sido creado con éxito"));            
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","No se pudo agregar el Parametro con éxito"));            
            }
        }
    }
    
    public void btnModificarParametro(){
        
        if (parametro_nuevo.getValor().compareTo(BigDecimal.valueOf(10000.00))>=0) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El campo valor debe contener una cifra menor que diez mil"));            
        }else{
            Object obj = this.controller.editar_parametro(parametro_nuevo);
            if(obj instanceof Boolean){          
                btn = true;
                this.parametros.remove(parametro_seleccionado); //Removemos el parametro viejo
                this.parametros.add(parametro_nuevo);           // Agregamos el parametro editado
                RequestContext.getCurrentInstance().execute("param.hide()");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!","El Parametro "+parametro_nuevo.getNombre()+" ha sido editado con éxito"));            
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","No se pudo editar el Parametro"));            
            }
        }
    }
    
    public void btnEliminarParametro(){
        Object obj = this.controller.eliminar_parametro(parametro_seleccionado);
        
        if(obj instanceof Boolean){      
            this.parametros.remove(parametro_seleccionado);
            RequestContext.getCurrentInstance().execute("DialogEliminarParam.hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!","El Parametro "+parametro_seleccionado.getNombre()+" ha sido eliminado"));            
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!","No se pudo eliminar el Parametro"));            
        }
    
    }
    
    
    public void btn_iniciarModificacion(SweParam p){
        btn = false;
        parametro_nuevo = new SweParam();  
        
        parametro_nuevo.setCodigoParam(p.getCodigoParam());
        parametro_nuevo.setNombre(p.getNombre()); 
        parametro_nuevo.setDescripcion(p.getDescripcion());
        parametro_nuevo.setLongitud(p.getLongitud());         
        parametro_nuevo.setValor(p.getValor()); 
        parametro_nuevo.setTipo(p.getTipo()); 
    }
    
    
    public void inicializar(){
        parametro_nuevo = new SweParam();  
        btn = false;
    }
    /**
     * @return the parametros
     */
    public List<SweParam> getParametros() {
        return parametros;
    }

    /**
     * @param parametros the parametros to set
     */
    public void setParametros(List<SweParam> parametros) {
        this.parametros = parametros;
    }

    /**
     * @return the parametro_seleccionado
     */
    public SweParam getParametro_seleccionado() {
        return parametro_seleccionado;
    }

    /**
     * @param parametro_seleccionado the parametro_seleccionado to set
     */
    public void setParametro_seleccionado(SweParam parametro_seleccionado) {
        this.parametro_seleccionado = parametro_seleccionado;
    }

    /**
     * @return the parametro_nuevo
     */
    public SweParam getParametro_nuevo() {
        return parametro_nuevo;
    }

    /**
     * @param parametro_nuevo the parametro_nuevo to set
     */
    public void setParametro_nuevo(SweParam parametro_nuevo) {
        this.parametro_nuevo = parametro_nuevo;
    }

    /**
     * @return the btn
     */
    public boolean isBtn() {
        return btn;
    }

    /**
     * @param btn the btn to set
     */
    public void setBtn(boolean btn) {
        this.btn = btn;
    }
}

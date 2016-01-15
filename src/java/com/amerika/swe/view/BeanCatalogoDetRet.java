/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCataDetret;
import com.amerika.swe.model.SweTipoDetret;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanCatalogoDetRet implements Serializable {

    private Controller controller;
    private List<SweCata> list_componentes;
    private List<SweTipoDetret> list_tipoDetRet;
    private List<SweTipoDetret> list_tipoDetRetXtipoComp; // Estos valores se muestran en el SelectOneMenu
    private SweCata componente_seleccionado;
    private Integer idComponente_seleccionado;
    private SweTipoDetret tipoDetRet_seleccionado;
    /*Estos van a ser los registros de la DB sin modificaciones 
     * y que no se van a mostrar con esta lista*/
    private List<SweCataDetret> list_Originales_CataDetRet;
//    /* Esta lista va a tener los datos de la lista list_Originales_CataDetRet 
//     * mas los nuevos que se agreguen. Esto datos serán mostrados en un DataTable*/
//    /*Este atributo permite bloquear o desbloar el DataTable*/
    private boolean btnAgregarFila = false;
    private boolean disable_btnGuardar = false;
//    private List<Integer> indices_modificados = new ArrayList<Integer>();

    public BeanCatalogoDetRet() {
        this.controller = new Controller();
        list_componentes = this.controller.listarPOR("Tipo De Componente");
        list_tipoDetRet = this.controller.listar_DetRet();

        list_tipoDetRetXtipoComp = new ArrayList<SweTipoDetret>();
        componente_seleccionado = new SweCata();
        tipoDetRet_seleccionado = new SweTipoDetret();
        list_Originales_CataDetRet = new ArrayList<SweCataDetret>();
    }

    public void registrarIntervalos() {        
        Object obj = this.controller.agregar_catalogoDetRet(list_Originales_CataDetRet);

        if (obj instanceof Boolean) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Intervalos registrados con éxito"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo hacer el registro de los intervalos"));
        }
    }

    public void ajaxSelectedComponente() {

        list_tipoDetRetXtipoComp.clear();
        list_Originales_CataDetRet.clear();
        tipoDetRet_seleccionado = new SweTipoDetret();
        btnAgregarFila = false;
        if (idComponente_seleccionado != -1) {
            for (int i = 0; i < list_componentes.size(); i++) {
                if (list_componentes.get(i).getIdCatalogo() == idComponente_seleccionado) {
                    componente_seleccionado = list_componentes.get(i);
                    break;
                }
            }

            for (int i = 0; i < list_tipoDetRet.size(); i++) {

                if (componente_seleccionado.getNombre().equals("ILF") || componente_seleccionado.getNombre().equals("EIF")) {
                    if (list_tipoDetRet.get(i).getNombre().equals("DET") || list_tipoDetRet.get(i).getNombre().equals("RET")) {
                        this.list_tipoDetRetXtipoComp.add(list_tipoDetRet.get(i));
                    }
                } else {
                    if (list_tipoDetRet.get(i).getNombre().equals("DET") || list_tipoDetRet.get(i).getNombre().equals("FTR")) {
                        this.list_tipoDetRetXtipoComp.add(list_tipoDetRet.get(i));
                    }
                }
            }      
            
        }
    }

    /*Una vez seleccionado el DET, RET O FRT se consultan
     los valores en la DB, se crea una copia de los valores para conservar
     los originales*/
    public void ajaxSelectedTipoDetRet() {
        list_Originales_CataDetRet.clear();
        btnAgregarFila = false;
        if(tipoDetRet_seleccionado.getCodigoTipoDetret()!=-1){
            list_Originales_CataDetRet = this.controller.listar(idComponente_seleccionado, tipoDetRet_seleccionado.getCodigoTipoDetret());
            btnAgregarFila = true;
        }
        
    }

    /*Agegamos una nueva fila al DataTable que muestra y registra los
     intervalos*/
    public void agregarFilaDataTable() {
        SweCataDetret c = new SweCataDetret();
        c.setSweCata(componente_seleccionado);
        c.setSweTipoDetret(tipoDetRet_seleccionado);
        if (list_Originales_CataDetRet.size()>1) {
            if(list_Originales_CataDetRet.get(list_Originales_CataDetRet.size()-1).getRangoFinal()==null){
                list_Originales_CataDetRet.get(list_Originales_CataDetRet.size()-1)
                        .setRangoFinal(list_Originales_CataDetRet.get(list_Originales_CataDetRet.size()-1).getRangoInicial()+1);
            }
            c.setRangoInicial( list_Originales_CataDetRet.get(list_Originales_CataDetRet.size()-1).getRangoFinal()+1); 
        }
        this.list_Originales_CataDetRet.add(c);
    }

    /*Este evento se ejecuta al instante en que se edita un celda del 
     dataTable*/
    public void onCellEdit(RowEditEvent  event) {
        
//        SweCataDetret registro = (SweCataDetret)event.getObject();        

        boolean bandera = false;
        String msj_error = "";
        for (int i = 0; i < list_Originales_CataDetRet.size(); i++) {
            
            if(list_Originales_CataDetRet.get(i).getRangoFinal()!=null && (list_Originales_CataDetRet.get(i).getRangoFinal()<0 || list_Originales_CataDetRet.get(i).getRangoInicial()<0)){
                bandera = true;
                msj_error = "No se permiten valores negativos. Verifique el intervalo "+(i+1);                   
                break;
            }
            else if(i==0){
                //Verificamos que los valores máximo sea mayor o igual que el mínimo
                if(list_Originales_CataDetRet.get(i).getRangoFinal()!=null && list_Originales_CataDetRet.get(i).getRangoFinal()<list_Originales_CataDetRet.get(i).getRangoInicial()){
                    bandera = true;
                    msj_error = "El valor máximo del intervalo "+ i +" debe ser mayor o igual que su valor mínimo";                   
                    break;
                }
            }else{
                //Si hay un intervalo anterior, verificamos que el valor final anterior no sea null
                if(list_Originales_CataDetRet.get(i-1).getRangoFinal()==null){
                    bandera = true;
                    msj_error = "El valor máximo del intervalo "+ i + " no puede ser nulo";                   
                    break;          
                }
                //Verificamos que el valor mínimo del intervalo actual sea mayor que el valor máximo del intervalo anterior
                else if(list_Originales_CataDetRet.get(i).getRangoInicial()<=list_Originales_CataDetRet.get(i-1).getRangoFinal()){
                    bandera = true;
                    msj_error = "El valor mínimo del intervalo "+ (i+1) +" debe ser mayor que el valor máximo del intervalo "+i;                    
                    break;
                }
                // Verificamos que el valor mínimo del intervalo actual sea el valor máximo anterior mas uno
                else if(list_Originales_CataDetRet.get(i).getRangoInicial()>list_Originales_CataDetRet.get(i-1).getRangoFinal()+1){
                    bandera = true;
                    msj_error = "El valor mínimo del intervalo "+ (i+1) +" debe ser "+(list_Originales_CataDetRet.get(i-1).getRangoFinal()+1)+". No pueden quedar espacios entre los intervalos";                    
                    break;
                }
                // Verificamos que el valor mínimo actual sea menor o igual que el valor máximo actual
                else if(list_Originales_CataDetRet.get(i).getRangoFinal()!=null && list_Originales_CataDetRet.get(i).getRangoFinal()<list_Originales_CataDetRet.get(i).getRangoInicial()){
                    bandera = true;
                    msj_error = "El valor máximo del intervalo "+ (i+1) +" debe ser mayor que su valor mínimo";                    
                    break;
                }
            }            
        }
        if (bandera) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msj_error));
        }       
        disable_btnGuardar = bandera;      
        
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
     * @return the componente_seleccionado
     */
    public SweCata getComponente_seleccionado() {
        return componente_seleccionado;
    }

    /**
     * @param componente_seleccionado the componente_seleccionado to set
     */
    public void setComponente_seleccionado(SweCata componente_seleccionado) {
        this.componente_seleccionado = componente_seleccionado;
    }

    /**
     * @return the list_tipoDetRet
     */
    public List<SweTipoDetret> getList_tipoDetRet() {
        return list_tipoDetRet;
    }

    /**
     * @param list_tipoDetRet the list_tipoDetRet to set
     */
    public void setList_tipoDetRet(List<SweTipoDetret> list_tipoDetRet) {
        this.list_tipoDetRet = list_tipoDetRet;
    }

    /**
     * @return the tipoDetRet_seleccionado
     */
    public SweTipoDetret getTipoDetRet_seleccionado() {
        return tipoDetRet_seleccionado;
    }

    /**
     * @param tipoDetRet_seleccionado the tipoDetRet_seleccionado to set
     */
    public void setTipoDetRet_seleccionado(SweTipoDetret tipoDetRet_seleccionado) {
        this.tipoDetRet_seleccionado = tipoDetRet_seleccionado;
    }

    /**
     * @return the list_tipoDetRetXtipoComp
     */
    public List<SweTipoDetret> getList_tipoDetRetXtipoComp() {
        return list_tipoDetRetXtipoComp;
    }

    /**
     * @param list_tipoDetRetXtipoComp the list_tipoDetRetXtipoComp to set
     */
    public void setList_tipoDetRetXtipoComp(List<SweTipoDetret> list_tipoDetRetXtipoComp) {
        this.list_tipoDetRetXtipoComp = list_tipoDetRetXtipoComp;
    }

    /**
     * @return the idComponente_seleccionado
     */
    public Integer getIdComponente_seleccionado() {
        return idComponente_seleccionado;
    }

    /**
     * @param idComponente_seleccionado the idComponente_seleccionado to set
     */
    public void setIdComponente_seleccionado(Integer idComponente_seleccionado) {
        this.idComponente_seleccionado = idComponente_seleccionado;
    }

    /**
     * @return the list_Originales_CataDetRet
     */
    public List<SweCataDetret> getList_Originales_CataDetRet() {
        return list_Originales_CataDetRet;
    }

    /**
     * @param list_Originales_CataDetRet the list_Originales_CataDetRet to set
     */
    public void setList_Originales_CataDetRet(List<SweCataDetret> list_Originales_CataDetRet) {
        this.list_Originales_CataDetRet = list_Originales_CataDetRet;
    }

    /**
     * @return the btnAgregarFila
     */
    public boolean isBtnEditar() {
        return btnAgregarFila;
    }

    /**
     * @param btnAgregarFila the btnAgregarFila to set
     */
    public void setBtnAgregarFila(boolean btnAgregarFila) {
        this.btnAgregarFila = btnAgregarFila;
    }

    /**
     * @return the disable_btnGuardar
     */
    public boolean isDisable_btnGuardar() {
        return disable_btnGuardar;
    }

    /**
     * @param disable_btnGuardar the disable_btnGuardar to set
     */
    public void setDisable_btnGuardar(boolean btnGuardar) {
        this.disable_btnGuardar = btnGuardar;
    }
}

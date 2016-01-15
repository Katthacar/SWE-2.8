/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCataDetret;
import com.amerika.swe.model.SweCompXPfuncion;
import com.amerika.swe.model.SweCompXPfuncionId;
import com.amerika.swe.model.SweTipoDetret;
import java.io.Serializable;
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
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanCaracterizacionComplejidad implements Serializable {

    private Controller controller;
    private List<SweCata> list_componentes;
    private List<SweCata> list_complejidades;
    
    private List<SweTipoDetret> list_puntoFuncion;
    private List<SweTipoDetret> list_Det;
    private List<SweTipoDetret> list_RetFtr;
    
    private SweCata componente_seleccionado;
    private SweCata complejidad_seleccionada;
    private SweTipoDetret det_Seleccionado;
    private SweTipoDetret retFtr_Seleccionado;
    
    private int idComponenteSeleccionado;
    private int id_detSeleccionado;
    private int id_retFtrSeleccionado;
    private int id_ComplejidadSeleccionada;
    
    private List<SweCataDetret> list_intervalos1;
    private List<SweCataDetret> list_intervalos2;
    
    private SweCataDetret intervalo1_seleccionado;
    private SweCataDetret intervalo2_seleccionado;
    
    private List<SweCompXPfuncion> list_compXfuncion;   // Esta lista contine los registros anteriores
    private List<SweCompXPfuncion> nuevos_compXFuncion; // Esta lista contiene los nuevos registros a guardar
    
    private SweCompXPfuncion compXpfuncion_seleccionado;
    
    public BeanCaracterizacionComplejidad() {
        this.controller = new Controller();        
        
        list_Det = new ArrayList<SweTipoDetret>();
        list_RetFtr = new ArrayList<SweTipoDetret>();
        componente_seleccionado = new SweCata();
        det_Seleccionado = new SweTipoDetret();
        retFtr_Seleccionado = new SweTipoDetret();
        list_compXfuncion = new ArrayList<SweCompXPfuncion>();
        nuevos_compXFuncion = new ArrayList<SweCompXPfuncion>();
        list_intervalos1 = new ArrayList<SweCataDetret>();
        list_intervalos2 = new ArrayList<SweCataDetret>();
        
        toggleFieldSet();
    }

    public void toggleFieldSet(){
        
        /* Si aún no esta inicializada la lista de tipos de componentes
         * consultamos la base de datos 
         */
        if(!(list_componentes instanceof ArrayList)){
            list_componentes = this.controller.listarPOR("Tipo De Componente");
        }        
        
        /*
         * Si aún no se han listado los tipos de función consultamos la base
         * de datos
         */
        if(!(list_puntoFuncion instanceof ArrayList)){
            list_puntoFuncion = this.controller.listar_DetRet();            
        }
        
        if(!(list_complejidades instanceof ArrayList)){
            list_complejidades = this.controller.listarPOR("Complejidad");
        }
    }
    
    /*
     * Este metodo se ejecuta cuando se selecciona un componente
     * del selectOneMenu, se toma el componente seleccionado de la lista
     * y se guarda en el atributo componente_seleccionado
     * 
     */
    public void ajaxSelectedComponente() {
        reiniciar();
        if(idComponenteSeleccionado!=-1){
            for (int i = 0; i < list_componentes.size(); i++) {
                if (list_componentes.get(i).getIdCatalogo() == idComponenteSeleccionado) {
                    componente_seleccionado = list_componentes.get(i);
                    break;
                }
            }
            
            /* Agregamos los tipos de funcion en los list
             * de acuerdo al tipo de componente seleccionado
             */
            for (int i = 0; i < list_puntoFuncion.size(); i++) {
                if (list_puntoFuncion.get(i).getNombre().equals("DET")) {
                    this.list_Det.add(list_puntoFuncion.get(i));
                } else if (componente_seleccionado.getNombre().equals("ILF") || componente_seleccionado.getNombre().equals("EIF")) {
                    if (list_puntoFuncion.get(i).getNombre().equals("RET")) {
                        this.list_RetFtr.add(list_puntoFuncion.get(i));
                    }
                } else {
                    if (list_puntoFuncion.get(i).getNombre().equals("FTR")) {
                        this.list_RetFtr.add(list_puntoFuncion.get(i));
                    }
                }
            }
            
            //Listamos los registros anteriores de acuerdo al tipo de componente seleccionados
            this.list_compXfuncion = this.controller.listar_complejidadXpFuncion(idComponenteSeleccionado);
        }
    }
    
    public void reiniciar(){
        this.list_Det.clear();
        this.list_RetFtr.clear();
        this.list_compXfuncion.clear();
        
        this.det_Seleccionado = null;
        this.retFtr_Seleccionado = null;
        this.id_detSeleccionado = 0;
        this.id_retFtrSeleccionado = 0;
        
        this.list_intervalos1.clear();
        this.list_intervalos2.clear();
        
    }
    
    public void ajaxSelectDet(){        
        for (int i = 0; i < list_Det.size(); i++) {
            if(id_detSeleccionado == this.list_Det.get(i).getCodigoTipoDetret()){
                this.det_Seleccionado = this.list_Det.get(i);
                break;
            }
        }        
        try{
            this.list_intervalos1 = this.controller.listar(idComponenteSeleccionado, id_detSeleccionado);
        }catch(Exception e){}      
        
    }
    
    public void ajaxSelectRetFtr(){
        for (int i = 0; i < list_RetFtr.size(); i++) {
            if(id_retFtrSeleccionado == this.list_RetFtr.get(i).getCodigoTipoDetret()){
                this.retFtr_Seleccionado = this.list_RetFtr.get(i);
                break;
            }
        }
        try{
            this.list_intervalos2 = this.controller.listar(idComponenteSeleccionado, id_retFtrSeleccionado);
        }catch(Exception e){} 
    }
    
    
    public void ajaxRradioComplejidad(){
        for (int i = 0; i < list_complejidades.size(); i++) {
            if(id_ComplejidadSeleccionada == list_complejidades.get(i).getIdCatalogo()){
                complejidad_seleccionada = list_complejidades.get(i);
                break;
            }
        }
    }
    
    public void btnAgregar(){
        if(intervalo2_seleccionado == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un intervalo de la tabla Ret/Ftr"));
        }else if(intervalo1_seleccionado== null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un intervalo de la tabla Det"));
        }else if(complejidad_seleccionada == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione una complejidad"));
        }else{
            
            /*Verificamos que los intevalos no se hayan agregado antes*/
            boolean ban = true;
            for (int i = 0; i < list_compXfuncion.size(); i++) {
                if(list_compXfuncion.get(i).getSweCataDetretByIdDet().getIdcatalogoDetret() == intervalo1_seleccionado.getIdcatalogoDetret() &&
                   list_compXfuncion.get(i).getSweCataDetretByIdRetftr().getIdcatalogoDetret() == intervalo2_seleccionado.getIdcatalogoDetret()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El componente actual con los intervalos seleccionados ya fueron agregados"));
                    ban = false;
                    break;
                }
            }
            
            if(ban){
                
                SweCompXPfuncion cf = new SweCompXPfuncion();
                SweCompXPfuncionId id = new SweCompXPfuncionId(intervalo1_seleccionado.getIdcatalogoDetret(), intervalo2_seleccionado.getIdcatalogoDetret(), id_ComplejidadSeleccionada);
                cf.setId(id);
                cf.setSweCata(complejidad_seleccionada);
                cf.setSweCataDetretByIdDet(intervalo1_seleccionado);
                cf.setSweCataDetretByIdRetftr(intervalo2_seleccionado); 
                list_compXfuncion.add(cf);      // Agregamos el valor en la lista que se muestra
                nuevos_compXFuncion.add(cf);    // Agregamoe el valor en la lista que guardaremos
                intervalo1_seleccionado    = null;                  
                complejidad_seleccionada   = null;
                id_ComplejidadSeleccionada = 0;
            }
        }
    }
    
    
    public void btnGuardarCambios(){
        if(!nuevos_compXFuncion.isEmpty()){
            Object obj = this.controller.agregar_complejidaXpfuncion(nuevos_compXFuncion);
            
            if(obj instanceof Boolean){
                nuevos_compXFuncion.clear(); // Limpiamos la lista de valores nuevos registrados
//                list_compXfuncion.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "El registro se completó con éxito"));
            }else{
                String error = ((HibernateException)obj).getCause().toString();
                if(error.indexOf("restricción única") > -1){
                    error = "No se pudo completar el registro. Ya existe un elemento igual a los que desea registrar";
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", error));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "No hay registros nuevos para guardar"));
        }
    }
    
    
    public void dialogEliminarElemento(boolean bandera){
        if(bandera){            
            /*Si el elemento seleccionado para elminar no esta contenido 
             en la lista de los nuevos registros, quiere decir que ya encuentra registrado
             en la base de datos y por tanto hay que eliminarlo. De los contrario solo hay que sacarlo de la lista
             que muestra los valores.*/
            if(!this.nuevos_compXFuncion.contains(compXpfuncion_seleccionado)){
                Object obj = this.controller.eliminar_compXpfuncion(compXpfuncion_seleccionado);
                if(obj instanceof Boolean){
                    this.list_compXfuncion.remove(compXpfuncion_seleccionado);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "El registro fue borrado satisfactoriamente"));
                }else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo eliminar el registro del sistema"));
                }
            }else{ 
                this.nuevos_compXFuncion.remove(compXpfuncion_seleccionado);
                this.list_compXfuncion.remove(compXpfuncion_seleccionado);
            }
             
        }else{
            if(compXpfuncion_seleccionado==null){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "Seleccione un elemento de la tabla y luego presione eliminar"));
            }else{
                RequestContext.getCurrentInstance().execute("DialogEliminar.show()"); 	
            }
        }
        
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
     * @return the list_Det
     */
    public List<SweTipoDetret> getList_Det() {
        return list_Det;
    }

    /**
     * @param list_Det the list_Det to set
     */
    public void setList_Det(List<SweTipoDetret> list_Det) {
        this.list_Det = list_Det;
    }

    /**
     * @return the list_RetFtr
     */
    public List<SweTipoDetret> getList_RetFtr() {
        return list_RetFtr;
    }

    /**
     * @param list_RetFtr the list_RetFtr to set
     */
    public void setList_RetFtr(List<SweTipoDetret> list_RetFtr) {
        this.list_RetFtr = list_RetFtr;
    }

    /**
     * @return the det_Seleccionado
     */
    public SweTipoDetret getDet_Seleccionado() {
        return det_Seleccionado;
    }

    /**
     * @param det_Seleccionado the det_Seleccionado to set
     */
    public void setDet_Seleccionado(SweTipoDetret det_Seleccionado) {
        this.det_Seleccionado = det_Seleccionado;
    }

    /**
     * @return the retFtr_Seleccionado
     */
    public SweTipoDetret getRetFtr_Seleccionado() {
        return retFtr_Seleccionado;
    }

    /**
     * @param retFtr_Seleccionado the retFtr_Seleccionado to set
     */
    public void setRetFtr_Seleccionado(SweTipoDetret retFtr_Seleccionado) {
        this.retFtr_Seleccionado = retFtr_Seleccionado;
    }

    /**
     * @return the idComponenteSeleccionado
     */
    public int getIdComponenteSeleccionado() {
        return idComponenteSeleccionado;
    }

    /**
     * @param idComponenteSeleccionado the idComponenteSeleccionado to set
     */
    public void setIdComponenteSeleccionado(int idComponenteSeleccionado) {
        this.idComponenteSeleccionado = idComponenteSeleccionado;
    }

    /**
     * @return the id_detSeleccionado
     */
    public int getId_detSeleccionado() {
        return id_detSeleccionado;
    }

    /**
     * @param id_detSeleccionado the id_detSeleccionado to set
     */
    public void setId_detSeleccionado(int id_detSeleccionado) {
        this.id_detSeleccionado = id_detSeleccionado;
    }

    /**
     * @return the id_retFtrSeleccionado
     */
    public int getId_retFtrSeleccionado() {
        return id_retFtrSeleccionado;
    }

    /**
     * @param id_retFtrSeleccionado the id_retFtrSeleccionado to set
     */
    public void setId_retFtrSeleccionado(int id_retFtrSeleccionado) {
        this.id_retFtrSeleccionado = id_retFtrSeleccionado;
    }

    /**
     * @return the list_intervalos1
     */
    public List<SweCataDetret> getList_intervalos1() {
        return list_intervalos1;
    }

    /**
     * @param list_intervalos1 the list_intervalos1 to set
     */
    public void setList_intervalos1(List<SweCataDetret> list_intervalos1) {
        this.list_intervalos1 = list_intervalos1;
    }

    /**
     * @return the list_intervalos2
     */
    public List<SweCataDetret> getList_intervalos2() {
        return list_intervalos2;
    }

    /**
     * @param list_intervalos2 the list_intervalos2 to set
     */
    public void setList_intervalos2(List<SweCataDetret> list_intervalos2) {
        this.list_intervalos2 = list_intervalos2;
    }

    /**
     * @return the intervalo1_seleccionado
     */
    public SweCataDetret getIntervalo1_seleccionado() {
        return intervalo1_seleccionado;
    }

    /**
     * @param intervalo1_seleccionado the intervalo1_seleccionado to set
     */
    public void setIntervalo1_seleccionado(SweCataDetret intervalo1_seleccionado) {
        this.intervalo1_seleccionado = intervalo1_seleccionado;
    }

    /**
     * @return the intervalo2_seleccionado
     */
    public SweCataDetret getIntervalo2_seleccionado() {
        return intervalo2_seleccionado;
    }

    /**
     * @param intervalo2_seleccionado the intervalo2_seleccionado to set
     */
    public void setIntervalo2_seleccionado(SweCataDetret intervalo2_seleccionado) {
        this.intervalo2_seleccionado = intervalo2_seleccionado;
    }

    /**
     * @return the list_complejidades
     */
    public List<SweCata> getList_complejidades() {
        return list_complejidades;
    }

    /**
     * @param list_complejidades the list_complejidades to set
     */
    public void setList_complejidades(List<SweCata> list_complejidades) {
        this.list_complejidades = list_complejidades;
    }

    /**
     * @return the id_ComplejidadSeleccionada
     */
    public int getId_ComplejidadSeleccionada() {
        return id_ComplejidadSeleccionada;
    }

    /**
     * @param id_ComplejidadSeleccionada the id_ComplejidadSeleccionada to set
     */
    public void setId_ComplejidadSeleccionada(int id_ComplejidadSeleccionada) {
        this.id_ComplejidadSeleccionada = id_ComplejidadSeleccionada;
    }

    /**
     * @return the list_compXfuncion
     */
    public List<SweCompXPfuncion> getList_compXfuncion() {
        return list_compXfuncion;
    }

    /**
     * @param list_compXfuncion the list_compXfuncion to set
     */
    public void setList_compXfuncion(List<SweCompXPfuncion> list_compXfuncion) {
        this.list_compXfuncion = list_compXfuncion;
    }

    /**
     * @return the compXpfuncion_seleccionado
     */
    public SweCompXPfuncion getCompXpfuncion_seleccionado() {
        return compXpfuncion_seleccionado;
    }

    /**
     * @param compXpfuncion_seleccionado the compXpfuncion_seleccionado to set
     */
    public void setCompXpfuncion_seleccionado(SweCompXPfuncion compXpfuncion_seleccionado) {
        this.compXpfuncion_seleccionado = compXpfuncion_seleccionado;
    }

    /**
     * @return the nuevos_compXFuncion
     */
    public List<SweCompXPfuncion> getNuevos_compXFuncion() {
        return nuevos_compXFuncion;
    }

    /**
     * @param nuevos_compXFuncion the nuevos_compXFuncion to set
     */
    public void setNuevos_compXFuncion(List<SweCompXPfuncion> nuevos_compXFuncion) {
        this.nuevos_compXFuncion = nuevos_compXFuncion;
    }
}

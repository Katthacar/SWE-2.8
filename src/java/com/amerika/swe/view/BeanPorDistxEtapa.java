/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SwePorcDistXetapa;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class BeanPorDistxEtapa implements Serializable {

    private Controller controller;
    private List<SwePorcDistXetapa> list_porcDistxEtapa;
    private List<SwePorcDistXetapa> list_originalPorcDistxEtapa; //Este atributo contiene la lista de valores SwePorcDistXetapa traidos de la DB
    private List<SwePorcDistXetapa> list_historico;
    private SwePorcDistXetapa seleccionado;
    private SwePorcDistXetapa nuevo_porcDistxEtapa;
    private List<SweCata> list_etapas;
    private BigDecimal sum_porcentajes;
    private boolean editar = false;
    
    public BeanPorDistxEtapa() {
        this.controller = new Controller();
        list_originalPorcDistxEtapa = this.controller.listarVigentes_porcDistxEtapa();
        list_porcDistxEtapa = new ArrayList<SwePorcDistXetapa>(list_originalPorcDistxEtapa); 
        
        sum_porcentajes = BigDecimal.ZERO;
        sum_porcentajes.setScale(2);
        
        for (int i = 0; i < list_porcDistxEtapa.size(); i++)
             sum_porcentajes = sum_porcentajes.add(list_porcDistxEtapa.get(i).getPorcentaje());
        
    }

    public void inicializar() {
        this.nuevo_porcDistxEtapa = new SwePorcDistXetapa();
        this.nuevo_porcDistxEtapa.setVigenciaInicial(Calendar.getInstance().getTime());
        this.nuevo_porcDistxEtapa.setSweCata(new SweCata());        
        /*Preguntamos si la lista esta vacia y evitar consultar la DB
         si los datos ya estan cargados*/
        if (list_etapas == null) {
            list_etapas = this.controller.listarPOR("Etapas");
        }
        if (seleccionado !=null) {
            this.nuevo_porcDistxEtapa.setSweCata(seleccionado.getSweCata());
            this.nuevo_porcDistxEtapa.setPorcentaje(seleccionado.getPorcentaje()); 
        }

    }

    public void agregar() {
        
        if(nuevo_porcDistxEtapa.getPorcentaje().scale() > 2 || nuevo_porcDistxEtapa.getPorcentaje().compareTo(BigDecimal.valueOf(0)) <0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Los porcentajes deben ser mayor que cero y con máximo dos dígitos decimales"));
        }else if(nuevo_porcDistxEtapa.getPorcentaje().compareTo(BigDecimal.valueOf(100.00))>0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Una etapa puede representar máximo el 100%"));
        }else{
            sum_porcentajes = BigDecimal.ZERO;
            sum_porcentajes.setScale(2);
            for (int i = 0; i < list_etapas.size(); i++) {
                if (nuevo_porcDistxEtapa.getSweCata().getIdCatalogo() == list_etapas.get(i).getIdCatalogo()) {
                    this.nuevo_porcDistxEtapa.setSweCata(list_etapas.get(i));
                    break;
                }
            }

            boolean ban = false;
            for (int i = 0; i < list_porcDistxEtapa.size(); i++) {

                /*Preguntamos si la etapa ya esta contenida en la lista*/
                if (list_porcDistxEtapa.get(i).getSweCata().getIdCatalogo() == nuevo_porcDistxEtapa.getSweCata().getIdCatalogo()) {
                    list_porcDistxEtapa.set(i, nuevo_porcDistxEtapa);
                    ban = true;
                    RequestContext.getCurrentInstance().execute("carDialog.hide()");
                }
                sum_porcentajes = sum_porcentajes.add(list_porcDistxEtapa.get(i).getPorcentaje());
            }

            /*Si el tamaño de la lista no varió quiere decir que la etapa que deseamos
             agregar no esta contenida en la lista, por lo tango la agregamos*/
            if (!ban) {
                list_porcDistxEtapa.add(nuevo_porcDistxEtapa);
                sum_porcentajes = sum_porcentajes.add(nuevo_porcDistxEtapa.getPorcentaje());
//                editar = true;
                RequestContext.getCurrentInstance().execute("carDialog.hide()");
            }

            if (sum_porcentajes.compareTo(BigDecimal.valueOf(100.00)) == 0) {                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Completado!", "La suma de los porcentajes ha alcanzado el 100%"));
            } else if (sum_porcentajes.compareTo(BigDecimal.valueOf(100.00)) > 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "La suma de los porcentajes ha excedido el 100% por favor ajuste los valores para guardar los cambios"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "La suma de los porcentajes no alcanza el 100% por favor ajuste los valores para guardar los cambios"));
            }
        }
    }

    public void guardarCambios() {

        List<SwePorcDistXetapa> cambios_porDistxEtapab = new ArrayList<SwePorcDistXetapa>();

        /*Verificamos si esta lista contiene valores traidos de la DB*/
        if (!list_originalPorcDistxEtapa.isEmpty()) {
            for (int i = 0; i < list_originalPorcDistxEtapa.size(); i++) {

                /*Preguntamos si los valores de la lista mostrada en la tabla sufrieron
                 cambios. En caso de ser verdadero se toman aquellos objetos modificados
                 y se agregan en la lista cambios_porDistxEtapab */
                if (list_originalPorcDistxEtapa.get(i).getPorcentaje().floatValue() != list_porcDistxEtapa.get(i).getPorcentaje().floatValue()) {
                    cambios_porDistxEtapab.add(list_porcDistxEtapa.get(i));
                }
            }

            /*Verificamos si la lista cambios_porDistxEtapab tienen objetos
              para guardar los cambios correspondientes*/
            if (!cambios_porDistxEtapab.isEmpty()) {
                SwePorcDistXetapa[] v = new SwePorcDistXetapa[cambios_porDistxEtapab.size()];
                cambios_porDistxEtapab.toArray(v);
                Object obj = this.controller.agregar_porcDistxEtapa(v);

                if (obj instanceof Boolean) {
                    editar = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Se modificaron los porcentajes de " + cambios_porDistxEtapab.size() + " etapas con éxito"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Los cambios no se puedieron realizar"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "No hay cambios en los porcentajes para registrar"));
            }

        }else{
            /*
             * En caso de que la lista list_originalPorcDistxEtapa este vacía, 
             * se toman los valores de list_porcDistxEtapa que es la que se muestra en 
             * pantalla y que es sesceptible a susfrir cambios
             */
            SwePorcDistXetapa[] v = new SwePorcDistXetapa[list_porcDistxEtapa.size()];
            list_porcDistxEtapa.toArray(v);
            Object obj = this.controller.agregar_porcDistxEtapa(v);

            if (obj instanceof Boolean) {
                editar = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "Se modificaron los porcentajes de " + list_porcDistxEtapa.size() + " etapas con éxito"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Los cambios no se puedieron realizar"));
            }
        }
    }

    public void ajaxEtapaSeleccionada(){
       
        for (int i = 0; i < list_porcDistxEtapa.size(); i++) {
            if(list_porcDistxEtapa.get(i).getSweCata().getIdCatalogo() == nuevo_porcDistxEtapa.getSweCata().getIdCatalogo()){
                
                /*Si la suma de los porcentajes es menor que 100 se añade 
                 los puntos porcentuales faltantes en el objeto a modificar
                 como valor predeterminado*/
                if(sum_porcentajes.compareTo(BigDecimal.valueOf(100.00)) < 0){
                    nuevo_porcDistxEtapa.setPorcentaje(list_porcDistxEtapa.get(i).getPorcentaje().add(BigDecimal.valueOf(100.00f - sum_porcentajes.floatValue())).setScale(2, RoundingMode.UP));   
                    
                }
                /*Si la Excede el 100% restamos al objeto que se va a modificar
                 y se pone este valor como default en la edición*/
                else if(sum_porcentajes.compareTo(BigDecimal.valueOf(100.00)) > 0){
                    nuevo_porcDistxEtapa.setPorcentaje(list_porcDistxEtapa.get(i).getPorcentaje().subtract(BigDecimal.valueOf(sum_porcentajes.floatValue() - 100.00f)).setScale(2, RoundingMode.DOWN));                   
                }else{
                    nuevo_porcDistxEtapa.setPorcentaje(list_porcDistxEtapa.get(i).getPorcentaje());  
                }
                break;
            }
        }
    }
    
    
    public void historial() {
        if(seleccionado != null){
            this.list_historico = this.controller.historial_porcDistxEtapa(this.seleccionado.getSweCata().getIdCatalogo());
            RequestContext.getCurrentInstance().execute("DialogHistorico.show()");
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "Por favor seleccione un elemento de la tabla para ver su histórico"));
        }
        
    }

    /**
     * @return the list_porcDistxEtapa
     */
    public List<SwePorcDistXetapa> getList_porcDistxEtapa() {
        return list_porcDistxEtapa;
    }

    /**
     * @param list_porcDistxEtapa the list_porcDistxEtapa to set
     */
    public void setList_porcDistxEtapa(List<SwePorcDistXetapa> list_porcDistxEtapa) {
        this.list_porcDistxEtapa = list_porcDistxEtapa;
    }

    /**
     * @return the list_historico
     */
    public List<SwePorcDistXetapa> getList_historico() {
        return list_historico;
    }

    /**
     * @param list_historico the list_historico to set
     */
    public void setList_historico(List<SwePorcDistXetapa> list_historico) {
        this.list_historico = list_historico;
    }

    /**
     * @return the seleccionado
     */
    public SwePorcDistXetapa getSeleccionado() {
        return seleccionado;
    }

    /**
     * @param seleccionado the seleccionado to set
     */
    public void setSeleccionado(SwePorcDistXetapa seleccionado) {
        this.seleccionado = seleccionado;
    }

    /**
     * @return the nuevo_porcDistxEtapa
     */
    public SwePorcDistXetapa getNuevo_porcDistxEtapa() {
        return nuevo_porcDistxEtapa;
    }

    /**
     * @param nuevo_porcDistxEtapa the nuevo_porcDistxEtapa to set
     */
    public void setNuevo_porcDistxEtapa(SwePorcDistXetapa nuevo_porcDistxEtapa) {
        this.nuevo_porcDistxEtapa = nuevo_porcDistxEtapa;
    }

    /**
     * @return the list_etapas
     */
    public List<SweCata> getList_etapas() {
        return list_etapas;
    }

    /**
     * @param list_etapas the list_etapas to set
     */
    public void setList_etapas(List<SweCata> list_etapas) {
        this.list_etapas = list_etapas;
    }

    /**
     * @return the sum_porcentajes
     */
    public BigDecimal getSum_porcentajes() {
        return sum_porcentajes;
    }

    /**
     * @param sum_porcentajes the sum_porcentajes to set
     */
    public void setSum_porcentajes(BigDecimal sum_porcentajes) {
        this.sum_porcentajes = sum_porcentajes;
    }

    /**
     * @return the list_originalPorcDistxEtapa
     */
    public List<SwePorcDistXetapa> getList_originalPorcDistxEtapa() {
        return list_originalPorcDistxEtapa;
    }

    /**
     * @param list_originalPorcDistxEtapa the list_originalPorcDistxEtapa to set
     */
    public void setList_originalPorcDistxEtapa(List<SwePorcDistXetapa> list_originalPorcDistxEtapa) {
        this.list_originalPorcDistxEtapa = list_originalPorcDistxEtapa;
    }

    /**
     * @return the editiar
     */
    public boolean isEditar() {
        return editar;
    }

    /**
     * @param editiar the editiar to set
     */
    public void setEditar(boolean editar) {
        this.editar = editar;
    }
}

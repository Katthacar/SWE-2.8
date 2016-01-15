/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view.estimacion;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFactAmbiXsolId;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SweFtecnXsolId;
import com.amerika.swe.model.SweTipoApliXFtec;
import com.amerika.swe.view.BeanEstimacion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
public class BeanFactorAjuste implements Serializable {
    
    private Controller controller;
    private List<SweFactAmbiXsol> list_factorAmbiental;
    private SweFactAmbiXsol[] facAmb_seleccionados;
    private List<SweFtecnXsol> list_apliXfact;
    private int totalInfluencia;
    private BigDecimal VAF;
    private float totalInfluenciaAmb;
    private BigDecimal facAmbCalculado;
    
    private BigDecimal k1;
    private BigDecimal k2;
    
    private BigDecimal k1FactorAmbiental;
    private BigDecimal k2FactorAmbiental;
                
    public BeanFactorAjuste() {
        this.controller = new Controller();
        List<SweCata> l = this.controller.listarPOR("Factor Ambiental");
        list_factorAmbiental = new ArrayList<SweFactAmbiXsol>();
        
        for (int i = 0; i < l.size(); i++) {            
            SweFactAmbiXsolId  id = new SweFactAmbiXsolId(l.get(i).getIdCatalogo(), 0);
            list_factorAmbiental.add(new SweFactAmbiXsol(id, l.get(i), null, l.get(i).getValor(), 0)); 
        }
        
        list_apliXfact = new ArrayList<SweFtecnXsol>();
        k1 = this.controller.buscar_parametro("K1 VAF");
        k2 = this.controller.buscar_parametro("K2 VAF");
        
        k1FactorAmbiental = this.controller.buscar_parametro("K1 Infl Amb");
        k2FactorAmbiental = this.controller.buscar_parametro("K2 Infl Amb");
        this.facAmbCalculado = k1FactorAmbiental;        
        totalInfluencia = 0;
        
        System.gc();
    }

    public void reiniciar(){
        totalInfluencia = 0;
        this.facAmbCalculado = k1FactorAmbiental;
        list_apliXfact = new ArrayList<SweFtecnXsol>();
        facAmb_seleccionados = null;
        VAF = BigDecimal.ZERO;
        totalInfluenciaAmb = 0.00f;
    }
    
    public void buscar_factoresTecnicos(Integer idAplicacion){
        if(idAplicacion > -1){
            List<SweTipoApliXFtec> l = this.controller.listar_TipoApliXFactec(idAplicacion);
            totalInfluencia = 0;
            list_apliXfact.clear();
            for (int i = 0; i < l.size(); i++) {
                SweFtecnXsolId id = new SweFtecnXsolId(0, l.get(i).getSweCataByCodigoFacttecnico().getIdCatalogo());
                list_apliXfact.add(new SweFtecnXsol(id, l.get(i).getSweCataByCodigoFacttecnico(), null, l.get(i).getGradoInfluencia()));
                totalInfluencia = totalInfluencia + l.get(i).getGradoInfluencia(); 
            }
            VAF = new BigDecimal(totalInfluencia).multiply(k2).add(k1);  
        }else{
            list_apliXfact.clear();
            VAF = BigDecimal.ZERO;
            totalInfluencia = 0;
        }
        /*A continuación Actualizamos el valor de VAF en el Bean que tiene
          los resultados de la estimación*/
         FacesContext context = FacesContext.getCurrentInstance();
         BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context,"#{beanEstimacion}", Object.class);
         estimacion.getEstimacionSolicitud().setVaf(VAF.setScale(2, RoundingMode.UP));  
    }
    
    public void ajaxActualizarTotalFacTec(){
        totalInfluencia = 0;
        for (int i = 0; i < list_apliXfact.size(); i++) {
            totalInfluencia = totalInfluencia + list_apliXfact.get(i).getGradoInfluencia();
        }
        VAF = new BigDecimal(totalInfluencia).multiply(k2).add(k1);  
        
        /*A continuación Actualizamos el valor de VAF y CT en el Bean que tiene
         los resultados de la estimación*/
        FacesContext context = FacesContext.getCurrentInstance();
        BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context,"#{beanEstimacion}", Object.class);
        
        estimacion.getEstimacionSolicitud().setVaf(VAF.setScale(2, RoundingMode.UP));  // Estimación solicitud VAF        
        estimacion.setEstados_estimacion((byte)1);   // Se ha modificado la estimación y no se han guardado los cambios
//        estimacion.getEstimacionSolicitud().setCt(totalInfluencia);                    // Estimación solicitud CT
    }
    
    public void ajaxActualizarTotalFacAmb(){
        totalInfluenciaAmb = 0.00f;
        
        for (int i = 0; i < (facAmb_seleccionados!=null ? facAmb_seleccionados.length : 0); i++) {
            totalInfluenciaAmb += facAmb_seleccionados[i].getPeso().floatValue()*(float)facAmb_seleccionados[i].getInfluencia();
        }
       this.facAmbCalculado = k1FactorAmbiental.subtract(BigDecimal.valueOf(totalInfluenciaAmb).multiply(k2FactorAmbiental));  
       actualializarFCE_TPF();
    }
    
    /** A continuación Actualizamos el valor de FCE y TPF en el Bean que tiene
     *  los resultados de la estimación
     *  FCE = FactorAmbientaCalculado + FactorCalibracionxTipoSolicitud
     *  TPF = APF * FCE
     *  HE = TPF * HEPF
     *  HRJE = HE;
     */
    private void actualializarFCE_TPF(){        
        FacesContext context = FacesContext.getCurrentInstance();
        BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context,"#{beanEstimacion}", Object.class);
        estimacion.getEstimacionSolicitud().setFa(facAmbCalculado); 
        estimacion.getEstimacionSolicitud().setFce(facAmbCalculado.add(estimacion.getFactorCalibracionTpSolicitud()).setScale(2, RoundingMode.DOWN));  
        estimacion.getEstimacionSolicitud().setTpf(estimacion.getEstimacionSolicitud().getApf().multiply(estimacion.getEstimacionSolicitud().getFce()).setScale(2, RoundingMode.DOWN));   
        estimacion.getEstimacionSolicitud().setHe(estimacion.getEstimacionSolicitud().getTpf().multiply(estimacion.getEstimacionSolicitud().getHepf()).setScale(2, RoundingMode.DOWN)); 
        estimacion.getEstimacionSolicitud().setHrje(estimacion.getEstimacionSolicitud().getHe());
//        estimacion.getEstimacionSolicitud().setFa(BigDecimal.valueOf(totalInfluenciaAmb));        
        estimacion.setEstados_estimacion((byte)1);
    }
    
    /**
     * Este metodo eliminar un factor de la tabla factoAmbientalXsolicitud.
     * Si ya tiene id de solicitud quiere decir que el objeto fue registrado
     */
    public void eliminarFacAmbiental(SweFactAmbiXsol facAmbiental){
        if (facAmbiental!=null && facAmbiental.getSweSoli() != null) {
            Object obj = this.controller.elimnar_FacAmbSolicitud(facAmbiental);            
            if(obj instanceof Boolean){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "Se ha borrado el factor ambiental de la estimación"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo borrar el factor ambiental de la estimación"));
            }
        }
    }
    
    
    /**
     * @return the list_factorAmbiental
     */
    public List<SweFactAmbiXsol> getList_factorAmbiental() {
        return list_factorAmbiental;
    }

    /**
     * @param list_factorAmbiental the list_factorAmbiental to set
     */
    public void setList_factorAmbiental(List<SweFactAmbiXsol> list_factorAmbiental) {
        this.list_factorAmbiental = list_factorAmbiental;
    }

    /**
     * @return the facAmb_seleccionados
     */
    public SweFactAmbiXsol[] getFacAmb_seleccionados() {
        return facAmb_seleccionados;
    }

    /**
     * @param facAmb_seleccionados the facAmb_seleccionados to set
     */
    public void setFacAmb_seleccionados(SweFactAmbiXsol[] facAmb_seleccionados) {
        if(this.facAmb_seleccionados!=null){
            /**
             * En caso de contener elementos el vector verificamos cual
             * fue el objeto extraido y lo enviamos como parametro a la funcion
             * eliminarFacAmbiental que verificará si el objeto esta registrado en tabla
             */          
            if(this.facAmb_seleccionados.length > facAmb_seleccionados.length){
                boolean ban = false;
                for (int i = 0; i < this.facAmb_seleccionados.length; i++) {
                    for (int j = 0; j < facAmb_seleccionados.length; j++) {
                        if (facAmb_seleccionados[j].getId().getIdfactambietal() == this.facAmb_seleccionados[i].getId().getIdfactambietal()) {
                            ban = true;
                            break;
                        }
                    }
                    if (!ban) {
                        eliminarFacAmbiental(this.facAmb_seleccionados[i]);
                        break;
                    }
                    ban = false;
                }
            }
        }
        this.facAmb_seleccionados = facAmb_seleccionados;
    }

    /**
     * @return the list_apliXfact
     */
    public List<SweFtecnXsol> getList_apliXfact() {
        return list_apliXfact;
    }

    /**
     * @param list_apliXfact the list_apliXfact to set
     */
    public void setList_apliXfact(List<SweFtecnXsol> list_apliXfact) {
        this.list_apliXfact = list_apliXfact;
    }

    /**
     * @return the totalInfluencia
     */
    public int getTotalInfluencia() {
        return totalInfluencia;
    }

    /**
     * @param totalInfluencia the totalInfluencia to set
     */
    public void setTotalInfluencia(int totalInfluencia) {
        this.totalInfluencia = totalInfluencia;
    }

    /**
     * @return the VAF
     */
    public BigDecimal getVAF() {
        return VAF;
    }

    /**
     * @param VAF the VAF to set
     */
    public void setVAF(BigDecimal VAF) {
        this.VAF = VAF;
    }

    /**
     * @return the totalInfluenciaAmb
     */
    public float getTotalInfluenciaAmb() {
        return totalInfluenciaAmb;
    }

    /**
     * @param totalInfluenciaAmb the totalInfluenciaAmb to set
     */
    public void setTotalInfluenciaAmb(float totalInfluenciaAmb) {
        this.totalInfluenciaAmb = totalInfluenciaAmb;
    }

    /**
     * @return the facAmbCalculado
     */
    public BigDecimal getFacAmbCalculado() {
        return facAmbCalculado;
    }

    /**
     * @param facAmbCalculado the facAmbCalculado to set
     */
    public void setFacAmbCalculado(BigDecimal facAmbCalculado) {
        this.facAmbCalculado = facAmbCalculado;
    }

    /**
     * @return the k1
     */
    public BigDecimal getK1() {
        return k1;
    }

    /**
     * @param k1 the k1 to set
     */
    public void setK1(BigDecimal k1) {
        this.k1 = k1;
    }

    /**
     * @return the k2
     */
    public BigDecimal getK2() {
        return k2;
    }

    /**
     * @param k2 the k2 to set
     */
    public void setK2(BigDecimal k2) {
        this.k2 = k2;
    }

    /**
     * @return the k1FactorAmbiental
     */
    public BigDecimal getK1FactorAmbiental() {
        return k1FactorAmbiental;
    }

    /**
     * @param k1FactorAmbiental the k1FactorAmbiental to set
     */
    public void setK1FactorAmbiental(BigDecimal k1FactorAmbiental) {
        this.k1FactorAmbiental = k1FactorAmbiental;
    }

    /**
     * @return the k2FactorAmbiental
     */
    public BigDecimal getK2FactorAmbiental() {
        return k2FactorAmbiental;
    }

    /**
     * @param k2FactorAmbiental the k2FactorAmbiental to set
     */
    public void setK2FactorAmbiental(BigDecimal k2FactorAmbiental) {
        this.k2FactorAmbiental = k2FactorAmbiental;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view.estimacion;


import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.SweTipoDetret;
import com.amerika.swe.view.BeanEstimacion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class BeanPFNoAjustados implements Serializable{

    private Controller controller;
    private List<SweCompXsoli> list_puntosFuncionales;
    private Map<Integer, List<SweCompXsoli>> mapa_listas;        
    private List<SweCata> list_TipoComponentes;
    private List<SweCata> list_Complejidades;
    private Map<Integer,Map<Integer,Integer>> mapa_pesosVigentes;
    private Map<Integer,Map<Integer,Integer>> mapa_conteo;
    private Map<Integer,BigDecimal> porcentajes_PF;
    private int TOTAL_PF;
    private BigDecimal TOTAL_POCENTAJES;
    
    private SweTipoDetret det;
    private SweTipoDetret retFtr;
    private SweCata tipoComponente_actual;
    
    private int indicePanel = 0;
    private String tituloDialog;
    
    /*Esta bandera habilita o desabilita el boton terminar
     * en el dialog de la pestaña PF No Ajustados*/
    private boolean disable_btnPF = false; 
    
    private SweCompXsoli componenteSelecionado;
    
    public BeanPFNoAjustados() {
        this.controller = new Controller();
        inicializar_listas();
    }

    
    /**
     * Este metodo inicializa las listas y el mapa
     * 
     */
    private void inicializar_listas(){
        this.list_TipoComponentes = this.controller.listarPOR("Tipo De Componente");
        this.list_Complejidades = this.controller.listarPOR("Complejidad");
        this.mapa_pesosVigentes = this.controller.listar_pesos();
        this.det = this.controller.buscar_funcion("DET");
        
        reiniciarValores();
    }
    
    public void reiniciarValores(){
        this.mapa_listas = new HashMap<Integer,List<SweCompXsoli>>(5);  
        this.list_puntosFuncionales = new ArrayList<SweCompXsoli>();   
        this.mapa_conteo = new HashMap<Integer, Map<Integer, Integer>>();
        this.porcentajes_PF = new HashMap<Integer, BigDecimal>();
        this.TOTAL_PF = 0;
        TOTAL_POCENTAJES = BigDecimal.ZERO;
    }
    
    /*Este método carga los tipos de componente por solicitud, dependiendo 
     del tipo de componente recibido como parametro. Estos datos se extraen de un 
     HasMap y son cargados a un List*/
    public void agregarPuntoFuncion(SweCata tipoComponente){
        list_puntosFuncionales = new ArrayList<SweCompXsoli>();
        if(mapa_listas.containsKey(tipoComponente.getIdCatalogo())){
            this.list_puntosFuncionales = this.getMapa_listas().get(tipoComponente.getIdCatalogo()); 
        }else{
            this.list_puntosFuncionales = new ArrayList<SweCompXsoli>();
            mapa_listas.put(tipoComponente.getIdCatalogo(), list_puntosFuncionales);
        }       
        tipoComponente_actual = tipoComponente;
        this.retFtr = (tipoComponente.getNombre().equals("ILF") || tipoComponente.getNombre().equals("EIF") ? this.controller.buscar_funcion("RET") : this.controller.buscar_funcion("FTR"));
        this.tituloDialog = tipoComponente.getNombre()+" - "+tipoComponente.getDescripcion();
    }
    
    
    /*Este metodo consulta la complejidad de acuerdo a la cantidad de Det's y Ret/Ftr
      escritos para un punto de función especificos.*/
    public void ajaxConsultarComplejidad(int indice){
        try{    
            if(this.list_puntosFuncionales.get(indice).getCantidadDet()<0){
                disable_btnPF = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El número de Det's no puede ser negativo. Por favor rectifique"));
            }else if(this.list_puntosFuncionales.get(indice).getCantidadRetftr()<0){
                disable_btnPF = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El número de Ret/Ftr no puede ser negativo. Por favor rectifique"));
            }else{
                SweCata complejidad = this.controller.consultarComp_puntoFuncion(this.list_puntosFuncionales.get(indice)
                        .getSweCataByIdTpocomponente().getIdCatalogo(), det.getCodigoTipoDetret(), 
                        retFtr.getCodigoTipoDetret(), this.list_puntosFuncionales.get(indice).getCantidadDet(), 
                        this.list_puntosFuncionales.get(indice).getCantidadRetftr());

                this.list_puntosFuncionales.get(indice).setSweCataByIdComplejidad(complejidad);         
                this.list_puntosFuncionales.get(indice).setPeso(this.mapa_pesosVigentes.get(this.list_puntosFuncionales.get(indice).getSweCataByIdTpocomponente().getIdCatalogo()).get(complejidad.getIdCatalogo())); 
                disable_btnPF = false;
                
            }
        }catch(NullPointerException e){
            disable_btnPF = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "No se logró establecer una complejidad con el número de det y ret/frt establecidos"));
        }
        
    }
    
    /**
     * Este metodo se ejecuta cada ves que se presiona el boton para
     * agregar un nuevo elemento(punto de función)
     */
    public void ajaxAddRow(){
        list_puntosFuncionales.add(new SweCompXsoli(0, tipoComponente_actual, new SweCata(), new SweSoli(), "", 0, 0, ""));
    }

    /**
     * Este metodo elminar una fila de la lista de componetes por solicitud
     */
    public void contextMenuDeleteRow(){
        if(componenteSelecionado !=null){
          
             /*Verificamos si el componente ya esta registrado para eliminarlo
              *de la base de datos*/
            if(componenteSelecionado.getIdcompXsolicitud() > 0){
                Object obj = this.controller.eliminar_ComponenteSolicitud(componenteSelecionado);
                if(obj instanceof Boolean){
                    list_puntosFuncionales.remove(componenteSelecionado);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "El componente ha sido borrado"));
                }else{
                      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El componente no pudo ser borrado"));
                }
            }else{
                list_puntosFuncionales.remove(componenteSelecionado);
            }
        } 
        componenteSelecionado = null;
    }
    
    public void actualizarConteo(){
        
        /**
         * A continuación se verifica que la lista de puntos funcionales 
         * no este vacia actualizar los valores
         */        
        actualizarValores();

        /*A continuación Actualizamos el valor de UPF en el Bean que tiene
         los resultados de la estimación*/
        FacesContext context = FacesContext.getCurrentInstance();
        BeanEstimacion estimacion = (BeanEstimacion) context.getApplication().evaluateExpressionGet(context,"#{beanEstimacion}", Object.class);
        estimacion.getEstimacionSolicitud().setUpf(new BigDecimal(TOTAL_PF).setScale(2, RoundingMode.UP));            
        estimacion.setEstados_estimacion((byte)1);  // Se ha modificado la estimación y no se han guardado los cambios
       
        
    }
    
    public void actualizarValores(){
        Map<Integer, Integer> conteo = new HashMap<Integer, Integer>();
        this.mapa_conteo.put(tipoComponente_actual.getIdCatalogo(), conteo);            

        for (int i = 0; i < this.list_puntosFuncionales.size(); i++) {

                if(conteo.containsKey(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo())){
                   Integer val = conteo.get(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo());                   
                   val++;
                   conteo.remove(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo());
                   conteo.put(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo(), val);
                }else{
                    conteo.put(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo(), new Integer(1));
                }

        }
        this.TOTAL_PF = 0;
        int valores = 0;
        List<Integer> subTotales = new ArrayList<Integer>();
        for (int i = 0; i < this.list_TipoComponentes.size(); i++) {
            for (int j = 0; j < this.list_Complejidades.size(); j++) {
                try{
                    valores += mapa_conteo.get(this.list_TipoComponentes.get(i).getIdCatalogo()).get(this.list_Complejidades.get(j).getIdCatalogo()) 
                            * mapa_pesosVigentes.get(list_TipoComponentes.get(i).getIdCatalogo()).get(list_Complejidades.get(j).getIdCatalogo());                        
                }catch(NullPointerException e){}
            }
            subTotales.add(valores);
            this.TOTAL_PF += valores;
            valores = 0;
        }

        /**
         * Actualizamos los porcentajes de los puntos de función por componente
         */
        this.porcentajes_PF.clear();
        this.TOTAL_POCENTAJES = BigDecimal.ZERO;
        for (int i = 0; i < subTotales.size(); i++) { 
            BigDecimal n = BigDecimal.ZERO;
            if(TOTAL_PF != 0){
                n = BigDecimal.valueOf((double)subTotales.get(i)).divide(BigDecimal.valueOf(TOTAL_PF), 5, RoundingMode.FLOOR);
                n = n.multiply(BigDecimal.valueOf(100));
                n = n.divide(BigDecimal.ONE, 1, RoundingMode.FLOOR); 
            }            
            this.porcentajes_PF.put(this.list_TipoComponentes.get(i).getIdCatalogo(), n); 
            this.TOTAL_POCENTAJES = this.TOTAL_POCENTAJES.add(n).setScale(1,RoundingMode.UP); 
            
        }        
        this.TOTAL_POCENTAJES = this.TOTAL_POCENTAJES.divide(BigDecimal.ONE, 0, RoundingMode.UP);
    }
    
    /*Este metodo verifica que los elementos contenidos en la
     lista no esten vacios. Si hay alguno vacio pregunta si se desea eliminar el registro o completarlo,
     de lo contrario se cirra el dialog y se agregan los elementos al HashMap*/
    public void bntTerminar(){
        boolean bandera = false;
        int i;
        String msjAviso = "";
        for (i=0; i < this.list_puntosFuncionales.size(); i++) {
            if(this.list_puntosFuncionales.get(i).getNombre()==null 
                    || this.list_puntosFuncionales.get(i).getNombre().equals("")){
                msjAviso = "El punto de función número "+ (i+1) + " de la lista no tiene nombre. Complete el registro o eliminelo para poder continuar";
                bandera = true;
                break;
            }else if(this.list_puntosFuncionales.get(i).getSweCataByIdComplejidad().getIdCatalogo()==0){
                msjAviso = "El punto de función número "+ (i+1) + " de la lista no tiene complejidad asociada. Ingrese la cantidad de Det y Ret/Ftr o elimine la fila para continuar";
                bandera = true;
                break;
            }
        }
        if(bandera){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!",msjAviso));
        }else{
            RequestContext.getCurrentInstance().execute("dialog.hide()");
        }
    }
    /**
     * @return the list_puntosFuncionales
     */
    public List<SweCompXsoli> getList_puntosFuncionales() {
        return list_puntosFuncionales;
    }

    /**
     * @param list_puntosFuncionales the list_puntosFuncionales to set
     */
    public void setList_puntosFuncionales(List<SweCompXsoli> list_puntosFuncionales) {
        this.list_puntosFuncionales = list_puntosFuncionales;
    }

    /**
     * @return the tituloDialog
     */
    public String getTituloDialog() {
        return tituloDialog;
    }

    /**
     * @param tituloDialog the tituloDialog to set
     */
    public void setTituloDialog(String tituloDialog) {
        this.tituloDialog = tituloDialog;
    }

    /**
     * @return the list_TipoComponentes
     */
    public List<SweCata> getList_TipoComponentes() {
        return list_TipoComponentes;
    }

    /**
     * @param list_TipoComponentes the list_TipoComponentes to set
     */
    public void setList_TipoComponentes(List<SweCata> list_TipoComponentes) {
        this.list_TipoComponentes = list_TipoComponentes;
    }

    /**
     * @return the list_Complejidades
     */
    public List<SweCata> getList_Complejidades() {
        return list_Complejidades;
    }

    /**
     * @param list_Complejidades the list_Complejidades to set
     */
    public void setList_Complejidades(List<SweCata> list_Complejidades) {
        this.list_Complejidades = list_Complejidades;
    }

    /**
     * @return the indicePanel
     */
    public int getIndicePanel() {
        indicePanel++;
        return indicePanel-1;
    }

    /**
     * @param indicePanel the indicePanel to set
     */
    public void setIndicePanel(int indicePanel) {
        this.indicePanel = indicePanel;
    }

    /**
     * @return the mapa_pesosVigentes
     */
    public Map<Integer,Map<Integer,Integer>> getMapa_pesosVigentes() {
        return mapa_pesosVigentes;
    }

    /**
     * @param mapa_pesosVigentes the mapa_pesosVigentes to set
     */
    public void setMapa_pesosVigentes(Map<Integer,Map<Integer,Integer>> mapa_pesosVigentes) {
        this.mapa_pesosVigentes = mapa_pesosVigentes;
    }

    /**
     * @return the mapa_conteo
     */
    public Map<Integer,Map<Integer,Integer>> getMapa_conteo() {
        return mapa_conteo;
    }

    /**
     * @param mapa_conteo the mapa_conteo to set
     */
    public void setMapa_conteo(Map<Integer,Map<Integer,Integer>> mapa_conteo) {
        this.mapa_conteo = mapa_conteo;
    }

    /**
     * @return the porcentajes_PF
     */
    public Map<Integer,BigDecimal> getPorcentajes_PF() {
        return porcentajes_PF;
    }

    /**
     * @param porcentajes_PF the porcentajes_PF to set
     */
    public void setPorcentajes_PF(Map<Integer,BigDecimal> porcentajes_PF) {
        this.porcentajes_PF = porcentajes_PF;
    }

    /**
     * @return the TOTAL_PF
     */
    public int getTOTAL_PF() {
        return TOTAL_PF;
    }

    /**
     * @param TOTAL_PF the TOTAL_PF to set
     */
    public void setTOTAL_PF(int TOTAL_PF) {
        this.TOTAL_PF = TOTAL_PF;
    }

    /**
     * @return the TOTAL_POCENTAJES
     */
    public BigDecimal getTOTAL_POCENTAJES() {
        return TOTAL_POCENTAJES;
    }

    /**
     * @param TOTAL_POCENTAJES the TOTAL_POCENTAJES to set
     */
    public void setTOTAL_POCENTAJES(BigDecimal TOTAL_POCENTAJES) {
        this.TOTAL_POCENTAJES = TOTAL_POCENTAJES;
    }


    /**
     * @return the mapa_listas
     */
    public Map<Integer, List<SweCompXsoli>> getMapa_listas() {
        return mapa_listas;
    }

    /**
     * @param mapa_listas the mapa_listas to set
     */
    public void setMapa_listas(Map<Integer, List<SweCompXsoli>> mapa_listas) {
        this.mapa_listas = mapa_listas;
    }

    /**
     * @return the componenteSelecionado
     */
    public SweCompXsoli getComponenteSelecionado() {
        return componenteSelecionado;
    }

    /**
     * @param componenteSelecionado the componenteSelecionado to set
     */
    public void setComponenteSelecionado(SweCompXsoli componenteSelecionado) {
        this.componenteSelecionado = componenteSelecionado;
    }

    /**
     * @return the tipoComponente_actual
     */
    public SweCata getTipoComponente_actual() {
        return tipoComponente_actual;
    }

    /**
     * @param tipoComponente_actual the tipoComponente_actual to set
     */
    public void setTipoComponente_actual(SweCata tipoComponente_actual) {
        this.tipoComponente_actual = tipoComponente_actual;
    }

    /**
     * @return the disable_btnPF
     */
    public boolean isDisable_btnPF() {
        return disable_btnPF;
    }

    /**
     * @param disable_btnPF the disable_btnPF to set
     */
    public void setDisable_btnPF(boolean disable_btnPF) {
        this.disable_btnPF = disable_btnPF;
    }
}

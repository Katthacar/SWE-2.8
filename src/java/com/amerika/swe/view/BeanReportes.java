/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweUsuarios;
import com.amerika.swe.model.util.ReporteHoras;
import com.amerika.swe.util.DataBaseConnector;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanReportes implements Serializable {

    private Controller controller;
    private List<ReporteHoras> list_exportar;    
    private ReporteHoras seleccion_TipoSol;
    private Date fechaInicial;
    private Date fechaFinal;
    private final Date FECHA_ACTUAL = new Date();

    public BeanReportes() {
        this.controller = new Controller();
    }

    public void bnt_hrSolCerradas() {
        list_exportar = this.controller.consultarReporte(fechaInicial, fechaFinal);
        if(list_exportar.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Mensaje!", "La consulta no arrojó resultados"));
        }
    }

    public void actualizarHorasSWE() {
        Object obj = this.controller.actualizarHorasSWE(fechaInicial, fechaFinal);
        if (obj instanceof Integer) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "Se han actualizado las horas de " + obj + " estimaciones"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El proceso de actualización de horas no se pudo completar"));
        }
    }


    public void procesarPDF() throws JRException {

        if (list_exportar instanceof List && !list_exportar.isEmpty()) {
            
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            Map<String, Object> parametros = new HashMap<String, Object>();
            
            String os = System.getProperty("os.name", "generic").toLowerCase();            
            String ruta = ctx.getExternalContext().getRealPath("WEB-INF/reportes/");
            // Si es windows modificamos la ruta
            if(os.contains("windows")){ 
                ruta = ruta + "\\";
            }else if(os.contains("linux")){
                ruta = ruta + "/";
            }  
            parametros.put("autor", "" + ((SweUsuarios)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario")).getNombre());
            parametros.put("fechaInicial", this.fechaInicial);
            parametros.put("fechaFinal", this.fechaFinal);
            parametros.put("urlSubReporte", ruta);
            parametros.put("logoamerika", ctx.getExternalContext().getRealPath("resources/images/amerika-PDF.png"));
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","inline; filename=\"Estimación.pdf\";");
//            response.setHeader("Content-Disposition", "attachment; filename=\"Estimacion.pdf\";"); //Para descargar el archivo directamente
            response.setHeader("title", "Reporte");
            DataBaseConnector connector = new DataBaseConnector();
            
            try {               
                ServletOutputStream out = response.getOutputStream();                
//              JasperReport reporte = (JasperReport) JRLoader.loadObject(ctx.getExternalContext().getRealPath("WEB-INF/reportes/reporteMaestro.jasper"));                
                File file = new File(ctx.getExternalContext().getRealPath("WEB-INF/reportes/reporteMaestro.jasper"));  
                JasperReport reporte = (JasperReport)JRLoader.loadObject(file);
                connector.Connection();
               
                Logger.getLogger("SWE").info(ruta); 
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, connector.getConexion());
               
                
                JRExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);                
                
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
                
                exporter.exportReport();  
                
                out.flush();               
                out.close();                
                ctx.responseComplete();
                
            } catch (Exception ex) {
                Logger.getLogger(BeanReportes.class.getName()).log(Level.WARNING,"Exceptiooooooooooooooooooooooooooooooon");               
                Logger.getLogger(BeanReportes.class.getName()).log(Level.WARNING, ex.getMessage());
            } finally {
                if (connector.getConexion() != null) {
                    connector.closeConnection();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "No hay datos para exportar"));
        }
    }

    /**
     * @return the list_exportar
     */
    public List<ReporteHoras> getList_exportar() {
        return list_exportar;
    }

    /**
     * @param list_exportar the list_exportar to set
     */
    public void setList_exportar(List<ReporteHoras> list_exportar) {
        this.list_exportar = list_exportar;
    }

    /**
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * @return the FECHA_ACTUAL
     */
    public Date getFECHA_ACTUAL() {
        return FECHA_ACTUAL;
    }

    /**
     * @return the seleccion_TipoSol
     */
    public ReporteHoras getSeleccion_TipoSol() {
        return seleccion_TipoSol;
    }

    /**
     * @param seleccion_TipoSol the seleccion_TipoSol to set
     */
    public void setSeleccion_TipoSol(ReporteHoras seleccion_TipoSol) {
        this.seleccion_TipoSol = seleccion_TipoSol;
    }
}

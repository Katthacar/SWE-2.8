/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.model.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 *
 * @author estudiante.proyectos
 */
public class ReporteHoras implements Serializable{
    
    private BigDecimal numEstimaciones;
    private BigDecimal idTipoSolicitud;
    private String tipoSolicitud;
    private BigDecimal hr_estimadas;
    private BigDecimal hr_reales;
    private BigDecimal pr_fce;
    private BigDecimal pr_fcr;
    private List<ReporteHorasEtapa> subReportHrEtapas;
    
    public ReporteHoras(BigDecimal numEstimaciones, BigDecimal idTipoSolicitud, String tipoSolicitud, BigDecimal hr_estimadas, BigDecimal hr_reales, BigDecimal pr_fce, BigDecimal pr_fcr) {
        this.numEstimaciones = numEstimaciones;
        this.idTipoSolicitud = idTipoSolicitud;
        this.tipoSolicitud = tipoSolicitud;
        this.hr_estimadas = hr_estimadas;
        this.hr_reales = hr_reales;
        this.pr_fce = pr_fce;
        this.pr_fcr = pr_fcr;
    }

    public ReporteHoras(BigDecimal numEstimaciones, BigDecimal idTipoSolicitud, String tipoSolicitud, BigDecimal hr_estimadas, BigDecimal hr_reales, BigDecimal pr_fce, BigDecimal pr_fcr, List<ReporteHorasEtapa> subReportHrEtapas) {
        this.numEstimaciones = numEstimaciones;
        this.idTipoSolicitud = idTipoSolicitud;
        this.tipoSolicitud = tipoSolicitud;
        this.hr_estimadas = hr_estimadas;
        this.hr_reales = hr_reales;
        this.pr_fce = pr_fce;
        this.pr_fcr = pr_fcr;
        this.subReportHrEtapas = subReportHrEtapas;
    }
    
    /**
     * @return the numEstimaciones
     */
    public BigDecimal getNumEstimaciones() {
        return numEstimaciones;
    }

    /**
     * @param numEstimaciones the numEstimaciones to set
     */
    public void setNumEstimaciones(BigDecimal numEstimaciones) {
        this.numEstimaciones = numEstimaciones;
    }

    /**
     * @return the tipoSolicitud
     */
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    /**
     * @param tipoSolicitud the tipoSolicitud to set
     */
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    /**
     * @return the hr_estimadas
     */
    public BigDecimal getHr_estimadas() {
        return hr_estimadas;
    }

    /**
     * @param hr_estimadas the hr_estimadas to set
     */
    public void setHr_estimadas(BigDecimal hr_estimadas) {
        this.hr_estimadas = hr_estimadas;
    }

    /**
     * @return the hr_reales
     */
    public BigDecimal getHr_reales() {
        return hr_reales;
    }

    /**
     * @param hr_reales the hr_reales to set
     */
    public void setHr_reales(BigDecimal hr_reales) {
        this.hr_reales = hr_reales;
    }

    /**
     * @return the pr_fce
     */
    public BigDecimal getPr_fce() {
        return pr_fce;
    }

    /**
     * @param pr_fce the pr_fce to set
     */
    public void setPr_fce(BigDecimal pr_fce) {
        this.pr_fce = pr_fce;
    }

    /**
     * @return the pr_fcr
     */
    public BigDecimal getPr_fcr() {
        return pr_fcr;
    }

    /**
     * @param pr_fcr the pr_fcr to set
     */
    public void setPr_fcr(BigDecimal pr_fcr) {
        this.pr_fcr = pr_fcr;
    }

    /**
     * @return the subReportHrEtapas
     */
    public List<ReporteHorasEtapa> getSubReportHrEtapas() {
        return subReportHrEtapas;
    }

    /**
     * @param subReportHrEtapas the subReportHrEtapas to set
     */
    public void setSubReportHrEtapas(List<ReporteHorasEtapa> subReportHrEtapas) {
        this.subReportHrEtapas = subReportHrEtapas;
    }

    /**
     * @return the idTipoSolicitud
     */
    public BigDecimal getIdTipoSolicitud() {
        return idTipoSolicitud;
    }

    /**
     * @param idTipoSolicitud the idTipoSolicitud to set
     */
    public void setIdTipoSolicitud(BigDecimal idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    public static class ReporteHorasEtapa{
        private BigDecimal idEtapa;
        private String etapa;
        private BigDecimal hr_estimadas;
        private BigDecimal hr_reales;
        private BigDecimal porcDistrHE;
        private BigDecimal porcDistrHR;
        
        public ReporteHorasEtapa() {
        }
        
        public ReporteHorasEtapa(BigDecimal idEtapa, String etapa, BigDecimal hr_estimadas, BigDecimal hr_reales, BigDecimal porcDistrHE, BigDecimal porcDistrHR) {
            this.idEtapa = idEtapa;
            this.etapa = etapa;
            this.hr_estimadas = hr_estimadas;
            this.hr_reales = hr_reales;
            this.porcDistrHE = porcDistrHE;
            this.porcDistrHR = porcDistrHR;
        }
        
        /**
         * @return the idEtapa
         */
        public BigDecimal getIdEtapa() {
            return idEtapa;
        }

        /**
         * @param idEtapa the idEtapa to set
         */
        public void setIdEtapa(BigDecimal idEtapa) {
            this.idEtapa = idEtapa;
        }

        /**
         * @return the etapa
         */
        public String getEtapa() {
            return etapa;
        }

        /**
         * @param etapa the etapa to set
         */
        public void setEtapa(String etapa) {
            this.etapa = etapa;
        }

        /**
         * @return the hr_estimadas
         */
        public BigDecimal getHr_estimadas() {
            return hr_estimadas;
        }

        /**
         * @param hr_estimadas the hr_estimadas to set
         */
        public void setHr_estimadas(BigDecimal hr_estimadas) {
            this.hr_estimadas = hr_estimadas;
        }

        /**
         * @return the hr_reales
         */
        public BigDecimal getHr_reales() {
            return hr_reales;
        }

        /**
         * @param hr_reales the hr_reales to set
         */
        public void setHr_reales(BigDecimal hr_reales) {
            this.hr_reales = hr_reales;
        }

        /**
         * @return the porcDistrHE
         */
        public BigDecimal getPorcDistrHE() {
            return porcDistrHE;
        }

        /**
         * @param porcDistrHE the porcDistrHE to set
         */
        public void setPorcDistrHE(BigDecimal porcDistrHE) {
            this.porcDistrHE = porcDistrHE;
        }

        /**
         * @return the porcDistrHR
         */
        public BigDecimal getPorcDistrHR() {
            return porcDistrHR;
        }

        /**
         * @param porcDistrHR the porcDistrHR to set
         */
        public void setPorcDistrHR(BigDecimal porcDistrHR) {
            this.porcDistrHR = porcDistrHR;
        }

       
    }
    
}

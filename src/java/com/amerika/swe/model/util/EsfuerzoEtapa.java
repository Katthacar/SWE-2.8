/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.model.util;

import com.amerika.swe.model.SwePorcDistXetapa;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author estudiante.proyectos
 */
public class EsfuerzoEtapa implements Serializable{

    private SwePorcDistXetapa porcentajeEstimado;
    private BigDecimal nHoras;
    private BigDecimal nDias;
    private BigDecimal nMeses;

    public EsfuerzoEtapa(SwePorcDistXetapa porcentaEstimado) {
        this.porcentajeEstimado = porcentaEstimado;
    }

    public EsfuerzoEtapa(SwePorcDistXetapa porcentaEstimado, BigDecimal nHoras, BigDecimal nDias, BigDecimal nMeses) {
        this.porcentajeEstimado = porcentaEstimado;        
        this.nHoras = nHoras;
        this.nDias = nDias;
        this.nMeses = nMeses;
    }

    /**
     * @return the porcentajeEstimado
     */
    public SwePorcDistXetapa getPorcentajeEstimado() {
        return porcentajeEstimado;
    }

    /**
     * @param porcentajeEstimado the porcentajeEstimado to set
     */
    public void setPorcentajeEstimado(SwePorcDistXetapa porcentajeEstimado) {
        this.porcentajeEstimado = porcentajeEstimado;
    }

    /**
     * @return the nHoras
     */
    public BigDecimal getnHoras() {
        return nHoras;
    }

    /**
     * @param nHoras the nHoras to set
     */
    public void setnHoras(BigDecimal nHoras) {
        this.nHoras = nHoras;
    }

    /**
     * @return the nDias
     */
    public BigDecimal getnDias() {
        return nDias;
    }

    /**
     * @param nDias the nDias to set
     */
    public void setnDias(BigDecimal nDias) {
        this.nDias = nDias;
    }

    /**
     * @return the nMeses
     */
    public BigDecimal getnMeses() {
        return nMeses;
    }

    /**
     * @param nMeses the nMeses to set
     */
    public void setnMeses(BigDecimal nMeses) {
        this.nMeses = nMeses;
    }

}

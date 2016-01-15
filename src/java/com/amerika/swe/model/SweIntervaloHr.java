/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.amerika.swe.model;

import java.math.BigDecimal;

/**
 *
 * @author anders.barrios
 */
public class SweIntervaloHr implements java.io.Serializable{
    private int idIntervaloHr;
    private SweIntervalo sweIntervalo;
    private BigDecimal horas;    

    public SweIntervaloHr() {
    }

    public SweIntervaloHr(int idIntervaloHr, SweIntervalo sweIntervalo, BigDecimal horas) {
        this.idIntervaloHr = idIntervaloHr;
        this.sweIntervalo = sweIntervalo;
        this.horas = horas;
    }

    /**
     * @return the idIntervaloHr
     */
    public int getIdIntervaloHr() {
        return idIntervaloHr;
    }

    /**
     * @param idIntervaloHr the idIntervaloHr to set
     */
    public void setIdIntervaloHr(int idIntervaloHr) {
        this.idIntervaloHr = idIntervaloHr;
    }

    /**
     * @return the sweIntervalo
     */
    public SweIntervalo getSweIntervalo() {
        return sweIntervalo;
    }

    /**
     * @param sweIntervalo the sweIntervalo to set
     */
    public void setSweIntervalo(SweIntervalo sweIntervalo) {
        this.sweIntervalo = sweIntervalo;
    }

    /**
     * @return the horas
     */
    public BigDecimal getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }
    
    
}

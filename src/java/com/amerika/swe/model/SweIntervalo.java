/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author anders.barrios
 */
public class SweIntervalo implements java.io.Serializable {

    private int idIntervalo;
    private SweCata sweCataByIdLenguaje;
    private SweCata sweCataByIdPerfil;
    private BigDecimal valorInicial;
    private BigDecimal valorFinal;
    private Date fechaModif;
    private String usuario;
    private Set sweIntervaloHrs = new HashSet(0);

    public SweIntervalo() {
    }

    public SweIntervalo(int idIntervalo, SweCata sweCataByIdLenguaje, SweCata sweCataByIdPerfil, BigDecimal valorInicial, BigDecimal valorFinal, Date fechaModif, String usuario, Set sweIntervaloHrs) {
        this.idIntervalo = idIntervalo;
        this.sweCataByIdLenguaje = sweCataByIdLenguaje;
        this.sweCataByIdPerfil = sweCataByIdPerfil;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
        this.fechaModif = fechaModif;
        this.usuario = usuario;
        this.sweIntervaloHrs = sweIntervaloHrs;
    }

    /**
     * @return the idIntervalo
     */
    public int getIdIntervalo() {
        return idIntervalo;
    }

    /**
     * @param idIntervalo the idIntervalo to set
     */
    public void setIdIntervalo(int idIntervalo) {
        this.idIntervalo = idIntervalo;
    }

    /**
     * @return the sweCataByIdLenguaje
     */
    public SweCata getSweCataByIdLenguaje() {
        return sweCataByIdLenguaje;
    }

    /**
     * @param sweCataByIdLenguaje the sweCataByIdLenguaje to set
     */
    public void setSweCataByIdLenguaje(SweCata sweCataByIdLenguaje) {
        this.sweCataByIdLenguaje = sweCataByIdLenguaje;
    }

    /**
     * @return the sweCataByIdPerfil
     */
    public SweCata getSweCataByIdPerfil() {
        return sweCataByIdPerfil;
    }

    /**
     * @param sweCataByIdPerfil the sweCataByIdPerfil to set
     */
    public void setSweCataByIdPerfil(SweCata sweCataByIdPerfil) {
        this.sweCataByIdPerfil = sweCataByIdPerfil;
    }

    /**
     * @return the valorInicial
     */
    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    /**
     * @param valorInicial the valorInicial to set
     */
    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    /**
     * @return the valorFinal
     */
    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    /**
     * @param valorFinal the valorFinal to set
     */
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    /**
     * @return the fechaModif
     */
    public Date getFechaModif() {
        return fechaModif;
    }

    /**
     * @param fechaModif the fechaModif to set
     */
    public void setFechaModif(Date fechaModif) {
        this.fechaModif = fechaModif;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the SweIntervaloHrs
     */
    public Set getSweIntervaloHrs() {
        return sweIntervaloHrs;
    }

    /**
     * @param sweIntervaloHrs the SweIntervaloHrs to set
     */
    public void setSweIntervaloHrs(Set sweIntervaloHrs) {
        this.sweIntervaloHrs = sweIntervaloHrs;
    }

}

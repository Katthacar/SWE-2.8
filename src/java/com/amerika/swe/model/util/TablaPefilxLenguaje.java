/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.model.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase me permite mostar la información asociada entre los perfiles
 * y los lenguajes
 *
 * @author estudiante.proyectos
 */
public class TablaPefilxLenguaje implements Serializable{
    private int id;
    private String lenguaje;
    private BigDecimal horas_senior;
    private BigDecimal horas_semiSenior;
    private BigDecimal horas_junior;
    private Date vigencia_inicial;

    public TablaPefilxLenguaje() {
    }

    
    
    public TablaPefilxLenguaje(int id, String lenguaje, BigDecimal horas_senior, BigDecimal horas_semiSenior, BigDecimal horas_junior, Date vigencia_inicial) {
        this.id = id;
        this.lenguaje = lenguaje;
        this.horas_senior = horas_senior;
        this.horas_semiSenior = horas_semiSenior;
        this.horas_junior = horas_junior;
        this.vigencia_inicial = vigencia_inicial;
    }

    
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the lenguaje
     */
    public String getLenguaje() {
        return lenguaje;
    }

    /**
     * @param lenguaje the lenguaje to set
     */
    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    /**
     * @return the horas_senior
     */
    public BigDecimal getHoras_senior() {
        return horas_senior;
    }

    /**
     * @param horas_senior the horas_senior to set
     */
    public void setHoras_senior(BigDecimal horas_senior) {
        this.horas_senior = horas_senior;
    }

    /**
     * @return the horas_semiSenior
     */
    public BigDecimal getHoras_semiSenior() {
        return horas_semiSenior;
    }

    /**
     * @param horas_semiSenior the horas_semiSenior to set
     */
    public void setHoras_semiSenior(BigDecimal horas_semiSenior) {
        this.horas_semiSenior = horas_semiSenior;
    }

    /**
     * @return the horas_junior
     */
    public BigDecimal getHoras_junior() {
        return horas_junior;
    }

    /**
     * @param horas_junior the horas_junior to set
     */
    public void setHoras_junior(BigDecimal horas_junior) {
        this.horas_junior = horas_junior;
    }

    /**
     * @return the vigencia_inicial
     */
    public Date getVigencia_inicial() {
        return vigencia_inicial;
    }

    /**
     * @param vigencia_inicial the vigencia_inicial to set
     */
    public void setVigencia_inicial(Date vigencia_inicial) {
        this.vigencia_inicial = vigencia_inicial;
    }
}

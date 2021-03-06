package com.amerika.swe.model;
// Generated 10/09/2013 09:52:41 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * SwePorcDistXetapa generated by hbm2java
 */
public class SwePorcDistXetapa  implements java.io.Serializable {


     private int idDistetapa;
     private SweCata sweCata;
     private BigDecimal porcentaje;
     private Date vigenciaInicial;
     private Date vigenciaFinal;

    public SwePorcDistXetapa() {
    }

	
    public SwePorcDistXetapa(int idDistetapa, SweCata sweCata, BigDecimal porcentaje, Date vigenciaInicial) {
        this.idDistetapa = idDistetapa;
        this.sweCata = sweCata;
        this.porcentaje = porcentaje;
        this.vigenciaInicial = vigenciaInicial;
    }
    public SwePorcDistXetapa(int idDistetapa, SweCata sweCata, BigDecimal porcentaje, Date vigenciaInicial, Date vigenciaFinal) {
       this.idDistetapa = idDistetapa;
       this.sweCata = sweCata;
       this.porcentaje = porcentaje;
       this.vigenciaInicial = vigenciaInicial;
       this.vigenciaFinal = vigenciaFinal;
    }
   
    public int getIdDistetapa() {
        return this.idDistetapa;
    }
    
    public void setIdDistetapa(int idDistetapa) {
        this.idDistetapa = idDistetapa;
    }
    public SweCata getSweCata() {
        return this.sweCata;
    }
    
    public void setSweCata(SweCata sweCata) {
        this.sweCata = sweCata;
    }
    public BigDecimal getPorcentaje() {
        return this.porcentaje;
    }
    
    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
    public Date getVigenciaInicial() {
        return this.vigenciaInicial;
    }
    
    public void setVigenciaInicial(Date vigenciaInicial) {
        this.vigenciaInicial = vigenciaInicial;
    }
    public Date getVigenciaFinal() {
        return this.vigenciaFinal;
    }
    
    public void setVigenciaFinal(Date vigenciaFinal) {
        this.vigenciaFinal = vigenciaFinal;
    }




}



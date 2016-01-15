package com.amerika.swe.model;
// Generated 10/09/2013 09:52:41 AM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;




/**
 * SweFactAmbiXsol generated by hbm2java
 */
public class SweFactAmbiXsol  implements java.io.Serializable {


     private SweFactAmbiXsolId id;
     private SweCata sweCata;
     private SweSoli sweSoli;
     private Integer influencia;
     private BigDecimal peso;
     
    public SweFactAmbiXsol() {
    }

	
    public SweFactAmbiXsol(SweFactAmbiXsolId id, SweCata sweCata, SweSoli sweSoli, BigDecimal peso) {
        this.id = id;
        this.sweCata = sweCata;
        this.sweSoli = sweSoli;
        this.peso = peso;
    }
    public SweFactAmbiXsol(SweFactAmbiXsolId id, SweCata sweCata, SweSoli sweSoli, BigDecimal peso, Integer influencia) {
       this.id = id;
       this.sweCata = sweCata;
       this.sweSoli = sweSoli;
       this.influencia = influencia;
       this.peso = peso;
    }
   
    public SweFactAmbiXsolId getId() {
        return this.id;
    }
    
    public void setId(SweFactAmbiXsolId id) {
        this.id = id;
    }
    public SweCata getSweCata() {
        return this.sweCata;
    }
    
    public void setSweCata(SweCata sweCata) {
        this.sweCata = sweCata;
    }
    public SweSoli getSweSoli() {
        return this.sweSoli;
    }
    
    public void setSweSoli(SweSoli sweSoli) {
        this.sweSoli = sweSoli;
    }

    /**
     * @return the peso
     */
    public BigDecimal getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    /**
     * @return the influencia
     */
    public Integer getInfluencia() {
        return influencia;
    }

    /**
     * @param influencia the influencia to set
     */
    public void setInfluencia(Integer influencia) {
        this.influencia = influencia;
    }




}



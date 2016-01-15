package com.amerika.swe.model;
// Generated 18/10/2013 09:22:21 AM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * SweEtapaXsol generated by hbm2java
 */
public class SweEtapaXsol  implements java.io.Serializable {


     private SweEtapaXsolId id;
     private SweCata sweCata;
     private SweSoli sweSoli;
     private BigDecimal horasEstimadas;
     private BigDecimal horasReales;
     private BigDecimal porcenEstimado;
     private BigDecimal porcenReal;

    public SweEtapaXsol() {
    }

	
    public SweEtapaXsol(SweEtapaXsolId id, SweCata sweCata, SweSoli sweSoli, BigDecimal horasEstimadas, BigDecimal porcenEstimado) {
        this.id = id;
        this.sweCata = sweCata;
        this.sweSoli = sweSoli;
        this.horasEstimadas = horasEstimadas;
        this.porcenEstimado = porcenEstimado;
    }
    public SweEtapaXsol(SweEtapaXsolId id, SweCata sweCata, SweSoli sweSoli, BigDecimal horasEstimadas, BigDecimal horasReales, BigDecimal porcenEstimado, BigDecimal porcenReal) {
       this.id = id;
       this.sweCata = sweCata;
       this.sweSoli = sweSoli;
       this.horasEstimadas = horasEstimadas;
       this.horasReales = horasReales;
       this.porcenEstimado = porcenEstimado;
       this.porcenReal = porcenReal;
    }
   
    public SweEtapaXsolId getId() {
        return this.id;
    }
    
    public void setId(SweEtapaXsolId id) {
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
    public BigDecimal getHorasEstimadas() {
        return this.horasEstimadas;
    }
    
    public void setHorasEstimadas(BigDecimal horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }
    public BigDecimal getHorasReales() {
        return this.horasReales;
    }
    
    public void setHorasReales(BigDecimal horasReales) {
        this.horasReales = horasReales;
    }
    public BigDecimal getPorcenEstimado() {
        return this.porcenEstimado;
    }
    
    public void setPorcenEstimado(BigDecimal porcenEstimado) {
        this.porcenEstimado = porcenEstimado;
    }
    public BigDecimal getPorcenReal() {
        return this.porcenReal;
    }
    
    public void setPorcenReal(BigDecimal porcenReal) {
        this.porcenReal = porcenReal;
    }




}



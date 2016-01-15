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
public class SwePronostico implements java.io.Serializable{
    private int idPronostico;
    private SweSoli sweSoli;
    private BigDecimal hrPronostico;
    private BigDecimal error;
    private BigDecimal errorCuadratico;

    public SwePronostico() {
    }

    public SwePronostico(int idPronostico, SweSoli sweSoli, BigDecimal hrPronostico) {
        this.idPronostico = idPronostico;
        this.sweSoli = sweSoli;
        this.hrPronostico = hrPronostico;
    }

    public SwePronostico(int idPronostico, SweSoli sweSoli, BigDecimal hrPronostico, BigDecimal error, BigDecimal errorCuadratico) {
        this.idPronostico = idPronostico;
        this.sweSoli = sweSoli;
        this.hrPronostico = hrPronostico;
        this.error = error;
        this.errorCuadratico = errorCuadratico;
    }    
    
    /**
     * @return the idPronostico
     */
    public int getIdPronostico() {
        return idPronostico;
    }

    /**
     * @param idPronostico the idPronostico to set
     */
    public void setIdPronostico(int idPronostico) {
        this.idPronostico = idPronostico;
    }

    /**
     * @return the sweSoli
     */
    public SweSoli getSweSoli() {
        return sweSoli;
    }

    /**
     * @param sweSoli the sweSoli to set
     */
    public void setSweSoli(SweSoli sweSoli) {
        this.sweSoli = sweSoli;
    }

    /**
     * @return the hrPronostico
     */
    public BigDecimal getHrPronostico() {
        return hrPronostico;
    }

    /**
     * @param hrPronostico the hrPronostico to set
     */
    public void setHrPronostico(BigDecimal hrPronostico) {
        this.hrPronostico = hrPronostico;
    }

    /**
     * @return the error
     */
    public BigDecimal getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(BigDecimal error) {
        this.error = error;
    }

    /**
     * @return the errorCuadratico
     */
    public BigDecimal getErrorCuadratico() {
        return errorCuadratico;
    }

    /**
     * @param errorCuadratico the errorCuadratico to set
     */
    public void setErrorCuadratico(BigDecimal errorCuadratico) {
        this.errorCuadratico = errorCuadratico;
    }
}

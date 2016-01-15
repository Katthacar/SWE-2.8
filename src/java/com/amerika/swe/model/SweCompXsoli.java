package com.amerika.swe.model;
// Generated 10/09/2013 09:52:41 AM by Hibernate Tools 3.2.1.GA



/**
 * SweCompXsoli generated by hbm2java
 */
public class SweCompXsoli  implements java.io.Serializable {


     private int idcompXsolicitud;
     private SweCata sweCataByIdTpocomponente;
     private SweCata sweCataByIdComplejidad;
     private SweSoli sweSoli;
     private String nombre;
     private int cantidadDet;
     private int cantidadRetftr;
     private String observacion;
     private int peso;

    public SweCompXsoli() {
    }

	
    public SweCompXsoli(int idcompXsolicitud, SweCata sweCataByIdTpocomponente, SweCata sweCataByIdComplejidad, SweSoli sweSoli, String nombre, int cantidadDet, int cantidadRetftr, String observacion) {
        this.idcompXsolicitud = idcompXsolicitud;
        this.sweCataByIdTpocomponente = sweCataByIdTpocomponente;
        this.sweCataByIdComplejidad = sweCataByIdComplejidad;
        this.sweSoli = sweSoli;
        this.nombre = nombre;
        this.cantidadDet = cantidadDet;
        this.cantidadRetftr = cantidadRetftr;
        this.observacion = observacion;
    }
    public SweCompXsoli(int idcompXsolicitud, SweCata sweCataByIdTpocomponente, SweCata sweCataByIdComplejidad, SweSoli sweSoli, String nombre, int cantidadDet, int cantidadRetftr, String observacion, int peso) {
       this.idcompXsolicitud = idcompXsolicitud;
       this.sweCataByIdTpocomponente = sweCataByIdTpocomponente;
       this.sweCataByIdComplejidad = sweCataByIdComplejidad;
       this.sweSoli = sweSoli;
       this.nombre = nombre;
       this.cantidadDet = cantidadDet;
       this.cantidadRetftr = cantidadRetftr;
       this.observacion = observacion;
       this.peso = peso;
    }
   
    public int getIdcompXsolicitud() {
        return this.idcompXsolicitud;
    }
    
    public void setIdcompXsolicitud(int idcompXsolicitud) {
        this.idcompXsolicitud = idcompXsolicitud;
    }
    public SweCata getSweCataByIdTpocomponente() {
        return this.sweCataByIdTpocomponente;
    }
    
    public void setSweCataByIdTpocomponente(SweCata sweCataByIdTpocomponente) {
        this.sweCataByIdTpocomponente = sweCataByIdTpocomponente;
    }
    public SweCata getSweCataByIdComplejidad() {
        return this.sweCataByIdComplejidad;
    }
    
    public void setSweCataByIdComplejidad(SweCata sweCataByIdComplejidad) {
        this.sweCataByIdComplejidad = sweCataByIdComplejidad;
    }
    public SweSoli getSweSoli() {
        return this.sweSoli;
    }
    
    public void setSweSoli(SweSoli sweSoli) {
        this.sweSoli = sweSoli;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCantidadDet() {
        return this.cantidadDet;
    }
    
    public void setCantidadDet(int cantidadDet) {
        this.cantidadDet = cantidadDet;
    }
    public int getCantidadRetftr() {
        return this.cantidadRetftr;
    }
    
    public void setCantidadRetftr(int cantidadRetftr) {
        this.cantidadRetftr = cantidadRetftr;
    }
    public String getObservacion() {
        return this.observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    public int getPeso() {
        return this.peso;
    }
    
    public void setPeso(int peso) {
        this.peso = peso;
    }

}



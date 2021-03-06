package com.amerika.swe.model;
// Generated 26/11/2013 01:46:12 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SweMenu generated by hbm2java
 */
public class SweMenu  implements java.io.Serializable {


     private int codigo;
     private SweMenu sweMenu;
     private String usuario;
     private String programa;
     private Date fechaModif;
     private String nombre;
     private Integer orden;
     private String icono;
     private String urlMenu;
     private Set sweMenus = new HashSet(0);
     private Set sweMenuPerfils = new HashSet(0);

    public SweMenu() {
    }

	
    public SweMenu(int codigo, String usuario, String programa, Date fechaModif, String nombre) {
        this.codigo = codigo;
        this.usuario = usuario;
        this.programa = programa;
        this.fechaModif = fechaModif;
        this.nombre = nombre;
    }
    public SweMenu(int codigo, SweMenu sweMenu, String usuario, String programa, Date fechaModif, String nombre, Integer orden, String icono, String urlMenu, Set sweMenus, Set sweMenuPerfils) {
       this.codigo = codigo;
       this.sweMenu = sweMenu;
       this.usuario = usuario;
       this.programa = programa;
       this.fechaModif = fechaModif;
       this.nombre = nombre;
       this.orden = orden;
       this.icono = icono;
       this.urlMenu = urlMenu;
       this.sweMenus = sweMenus;
       this.sweMenuPerfils = sweMenuPerfils;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public SweMenu getSweMenu() {
        return this.sweMenu;
    }
    
    public void setSweMenu(SweMenu sweMenu) {
        this.sweMenu = sweMenu;
    }
    public String getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPrograma() {
        return this.programa;
    }
    
    public void setPrograma(String programa) {
        this.programa = programa;
    }
    public Date getFechaModif() {
        return this.fechaModif;
    }
    
    public void setFechaModif(Date fechaModif) {
        this.fechaModif = fechaModif;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Integer getOrden() {
        return this.orden;
    }
    
    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    public String getIcono() {
        return this.icono;
    }
    
    public void setIcono(String icono) {
        this.icono = icono;
    }
    public String getUrlMenu() {
        return this.urlMenu;
    }
    
    public void setUrlMenu(String urlMenu) {
        this.urlMenu = urlMenu;
    }
    public Set getSweMenus() {
        return this.sweMenus;
    }
    
    public void setSweMenus(Set sweMenus) {
        this.sweMenus = sweMenus;
    }
    public Set getSweMenuPerfils() {
        return this.sweMenuPerfils;
    }
    
    public void setSweMenuPerfils(Set sweMenuPerfils) {
        this.sweMenuPerfils = sweMenuPerfils;
    }




}



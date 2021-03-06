package com.amerika.swe.model;
// Generated 26/11/2013 01:46:12 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SweUsuarios generated by hbm2java
 */
public class SweUsuarios  implements java.io.Serializable {


     private String codigo;
     private String usuario;
     private String programa;
     private Date fechaModif;
     private String pass;
     private String nombre;
     private String telefono;
     private String estado;
     private Date fechaAlta;
     private Date fechaBaja;
     private String email;
     private Set sweUsuarioPerfils = new HashSet(0);

    public SweUsuarios() {
    }

	
    public SweUsuarios(String codigo, String usuario, String programa, Date fechaModif, String pass, String nombre, String estado, Date fechaAlta) {
        this.codigo = codigo;
        this.usuario = usuario;
        this.programa = programa;
        this.fechaModif = fechaModif;
        this.pass = pass;
        this.nombre = nombre;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
    }
    public SweUsuarios(String codigo, String usuario, String programa, Date fechaModif, String pass, String nombre, String telefono, String estado, Date fechaAlta, Date fechaBaja, String email, Set sweUsuarioPerfils) {
       this.codigo = codigo;
       this.usuario = usuario;
       this.programa = programa;
       this.fechaModif = fechaModif;
       this.pass = pass;
       this.nombre = nombre;
       this.telefono = telefono;
       this.estado = estado;
       this.fechaAlta = fechaAlta;
       this.fechaBaja = fechaBaja;
       this.email = email;
       this.sweUsuarioPerfils = sweUsuarioPerfils;
    }
   
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    public String getPass() {
        return this.pass;
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public Date getFechaAlta() {
        return this.fechaAlta;
    }
    
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public Date getFechaBaja() {
        return this.fechaBaja;
    }
    
    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public Set getSweUsuarioPerfils() {
        return this.sweUsuarioPerfils;
    }
    
    public void setSweUsuarioPerfils(Set sweUsuarioPerfils) {
        this.sweUsuarioPerfils = sweUsuarioPerfils;
    }




}



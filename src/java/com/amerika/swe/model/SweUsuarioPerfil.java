package com.amerika.swe.model;
// Generated 26/11/2013 01:46:12 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * SweUsuarioPerfil generated by hbm2java
 */
public class SweUsuarioPerfil  implements java.io.Serializable {


     private SweUsuarioPerfilId id;
     private SwePerfil swePerfil;
     private SweUsuarios sweUsuarios;
     private String usuario;
     private String programa;
     private Date fechaModif;
     private Date fechaAlta;
     private Date fechaBaja;

    public SweUsuarioPerfil() {
    }

	
    public SweUsuarioPerfil(SweUsuarioPerfilId id, SwePerfil swePerfil, SweUsuarios sweUsuarios, String usuario, String programa, Date fechaModif, Date fechaAlta) {
        this.id = id;
        this.swePerfil = swePerfil;
        this.sweUsuarios = sweUsuarios;
        this.usuario = usuario;
        this.programa = programa;
        this.fechaModif = fechaModif;
        this.fechaAlta = fechaAlta;
    }
    public SweUsuarioPerfil(SweUsuarioPerfilId id, SwePerfil swePerfil, SweUsuarios sweUsuarios, String usuario, String programa, Date fechaModif, Date fechaAlta, Date fechaBaja) {
       this.id = id;
       this.swePerfil = swePerfil;
       this.sweUsuarios = sweUsuarios;
       this.usuario = usuario;
       this.programa = programa;
       this.fechaModif = fechaModif;
       this.fechaAlta = fechaAlta;
       this.fechaBaja = fechaBaja;
    }
   
    public SweUsuarioPerfilId getId() {
        return this.id;
    }
    
    public void setId(SweUsuarioPerfilId id) {
        this.id = id;
    }
    public SwePerfil getSwePerfil() {
        return this.swePerfil;
    }
    
    public void setSwePerfil(SwePerfil swePerfil) {
        this.swePerfil = swePerfil;
    }
    public SweUsuarios getSweUsuarios() {
        return this.sweUsuarios;
    }
    
    public void setSweUsuarios(SweUsuarios sweUsuarios) {
        this.sweUsuarios = sweUsuarios;
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




}


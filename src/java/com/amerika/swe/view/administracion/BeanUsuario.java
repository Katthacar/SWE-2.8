/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view.administracion;


import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweUsuarios;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanUsuario {

    private SweUsuarios usuarioAutentico;    
    private Controller controller;
    private String contrasena_anterior;
    private String contrasena_nueva1;
    private String contrasena_nueva2;
    
    public BeanUsuario() {
        this.controller = new Controller();
       usuarioAutentico = (SweUsuarios)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    
    public void cambiarContrasena(){
        
        if(contrasena_nueva1.equals(contrasena_nueva2)){        
            Object obj = this.controller.cambiarContrasena(usuarioAutentico.getCodigo(), contrasena_anterior,contrasena_nueva1); 
            if(obj instanceof Integer){
                RequestContext.getCurrentInstance().execute("dialog_pass.hide()"); 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje" , "Constraseña actualizada con éxito"));
            }else if(obj instanceof Boolean){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!" , "La Contraseña actual no coincide"));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!" , "Debido a un error no se pudo actualizar su contraseña"));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!" , "Las contraseñas no coinciden"));
        }
    }
    
    public void guardarCambios(){
        Object obj = this.controller.actualizarUsuario(usuarioAutentico);
        
        if(obj instanceof Boolean){
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuario");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioAutentico);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje" , "Actualizado con éxito"));
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!" , "No se pudo actualizar la información"));
        }
    }
    /**
     * @return the usuarioAutentico
     */
    public SweUsuarios getUsuarioAutentico() {
        return usuarioAutentico;
    }

    /**
     * @param usuarioAutentico the usuarioAutentico to set
     */
    public void setUsuarioAutentico(SweUsuarios usuarioAutentico) {
        this.usuarioAutentico = usuarioAutentico;
    }

 
    /**
     * @return the contrasena_anterior
     */
    public String getContrasena_anterior() {
        return contrasena_anterior;
    }

    /**
     * @param contrasena_anterior the contrasena_anterior to set
     */
    public void setContrasena_anterior(String contrasena_anterior) {
        this.contrasena_anterior = contrasena_anterior;
    }

    /**
     * @return the contrasena_nueva1
     */
    public String getContrasena_nueva1() {
        return contrasena_nueva1;
    }

    /**
     * @param contrasena_nueva1 the contrasena_nueva1 to set
     */
    public void setContrasena_nueva1(String contrasena_nueva1) {
        this.contrasena_nueva1 = contrasena_nueva1;
    }

    /**
     * @return the contrasena_nueva2
     */
    public String getContrasena_nueva2() {
        return contrasena_nueva2;
    }

    /**
     * @param contrasena_nueva2 the contrasena_nueva2 to set
     */
    public void setContrasena_nueva2(String contrasena_nueva2) {
        this.contrasena_nueva2 = contrasena_nueva2;
    }
    
}

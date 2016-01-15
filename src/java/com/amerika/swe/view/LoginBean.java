/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import com.amerika.swe.model.SweUsuarios;
import com.amerika.swe.controller.Controller;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import com.amerika.swe.util.MyUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author estudiante.soporte
 */

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{
    
    private String username;
    private String password;
    private SweUsuarios usuarioAutentico;
    
    private Controller controller;
          
    public LoginBean() {
        this.controller = new Controller();       
    }


    public void login(ActionEvent actionEvent) {  
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg;  
        boolean loggedIn = false;  
        String ruta = "";
        
        SweUsuarios usuario = this.controller.login(username,password);
          
        if(usuario != null) {
            if(usuario.getSweUsuarioPerfils().size()>0){
                usuarioAutentico = usuario;
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", usuario.getNombre()); 
                loggedIn = true;                
                ruta = MyUtil.basepathlogin()+"vistas/inicio.xhtml";    
            }else{
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje!", "Este usuario no tiene acceso al sistema"); 
            }        
        }else{             
            //RequestContext.getCurrentInstance().execute("estado_login.hide()");
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error!", "Usuario y/o clave incorrectos");
        }  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        context.addCallbackParam("loggedIn", loggedIn);  
        context.addCallbackParam("ruta", ruta);  
    }  
    
    public void logout(){          
                
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession sesion = (HttpSession) facesContext.getExternalContext().getSession(false);
        sesion.invalidate();
        usuarioAutentico = null;
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout", "Ha cerrado sesión"));  
        context.addCallbackParam("loggeOut", true);
        context.addCallbackParam("ruta", MyUtil.basepathlogin()+"login.xhtml");
        
         try {
            RequestContext.getCurrentInstance().execute("handleLoginRequest(xhr, status, args)");
            FacesContext.getCurrentInstance().getExternalContext().redirect(MyUtil.baseurl());
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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

}
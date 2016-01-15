/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@javax.faces.bean.ViewScoped       
public class ControlSession implements Serializable{

    public void ajax_inactivo(){        
        FacesContext context = FacesContext.getCurrentInstance();
        LoginBean estimacion = (LoginBean) context.getApplication().evaluateExpressionGet(context,"#{loginBean}", Object.class);
        estimacion.logout();  
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view.administracion;

import com.amerika.swe.controller.Controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanGestionPerfiles {

    private final Controller controller;
    
    public BeanGestionPerfiles() {
        this.controller = new Controller();
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.util;

import javax.faces.context.FacesContext;



/**
 *
 * @author estudiante.soporte
 */
public class MyUtil {

    public static String baseurl(){
        String scheme = FacesContext.getCurrentInstance().getExternalContext().getRequestScheme();
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "" + FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        return scheme + "://" + serverName + ":" + serverPort + path + "/";
    }
    
    public static String basepathlogin() {
        return "/SWE/faces/";
    }

    public static String Pagina_Inicial() {
        return "/faces/vistas/";
    }
}

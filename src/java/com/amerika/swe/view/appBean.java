/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.view;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import com.amerika.swe.util.MyUtil;

/**
 *
 * @author estudiante.soporte
 */
@Named(value = "appBean")
@ApplicationScoped
public class appBean {

    /**
     * Creates a new instance of appBean
     */
    public appBean() {
    }
        
    public String getBaseUrl(){        
        return MyUtil.baseurl();
    }
    
    public String getBasePath(){
        return MyUtil.basepathlogin();
    }
    
    public String getPagina_Inicial(){
        return MyUtil.Pagina_Inicial();
    }
    
    public String getFactorAmbiental(){
        return MyUtil.Pagina_Inicial();
    }
    
}

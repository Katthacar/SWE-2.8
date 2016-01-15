/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amerika.swe.util;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweMenu;
import com.amerika.swe.model.SweUsuarioPerfil;
import com.amerika.swe.model.SweUsuarios;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;


/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@SessionScoped
public class Menu implements Serializable{

    private MenuModel menuBar;
    private Controller controller;
    
    public Menu() {
        controller = new Controller();
        crearMenu();
    }
    
    private void crearMenu(){
        menuBar =  new DefaultMenuModel();
        Set<String> pagina_permitida = new HashSet<String>(); // En esta variable guardaremos los nombres de las paginas a las cuales tiene permiso el usuario de acceder
        SweUsuarios usuario = (SweUsuarios)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        SweUsuarioPerfil usuario_perfil = (SweUsuarioPerfil)usuario.getSweUsuarioPerfils().iterator().next();
        List<SweMenu> listaMenu = this.controller.lista_menu(usuario_perfil.getId().getCodigoPerfil(),0);
    
        DefaultMenuItem  itemInicio = new DefaultMenuItem();
        itemInicio.setValue("Inicio");
        itemInicio.setUrl(MyUtil.Pagina_Inicial()+"inicio.xhtml");
        itemInicio.setIcon("home");
        menuBar.addElement(itemInicio);        
      
        for (int i = 0; i < listaMenu.size(); i++) {
            DefaultSubMenu submenu = new DefaultSubMenu();
            submenu.setLabel(listaMenu.get(i).getNombre());
            
            List<SweMenu> list_item = this.controller.lista_menu(usuario_perfil.getId().getCodigoPerfil(),listaMenu.get(i).getCodigo()); 
            for (SweMenu sweMenu : list_item) {
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(sweMenu.getNombre());
                item.setUrl(MyUtil.Pagina_Inicial() + sweMenu.getUrlMenu());
                item.setIcon(sweMenu.getIcono()); 
                submenu.addElement(item); 
                pagina_permitida.add(sweMenu.getUrlMenu().substring(sweMenu.getUrlMenu().lastIndexOf("/")+1));
            }              
            menuBar.addElement(submenu);
        }
        
        DefaultMenuItem itemCerrar = new DefaultMenuItem();            
        itemCerrar.setValue("Cerrar Sesión");
        itemCerrar.setIcon("salir");       
        itemCerrar.setOncomplete("handleLogoutRequest(xhr, status, args)"); 
        itemCerrar.setCommand("#{loginBean.logout}"); 
        menuBar.addElement(itemCerrar);        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pagina_permitida", pagina_permitida); // Guardamos en el session las paginas a las cuales tiene permiso el usuario que se atenticó
    }

    /**
     * @return the menuBar
     */
    public MenuModel getMenuBar() {
        return menuBar;
    }

    /**
     * @param menuBar the menuBar to set
     */
    public void setMenuBar(MenuModel menuBar) {
        this.menuBar = menuBar;
    }
}

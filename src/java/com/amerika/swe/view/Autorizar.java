package com.amerika.swe.view;

import com.amerika.swe.util.MyUtil;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author estudiante.proyectos
 */
public class Autorizar implements PhaseListener {

    /**
     *
     * @param event
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        String pagina_actual = context.getViewRoot().getViewId();

        boolean isLogin = (pagina_actual.lastIndexOf("login.xhtml") > -1 ? true : false);
        HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
        Object obj = sesion.getAttribute("usuario");
        Set<String> pagina_permitida = (Set<String>) sesion.getAttribute("pagina_permitida");

        if (!isLogin && obj == null) {
            try {
                FacesContext contex = FacesContext.getCurrentInstance();
                contex.getExternalContext().redirect(MyUtil.baseurl());
            } catch (IOException ex) {
                Logger.getLogger(Autorizar.class.getName()).log(Level.SEVERE, null, ex);
                NavigationHandler navigation = context.getApplication().getNavigationHandler();
                navigation.handleNavigation(context, null, MyUtil.basepathlogin() + "login.xhtml");
            }
        } 
//        else if (!isLogin && obj != null) {
//            String s = pagina_actual.substring(pagina_actual.lastIndexOf("/") + 1);
//            if (pagina_permitida != null && !pagina_permitida.contains(s)) {
//                try {
//                    FacesContext contex = FacesContext.getCurrentInstance();
//                    contex.getExternalContext().redirect(MyUtil.baseurl()+"faces/vistas/inicio.xhtml");
//                } catch (IOException ex) {
//                    Logger.getLogger(Autorizar.class.getName()).log(Level.SEVERE, null, ex);
//                    NavigationHandler navigation = context.getApplication().getNavigationHandler();
//                    navigation.handleNavigation(context, null, MyUtil.basepathlogin() + "login.xhtml");
//                }
//            }
//        }

    }

    /**
     *
     * @param event
     */
    @Override
    public void beforePhase(PhaseEvent event) {
    }

    /**
     *
     * @return
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}

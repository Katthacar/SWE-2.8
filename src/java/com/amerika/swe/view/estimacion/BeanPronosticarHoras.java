package com.amerika.swe.view.estimacion;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SwePronostico;
import com.amerika.swe.model.SweUsuarios;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 *
 * @author estudiante.soporte
 */
@ManagedBean
@SessionScoped
public class BeanPronosticarHoras implements Serializable {

    private final Controller controller;
    private BigDecimal pronosticoHoras;
    private SwePronostico pronostico;

    public BeanPronosticarHoras() {
        controller = new Controller();
    }

    /**
     * @return the pronosticoHoras
     */
    public BigDecimal getPronosticoHoras() {
        return pronosticoHoras;
    }

    /**
     * @param pronosticoHoras the pronosticoHoras to set
     */
    public void setPronosticoHoras(BigDecimal pronosticoHoras) {
        this.pronosticoHoras = pronosticoHoras;
    }

    public void pronosticarHoras(int idLenguaje, int idPerfil, BigDecimal apf) {
        String codUsuario = ((SweUsuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario")).getCodigo();

        pronosticoHoras = controller.ejec_P_PronosticarHoras(idLenguaje, idPerfil, apf, codUsuario);
        if (pronosticoHoras.compareTo(BigDecimal.ZERO) == 0) {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "No se lograron pronosticar horas");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } else {
            FacesMessage msj = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Se pronosticaron " + pronosticoHoras + " horas");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            if (pronostico == null) {
                pronostico = new SwePronostico();
            }
            pronostico.setHrPronostico(pronosticoHoras);

        }
    }

    /**
     * @return the pronostico
     */
    public SwePronostico getPronostico() {
        return pronostico;
    }

    /**
     * @param pronostico the pronostico to set
     */
    public void setPronostico(SwePronostico pronostico) {
        this.pronostico = pronostico;
    }

}

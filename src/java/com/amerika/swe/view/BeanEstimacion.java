package com.amerika.swe.view;

import com.amerika.swe.controller.Controller;
import com.amerika.swe.model.SweCata;
import com.amerika.swe.model.SweCompXsoli;
import com.amerika.swe.model.SweDefperfAsociahrXleng;
import com.amerika.swe.model.SweEtapaXsol;
import com.amerika.swe.model.SweEtapaXsolId;
import com.amerika.swe.model.SweFactAmbiXsol;
import com.amerika.swe.model.SweFactCalXTsol;
import com.amerika.swe.model.SweFtecnXsol;
import com.amerika.swe.model.SwePorcDistXetapa;
import com.amerika.swe.model.SwePronostico;
import com.amerika.swe.model.SweSoli;
import com.amerika.swe.model.SweUsuarios;
import com.amerika.swe.model.util.EsfuerzoEtapa;
import com.amerika.swe.view.estimacion.BeanFactorAjuste;
import com.amerika.swe.view.estimacion.BeanPFNoAjustados;
import com.amerika.swe.view.estimacion.BeanPronosticarHoras;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author estudiante.proyectos
 */
@ManagedBean
@ViewScoped
public class BeanEstimacion implements Serializable {

    private final Controller controller;
    private List<SweCata> list_tipoSol;
    private List<SweCata> list_tipoAplicacion;
    private List<SweCata> list_lenguaje;
    private List<SweCata> list_escenario;
    private String nombreCliente;
    private String nombreProyecto;
    private String analista;
    private String fechaSolicitud;
    private Integer idSolicitud;
    /*Los atributos a continuación son los de los totales de estimación
     y el esfuerzo requerido por etapa*/
    private List<EsfuerzoEtapa> list_esfuerzoxEtapa;    // Lista que ilustra el esfuerzo por etapa en dias horas, días y meses
    private BigDecimal productividadxHora;              // Horas productivas al día
    private BigDecimal horasXdia;                       // Horas laborables del día
    private BigDecimal diasXmeses;                      // Días productivos o laborables al mes
    private BigDecimal porcentajeProductividadhr;       // Porcentaje de productividad de la persona al día
    private BigDecimal pfPersonaMes;                    // Punto de función por persona al mes
    private boolean bandaraSolicitudValida = false;     // 
    private boolean banderaBtnCalcularEsfuerzo = false;
    private boolean disableTabview = true;              // Este atributo sirve de bandera para pintar o no las pestañas
    private boolean banBtnGuardarEstimacion = false;    // Este atributo activa y desactiva los botones de gardar y cerrar estimación
    private SweSoli estimacionSolicitud;                // Atributo que contiene los valores de la estimación
    private BigDecimal factorCalibracionTpSolicitud;    // Factor de calibración vigente de acuerdo al tipo de solicitud
    private BigDecimal totalPorcDistribucion;           // Representa el total del procentaje de distribución por etapas
    private BigDecimal totalEsfuerzoHoras;              // Representa el total de esfuerzo en horas
    private BigDecimal totalEsfuerzoDias;               // Representa el total del esfuerzo requerido en dias
    private BigDecimal totalEsfuerzoMeses;              // Representa el total del esruerzo requerido en meses 
    /**
     * Este atributo define los estados en que se encuentra la estimación en la
     * pantalla, estos estados estan representados por valores enteros como se
     * muestra a continuación: 0 - La estimación no ha sido guardada por primera
     * vez 1 - La estimación fue modificada y no se han guardado los cambios 2 -
     * La estimación ya fue guardada
     */
    private byte estados_estimacion = 0;
    /**
     * *
     * Esta bandera permite controlar que no se estime de manera simultane una
     * misma solicitud false => indica que se de debe verificar que no haya
     * estimación abierta antes de guardar cambios true => indica que no hay
     * necesidad de consultar estimación abierta
     */
    private boolean bandera_bloqueo_multiusuario = false;

    public BeanEstimacion() {
        this.controller = new Controller();
        cargarListas();
        iniTablasEsfuerzoxEtapa();
        iniciarEstimacion();

    }

    private void cargarListas() {
        this.list_tipoSol = this.controller.listarPOR("Tipo Solicitud");
        this.list_tipoAplicacion = this.controller.listarPOR("Tipo Aplicación");
        this.list_lenguaje = this.controller.listarPOR("Lenguaje");
        this.list_escenario = this.controller.listarPOR("Perfiles");
    }

    /**
     * Este metodo inicializa las variables de la estimación
     */
    private void iniciarEstimacion() {
        estimacionSolicitud = new SweSoli();

        estimacionSolicitud.setSweCataByIdTposolicitud(new SweCata());
        estimacionSolicitud.setSweCataByIdAplicacion(new SweCata());
        estimacionSolicitud.setSweCataByIdLenguaje(new SweCata());
        estimacionSolicitud.setSweCataByIdPerfil(new SweCata());

        estimacionSolicitud.setUpf(BigDecimal.ZERO);
        estimacionSolicitud.setVaf(BigDecimal.ZERO);
        estimacionSolicitud.setApf(BigDecimal.ZERO);
        estimacionSolicitud.setTpf(BigDecimal.ZERO);
        estimacionSolicitud.setHe(BigDecimal.ZERO);
        estimacionSolicitud.setHrje(BigDecimal.ZERO);
        estimacionSolicitud.setFa(BigDecimal.ZERO);

//        idSolicitud = new Integer(0);
        FacesContext context = FacesContext.getCurrentInstance();
        BeanFactorAjuste factorAjuste = (BeanFactorAjuste) context.getApplication().evaluateExpressionGet(context, "#{beanFactorAjuste}", Object.class);
        estimacionSolicitud.setFce(factorAjuste.getFacAmbCalculado().setScale(2, RoundingMode.UP));

        estimacionSolicitud.setHepf(BigDecimal.ZERO);
    }

    /*Este metodo inicializa las tablas de esfuerzo requerido por etapa
     y los totales de estimación*/
    private void iniTablasEsfuerzoxEtapa() {
        List<SwePorcDistXetapa> l = this.controller.listarVigentes_porcDistxEtapa();

        this.list_esfuerzoxEtapa = new ArrayList<EsfuerzoEtapa>();
        this.totalPorcDistribucion = BigDecimal.ZERO;
        for (int i = 0; i < l.size(); i++) {
            list_esfuerzoxEtapa.add(new EsfuerzoEtapa(l.get(i), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            this.totalPorcDistribucion = this.totalPorcDistribucion.add(l.get(i).getPorcentaje());
        }

        porcentajeProductividadhr = this.controller.buscar_parametro("Productividad h");
        horasXdia = this.controller.buscar_parametro("Horas x día");
        diasXmeses = this.controller.buscar_parametro("Días x mes");
        productividadxHora = horasXdia.multiply(porcentajeProductividadhr);
        pfPersonaMes = this.controller.buscar_parametro("PF persona mes");
    }

    public void confirmarBuscarSolicitud() {
        iniciarEstimacion();
        iniTablasEsfuerzoxEtapa();
        ((BeanPFNoAjustados) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{beanPFNoAjustados}", Object.class)).reiniciarValores();
        ((BeanFactorAjuste) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{beanFactorAjuste}", Object.class)).reiniciar();
        disableTabview = true;
        estados_estimacion = 0;
        Object[] obj = this.controller.buscarSilicitudOT(idSolicitud, "Abierto");

        // Verificamos que hayan datos de la solicitud para validar su existencia y que ademas no tenga fecha de cierre ni tampoco fecha de anulación
        if ((obj != null && obj[0] != null && obj[1] != null && obj[2] != null || obj[3] != null && obj[4] != null) && obj[5] == null && obj[6] == null) {
            nombreCliente = ("" + obj[0]).toUpperCase();
            nombreProyecto = ("" + obj[1]).toUpperCase();
            analista = ("" + obj[2]).toUpperCase();
            fechaSolicitud = DateFormat.getDateInstance(DateFormat.MEDIUM).format((Date) obj[3]);

            /*A continuación seleccionamos el id del tipoSolicitud 
             * de la lista list_tipoSol luego de comparar el nombre consultado
             * de la OTMANAGER asociado el tipo de la solicitud.
             */
            bandaraSolicitudValida = false;
            for (int i = 0; i < list_tipoSol.size(); i++) {
                if (("" + obj[4]).equals(list_tipoSol.get(i).getNombre())) {
                    estimacionSolicitud.setSweCataByIdTposolicitud(list_tipoSol.get(i));
                    bandaraSolicitudValida = true;
                    break;
                }
            }
            if (!bandaraSolicitudValida) {
                estimacionSolicitud.getSweCataByIdTposolicitud().setIdCatalogo(0);
                factorCalibracionTpSolicitud = BigDecimal.ZERO;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El código ingresado tiene un tipo de solicitud sin definir en el sistema"));
            } else {
                buscarFactorCalibracion();
                ajaxActivarPestañas();
                if (obj[7] != null && !((List<SweSoli>) obj[7]).isEmpty()) {
                    this.estimacionSolicitud = ((List<SweSoli>) obj[7]).get(0);

                    if (estimacionSolicitud.getAprobada().equals("S")) {
                        RequestContext.getCurrentInstance().execute("DialogAprobada.show()");
                    } else {
                        List li = ((List<SweSoli>) obj[7]);
                        Iterator it = ((SweSoli)li.get(0)).getSwePronosticos().iterator();
                        if (it.hasNext()) {
                            RequestContext.getCurrentInstance().execute("DialogConfirmar.show()");
                            FacesContext context = FacesContext.getCurrentInstance();
                            BeanPronosticarHoras pronostico = (BeanPronosticarHoras) context.getApplication().evaluateExpressionGet(context, "#{beanPronosticarHoras}", Object.class);
                            pronostico.setPronostico((SwePronostico) it.next());

                        }

                    }
                }

            }
        } else if (obj[5] instanceof Date) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Esta solicitud fue cerrada con fecha " + DateFormat.getDateInstance(DateFormat.MEDIUM).format((Date) obj[5])));

        } else if (obj[6] instanceof Date) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Esta solicitud fue anulada con fecha " + DateFormat.getDateInstance(DateFormat.MEDIUM).format((Date) obj[6])));
        } else {
            nombreCliente = "";
            nombreProyecto = "";
            analista = "";
            fechaSolicitud = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La solicitud no ha sido registrada"));
        }

    }

    /**
     * Este metodo consulta información de la solicitud con el código ingresado
     * por el usuario.
     *
     */
    public void buscarSolicitud() {

        if (idSolicitud > 0) {
            if (estados_estimacion == 1) {
                RequestContext.getCurrentInstance().execute("dialog_buscar_solicitud.show()");
            } else {
                confirmarBuscarSolicitud();
                System.gc();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Ingrese el código de la solicitud"));
        }
    }

    /**
     * Este metodo consulta el valor de calibración vigente asociado al tipo de
     * solicitud seleccionada.
     */
    public void buscarFactorCalibracion() {
        try {
            factorCalibracionTpSolicitud = ((SweFactCalXTsol) this.controller.consultar_factorCalibracion_tpSolicitud(this.estimacionSolicitud.getSweCataByIdTposolicitud().getIdCatalogo())).getValor();
            estimacionSolicitud.setFce(estimacionSolicitud.getFce().add(factorCalibracionTpSolicitud).setScale(2, RoundingMode.UP));
        } catch (Exception e) {
            factorCalibracionTpSolicitud = BigDecimal.ZERO;
        }
    }

    public void ajaxActivarPestañas() {

        if (this.estimacionSolicitud.getSweCataByIdAplicacion().getIdCatalogo() > 0 && this.estimacionSolicitud.getSweCataByIdPerfil().getIdCatalogo() > 0 && this.estimacionSolicitud.getSweCataByIdLenguaje().getIdCatalogo() > 0
                && this.estimacionSolicitud.getSweCataByIdTposolicitud().getIdCatalogo() > 0 && bandaraSolicitudValida) {
            this.disableTabview = false;
        } else {
            this.disableTabview = true;
        }
    }

    /**
     * Este metodo actualiza el Conteo de Puntos de Función Ajustados APF = VAF
     * * UPF TPF = APF * FCE HE = TPF * HEPF HRJE = HE
     */
    public void ajaxActualizarAPF() {
        estimacionSolicitud.setApf(estimacionSolicitud.getVaf().multiply(estimacionSolicitud.getUpf()).setScale(5, RoundingMode.DOWN));
        estimacionSolicitud.setApf(estimacionSolicitud.getApf().divide(BigDecimal.ONE, 2, RoundingMode.DOWN));
        this.estimacionSolicitud.setTpf(this.estimacionSolicitud.getApf().multiply(this.estimacionSolicitud.getFce()).setScale(2, RoundingMode.DOWN));

        this.estimacionSolicitud.setHe(this.estimacionSolicitud.getTpf().multiply(this.estimacionSolicitud.getHepf()));
        this.estimacionSolicitud.setHrje(this.estimacionSolicitud.getHe());
        estados_estimacion = 1;
    }

    /**
     * Este metodo consulta las horas de un perfil por lenguaje, se ejecuata al
     * seleccionar el perfil y el lenguaje.
     */
    public void ajaxBuscarPerfilLenguaje() {
        if (this.estimacionSolicitud.getSweCataByIdLenguaje().getIdCatalogo() > 0 && this.estimacionSolicitud.getSweCataByIdPerfil().getIdCatalogo() > 0) {
            try {
                estimacionSolicitud.setHepf(((SweDefperfAsociahrXleng) this.controller.consultar_perfilLenguaje_vigente(this.estimacionSolicitud.getSweCataByIdLenguaje().getIdCatalogo(), this.estimacionSolicitud.getSweCataByIdPerfil().getIdCatalogo())).getHoras());
            } catch (Exception e) {
                estimacionSolicitud.setHepf(BigDecimal.ZERO);
            }
            this.estimacionSolicitud.setHe(this.estimacionSolicitud.getTpf().multiply(estimacionSolicitud.getHepf()));
            this.estimacionSolicitud.setHrje(this.estimacionSolicitud.getHe());
        }
        ajaxActivarPestañas();
    }

    /**
     * Este metodo verifica que la distribución de los porcentajes no este por
     * encima ni tampoco por debajo del 100%
     */
    public void ajaxModificarPorcDistribucion(CellEditEvent event) {

        try {
            this.totalPorcDistribucion = this.totalPorcDistribucion.subtract((BigDecimal) event.getOldValue());
            this.totalPorcDistribucion = this.totalPorcDistribucion.add((BigDecimal) event.getNewValue());
        } catch (Exception e) {
            Logger.getLogger("ads").info(e.getMessage());
        }

        if (this.totalPorcDistribucion.compareTo(BigDecimal.valueOf(100.00)) < 0) {
            this.banderaBtnCalcularEsfuerzo = true;
            this.banBtnGuardarEstimacion = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La distribución de los porcentajes no alcanza el 100%. Por favor verifique"));
        } else if (this.totalPorcDistribucion.compareTo(BigDecimal.valueOf(100.00)) > 0) {
            this.banderaBtnCalcularEsfuerzo = true;
            this.banBtnGuardarEstimacion = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La distribución de los porcentajes supera el 100%. Por favor verifique"));
        } else {
            this.banderaBtnCalcularEsfuerzo = false;
            this.banBtnGuardarEstimacion = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "La distribución alcanzó el 100%"));
        }

    }

    /**
     * Este metodo recalcula la tabla de esfuerzos requeridos en horas, dias y
     * meses, ademas de representar los totales de cada uno. *
     */
    public void btnCalcularEsfuerzo() {
        this.totalEsfuerzoHoras = BigDecimal.ZERO;
        this.totalEsfuerzoDias = BigDecimal.ZERO;
        this.totalEsfuerzoMeses = BigDecimal.ZERO;

        for (int i = 0; i < list_esfuerzoxEtapa.size(); i++) {
            list_esfuerzoxEtapa.get(i).setnHoras(list_esfuerzoxEtapa.get(i).getPorcentajeEstimado().getPorcentaje()
                    .multiply(this.estimacionSolicitud.getHrje()).divide(totalPorcDistribucion, 2, RoundingMode.UP));

            list_esfuerzoxEtapa.get(i).setnDias(list_esfuerzoxEtapa.get(i).getnHoras().divide(this.horasXdia, 2, RoundingMode.UP));

            list_esfuerzoxEtapa.get(i).setnMeses(list_esfuerzoxEtapa.get(i).getnDias().divide(this.diasXmeses, 2, RoundingMode.UP));

            this.totalEsfuerzoHoras = this.totalEsfuerzoHoras.add(list_esfuerzoxEtapa.get(i).getnHoras());
            this.totalEsfuerzoDias = this.totalEsfuerzoDias.add(list_esfuerzoxEtapa.get(i).getnDias());
            this.totalEsfuerzoMeses = this.totalEsfuerzoMeses.add(list_esfuerzoxEtapa.get(i).getnMeses());
        }
        this.estados_estimacion = 1;
    }

    /**
     * Con este método se evita que dos o mas usuarios estimen de manera
     * simultanea sobre el mismo código de solicitud.
     */
    public void btnEvitarConcurrencia() {
        if (bandera_bloqueo_multiusuario) {
            btnGuardarEstimacion();
        } else {
            List<SweSoli> s = this.controller.buscarEstimaciones(idSolicitud, "Abierto");
            if (s.isEmpty()) {
                btnGuardarEstimacion();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Se ha encontrado una estimación abierta asociada a esta solicitud. Por favor cierre la estimación " + s.get(0).getIdEstimacion() + " para crear una nueva"));
            }
        }
    }

    public void btnGuardarEstimacion() {

        /**
         * A continuación agregamos los parametros a la solicitud que son:
         * número de la solicitud, llave foranea TipoSolicitud, llave foranea
         * Lenguaje, llave forane perfil, llave foranea tipoAplicación y el
         * estado
         */
        // Agregamos el número de la solicitud
        estimacionSolicitud.setNumSolicitud(this.idSolicitud);
        //Agregamos la fecha de creación de la solicitud
        if (estimacionSolicitud.getFechaCreacion() == null) {
            estimacionSolicitud.setFechaCreacion(new Date());
        }
        /**
         * A continuación se seleccionan los Bean donde se encuentran los tipos
         * de componentes asociados a la solicitud
         */
        FacesContext context = FacesContext.getCurrentInstance();
        BeanPFNoAjustados pfNoAjustados = (BeanPFNoAjustados) context.getApplication().evaluateExpressionGet(context, "#{beanPFNoAjustados}", Object.class);

        BeanPronosticarHoras beanPronosticarHoras = (BeanPronosticarHoras) context.getApplication().evaluateExpressionGet(context, "#{beanPronosticarHoras}", Object.class);
        SwePronostico pronostico = beanPronosticarHoras.getPronostico();

        /*Obtenemos los componentes asociados al a solicitud*/
        Collection<List<SweCompXsoli>> compXsoli = pfNoAjustados.getMapa_listas().values();

        /**
         * Obtenemos las listas de factores tecnicos y factores ambientales por
         * solicitud del Bean BeanFactorAjuste
         */
        BeanFactorAjuste factorAjuste = (BeanFactorAjuste) context.getApplication().evaluateExpressionGet(context, "#{beanFactorAjuste}", Object.class);

        // Obtenemos los factores tecnicos
        List<SweFtecnXsol> factoresTecXsoli = factorAjuste.getList_apliXfact();

        // Obtemos los factores ambientales Seleccionados 
        List<SweFactAmbiXsol> factoresAmbXsol = (factorAjuste.getFacAmb_seleccionados() != null ? Arrays.asList(factorAjuste.getFacAmb_seleccionados()) : new ArrayList<SweFactAmbiXsol>());

        // Obtenemos el total de la influencia de los factores técnicos asociados a la solicitud
        this.estimacionSolicitud.setCt(factorAjuste.getTotalInfluencia());

        // Obtenemos el factor ambiental falculado
        // this.estimacionSolicitud.setFa(BigDecimal.valueOf(factorAjuste.getTotalInfluenciaAmb()));
        this.estimacionSolicitud.setFa(factorAjuste.getFacAmbCalculado());

        // Obtenemos los porcentajes de distribución de los esfuerzos por etapa
        List<SweEtapaXsol> etapaXsoli = new ArrayList<SweEtapaXsol>(this.list_esfuerzoxEtapa.size());
        for (int i = 0; i < this.list_esfuerzoxEtapa.size(); i++) {
            SweEtapaXsol etapa = new SweEtapaXsol();
            SweEtapaXsolId id = new SweEtapaXsolId();
            id.setCodigoEtapa(list_esfuerzoxEtapa.get(i).getPorcentajeEstimado().getSweCata().getIdCatalogo());
            etapa.setId(id);

            etapa.setPorcenEstimado(list_esfuerzoxEtapa.get(i).getPorcentajeEstimado().getPorcentaje());
            etapa.setHorasEstimadas(list_esfuerzoxEtapa.get(i).getnHoras());
            etapaXsoli.add(etapa);
        }

        estimacionSolicitud.setSweUsuariosByIdUsu_estima((SweUsuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"));
        estimacionSolicitud.setFechaModificacion(new java.util.Date());
        estimacionSolicitud.setAprobada("N"); // Con esta propiedad decimos que la estimación aun no ha sido aprobada
        Object obj = this.controller.guardarEstimacion(estimacionSolicitud, compXsoli, factoresAmbXsol, factoresTecXsoli, etapaXsoli, pronostico);

        if (obj instanceof Boolean) {
            estados_estimacion = 2; // Este estado representa que la estimación ya fue guardada
            bandera_bloqueo_multiusuario = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso!", "La estimación " + estimacionSolicitud.getIdEstimacion() + " ha sido guardada con éxito"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La estimación no se pudo registrar"));
        }
        System.gc();
    }

    /**
     * Este metodo cambia el estado de la estimación de abierta a cerrada.
     */
    public void btnCerrarEstimacion(boolean confirmar) {
        if (confirmar) {
            estimacionSolicitud.setSweUsuariosByIdUsu_cierra(((SweUsuarios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario")));
            estimacionSolicitud.setFechaModificacion(new java.util.Date());
            Object obj = this.controller.cambiarEstadoEstimacion(estimacionSolicitud, "Cerrado");

            if (obj instanceof Boolean) {
                reiniciarValores();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje!", "La estimación fue cerrada con éxito"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "La estimación no se pudo cerrar"));
            }
        } else {
            if (this.estados_estimacion == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso!", "La parametrizacón de la estimación no ha terminado correctamente."));
            } else if (this.estados_estimacion == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La estimación ha sido modificada", "Guarde los cambios y luego cierre la estimación si lo desea"));
            } else if (this.estados_estimacion == 2) {
                RequestContext.getCurrentInstance().execute("dialog_cerrar_estimacion.show()");
            }

        }
        System.gc();
    }

    /**
     * Este metodo carga los datos de una estimación abierta. Se extraen datos
     * de las siguiente tablas: - Componente x Solicitud - Factor Ambiental X
     * Solicitud - Factor Tecnico X Solicitud - Etapa X Solicitud
     *
     * Los datos cargados de las tablas anteriores son consultados con el id de
     * la estimación generado al momento de guardarse
     */
    public void cargarEstimacionAbierta() {
        bandera_bloqueo_multiusuario = true;

        FacesContext context = FacesContext.getCurrentInstance();

        List<SweCompXsoli> componentes = this.controller.buscar_CompSolicitud(this.estimacionSolicitud.getIdEstimacion());

        BeanPFNoAjustados pfNoAjustados = (BeanPFNoAjustados) context.getApplication().evaluateExpressionGet(context, "#{beanPFNoAjustados}", Object.class);

        /**
         * A continuación agregamos los componentes separados por listas en el
         * Bean pfNoAjustados
         */
        pfNoAjustados.reiniciarValores(); // Reiniciamos las listas y los mapas

        if (!componentes.isEmpty()) {
            int idFrom = 0;
            int idAnterior = 0;
            for (int i = 0; i < componentes.size(); i++) {
                if (i == 0) {
                    idAnterior = componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo();
                } else if (idAnterior != componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo()) {
                    pfNoAjustados.getMapa_listas().put(componentes.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), componentes.subList(idFrom, i));
                    pfNoAjustados.setTipoComponente_actual(componentes.get(idFrom).getSweCataByIdTpocomponente());
                    pfNoAjustados.setList_puntosFuncionales(componentes.subList(idFrom, i));
                    pfNoAjustados.actualizarValores();
                    idFrom = i;
                    idAnterior = componentes.get(i).getSweCataByIdTpocomponente().getIdCatalogo();
                }
            }
            pfNoAjustados.getMapa_listas().put(componentes.get(idFrom).getSweCataByIdTpocomponente().getIdCatalogo(), componentes.subList(idFrom, componentes.size()));
            pfNoAjustados.setTipoComponente_actual(componentes.get(idFrom).getSweCataByIdTpocomponente());
            pfNoAjustados.setList_puntosFuncionales(componentes.subList(idFrom, componentes.size()));
            pfNoAjustados.actualizarValores();
        }
        /**
         * A continuación obtenemos el BeanFactorAjuste para cargar los factores
         * técnicos y factores ambientales de la solicitud
         *
         */
        BeanFactorAjuste factorAjuste = (BeanFactorAjuste) context.getApplication().evaluateExpressionGet(context, "#{beanFactorAjuste}", Object.class);

        /*Obtenemos los factores técnicos y los cargamos*/
        List<SweFtecnXsol> factoresTecnicos = this.controller.buscar_FacTecSolicitud(estimacionSolicitud.getIdEstimacion());

        factorAjuste.setList_apliXfact(factoresTecnicos);

        factorAjuste.setTotalInfluencia(estimacionSolicitud.getCt());   // Actualizamos el atributo Total Influencia        
        factorAjuste.setVAF(estimacionSolicitud.getVaf());              // Actualizamos el VAF en el beanFactorAjuste

        /*Obtenemos los factores ambientales seleccionados en la estimación*/
        List<SweFactAmbiXsol> factoresAmbientales = this.controller.buscar_FacAmbSolicitud(estimacionSolicitud.getIdEstimacion());
        SweFactAmbiXsol[] facAmb_seleccionados = new SweFactAmbiXsol[factoresAmbientales.size()];
        facAmb_seleccionados = factoresAmbientales.toArray(facAmb_seleccionados);
        BigDecimal b = BigDecimal.ZERO;
        if (facAmb_seleccionados
                != null) {
            for (int i = 0; i < facAmb_seleccionados.length; i++) {
                facAmb_seleccionados[i] = factoresAmbientales.get(i);
                for (int j = 0; j < factorAjuste.getList_factorAmbiental().size(); j++) {
                    if (facAmb_seleccionados[i].getSweCata().getIdCatalogo() == factorAjuste.getList_factorAmbiental().get(j).getSweCata().getIdCatalogo()) {
                        factorAjuste.getList_factorAmbiental().get(j).setInfluencia(facAmb_seleccionados[i].getInfluencia());
                        b = b.add(facAmb_seleccionados[i].getPeso().multiply(new BigDecimal(facAmb_seleccionados[i].getInfluencia())));
                        break;
                    }
                }

            }
        }

        factorAjuste.setTotalInfluenciaAmb(b.floatValue());
        factorAjuste.setFacAmbCalculado(estimacionSolicitud.getFa());
//      factorAjuste.setFacAmbCalculado(factorAjuste.getK1FactorAmbiental().subtract(BigDecimal.valueOf(factorAjuste.getTotalInfluenciaAmb()).multiply(factorAjuste.getK2FactorAmbiental())));
        factorAjuste.setFacAmb_seleccionados(facAmb_seleccionados);

        /*Cargamos los porcentajes de distribución de las etapas*/
        List<SweEtapaXsol> esfuerEtapa = this.controller.buscar_EtapaSolicitud(estimacionSolicitud.getIdEstimacion());
        this.totalEsfuerzoHoras = BigDecimal.ZERO;
        this.totalEsfuerzoDias = BigDecimal.ZERO;
        this.totalEsfuerzoMeses = BigDecimal.ZERO;
        this.totalPorcDistribucion = BigDecimal.ZERO;

        this.list_esfuerzoxEtapa.clear();
        for (int i = 0;
                i < esfuerEtapa.size();
                i++) {
            SwePorcDistXetapa porcDistribucion = new SwePorcDistXetapa();
            porcDistribucion.setSweCata(esfuerEtapa.get(i).getSweCata());
            porcDistribucion.setPorcentaje(esfuerEtapa.get(i).getPorcenEstimado());

            EsfuerzoEtapa esfuerzo = new EsfuerzoEtapa(porcDistribucion);
            esfuerzo.setnHoras(esfuerEtapa.get(i).getHorasEstimadas());
            esfuerzo.setnDias(esfuerEtapa.get(i).getHorasEstimadas().divide(this.horasXdia, 2, RoundingMode.UP));
            esfuerzo.setnMeses(esfuerzo.getnDias().divide(this.diasXmeses, 2, RoundingMode.UP));

            this.totalEsfuerzoHoras = this.totalEsfuerzoHoras.add(esfuerzo.getnHoras());
            this.totalEsfuerzoDias = this.totalEsfuerzoDias.add(esfuerzo.getnDias());
            this.totalEsfuerzoMeses = this.totalEsfuerzoMeses.add(esfuerzo.getnMeses());
            this.totalPorcDistribucion = this.totalPorcDistribucion.add(esfuerEtapa.get(i).getPorcenEstimado());

            this.list_esfuerzoxEtapa.add(esfuerzo);
        }
        estados_estimacion = 2;  // Este estado refiere que la solicitud ha sido guardada
        disableTabview = false;

        System.gc(); // Hacemos un llamado de manera explicita al recolector de basura de 
    }

    public void ajax_selecTipoSolicitud(SweCata tipoSolicitud) {
        this.estimacionSolicitud.setSweCataByIdTposolicitud(tipoSolicitud);
    }

    public void ajax_selectAplicacion() {
        for (int i = 0; i < list_tipoAplicacion.size(); i++) {
            if (list_tipoAplicacion.get(i).getIdCatalogo() == this.estimacionSolicitud.getSweCataByIdAplicacion().getIdCatalogo()) {
                this.estimacionSolicitud.setSweCataByIdAplicacion(list_tipoAplicacion.get(i));
                break;
            }
        }
        ajaxActivarPestañas();
    }

    public void ajax_selectLenguaje() {
        for (int i = 0; i < this.list_lenguaje.size(); i++) {
            if (this.list_lenguaje.get(i).getIdCatalogo() == this.estimacionSolicitud.getSweCataByIdLenguaje().getIdCatalogo()) {
                this.estimacionSolicitud.setSweCataByIdLenguaje(this.list_lenguaje.get(i));
                break;
            }
        }
    }

    public void ajax_selectPerfil() {
        for (int i = 0; i < list_escenario.size(); i++) {
            if (list_escenario.get(i).getIdCatalogo() == this.estimacionSolicitud.getSweCataByIdPerfil().getIdCatalogo()) {
                this.estimacionSolicitud.setSweCataByIdPerfil(list_escenario.get(i));
                break;
            }
        }
    }

    public void reiniciarValores() {
        iniciarEstimacion();
        iniTablasEsfuerzoxEtapa();

        ((BeanPFNoAjustados) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{beanPFNoAjustados}", Object.class)).reiniciarValores();
        ((BeanFactorAjuste) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{beanFactorAjuste}", Object.class)).reiniciar();
        disableTabview = true;
        estados_estimacion = 0;
        nombreCliente = "";
        nombreProyecto = "";
        analista = "";
        fechaSolicitud = null;
        idSolicitud = null;
        bandera_bloqueo_multiusuario = false;
    }

    /**
     * @return the list_tipoSol
     */
    public List<SweCata> getList_tipoSol() {
        return list_tipoSol;
    }

    /**
     * @param list_tipoSol the list_tipoSol to set
     */
    public void setList_tipoSol(List<SweCata> list_tipoSol) {
        this.list_tipoSol = list_tipoSol;
    }

    /**
     * @return the list_tipoAplicacion
     */
    public List<SweCata> getList_tipoAplicacion() {
        return list_tipoAplicacion;
    }

    /**
     * @param list_tipoAplicacion the list_tipoAplicacion to set
     */
    public void setList_tipoAplicacion(List<SweCata> list_tipoAplicacion) {
        this.list_tipoAplicacion = list_tipoAplicacion;
    }

    /**
     * @return the list_lenguaje
     */
    public List<SweCata> getList_lenguaje() {
        return list_lenguaje;
    }

    /**
     * @param list_lenguaje the list_lenguaje to set
     */
    public void setList_lenguaje(List<SweCata> list_lenguaje) {
        this.list_lenguaje = list_lenguaje;
    }

    /**
     * @return the list_escenario
     */
    public List<SweCata> getList_escenario() {
        return list_escenario;
    }

    /**
     * @param list_escenario the list_escenario to set
     */
    public void setList_escenario(List<SweCata> list_escenario) {
        this.list_escenario = list_escenario;
    }

    /**
     * @return the nombreProyecto
     */
    public String getNombreProyecto() {
        return nombreProyecto;
    }

    /**
     * @param nombreProyecto the nombreProyecto to set
     */
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    /**
     * @return the analista
     */
    public String getAnalista() {
        return analista;
    }

    /**
     * @param analista the analista to set
     */
    public void setAnalista(String analista) {
        this.analista = analista;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fechaSolicitud;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    /**
     * @return the idSolicitud
     */
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * @param idSolicitud the idSolicitud to set
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * @return the nombreCliente
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * @param nombreCliente the nombreCliente to set
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * @return the list_esfuerzoxEtapa
     */
    public List<EsfuerzoEtapa> getList_esfuerzoxEtapa() {
        return list_esfuerzoxEtapa;
    }

    /**
     * @param list_esfuerzoxEtapa the list_esfuerzoxEtapa to set
     */
    public void setList_esfuerzoxEtapa(List<EsfuerzoEtapa> list_esfuerzoxEtapa) {
        this.list_esfuerzoxEtapa = list_esfuerzoxEtapa;
    }

    /**
     * @return the productividadxHora
     */
    public BigDecimal getProductividadxHora() {
        return productividadxHora;
    }

    /**
     * @param productividadxHora the productividadxHora to set
     */
    public void setProductividadxHora(BigDecimal productividadxHora) {
        this.productividadxHora = productividadxHora;
    }

    /**
     * @return the horasXdia
     */
    public BigDecimal getHorasXdia() {
        return horasXdia;
    }

    /**
     * @param horasXdia the horasXdia to set
     */
    public void setHorasXdia(BigDecimal horasXdia) {
        this.horasXdia = horasXdia;
    }

    /**
     * @return the diasXmeses
     */
    public BigDecimal getDiasXmeses() {
        return diasXmeses;
    }

    /**
     * @param diasXmeses the diasXmeses to set
     */
    public void setDiasXmeses(BigDecimal diasXmeses) {
        this.diasXmeses = diasXmeses;
    }

    /**
     * @return the porcentajeProductividadhr
     */
    public BigDecimal getPorcentajeProductividadhr() {
        return porcentajeProductividadhr;
    }

    /**
     * @param porcentajeProductividadhr the porcentajeProductividadhr to set
     */
    public void setPorcentajeProductividadhr(BigDecimal porcentajeProductividadhr) {
        this.porcentajeProductividadhr = porcentajeProductividadhr;
    }

    /**
     * @return the estimacionSolicitud
     */
    public SweSoli getEstimacionSolicitud() {
        return estimacionSolicitud;
    }

    /**
     * @param estimacionSolicitud the estimacionSolicitud to set
     */
    public void setEstimacionSolicitud(SweSoli estimacionSolicitud) {
        this.estimacionSolicitud = estimacionSolicitud;
    }

    /**
     * @return the bandaraSolicitudValida
     */
    public boolean isBandaraSolicitudValida() {
        return bandaraSolicitudValida;
    }

    /**
     * @param bandaraSolicitudValida the bandaraSolicitudValida to set
     */
    public void setBandaraSolicitudValida(boolean bandaraSolicitudValida) {
        this.bandaraSolicitudValida = bandaraSolicitudValida;
    }

    /**
     * @return the factorCalibracionTpSolicitud
     */
    public BigDecimal getFactorCalibracionTpSolicitud() {
        return factorCalibracionTpSolicitud;
    }

    /**
     * @param factorCalibracionTpSolicitud the factorCalibracionTpSolicitud to
     * set
     */
    public void setFactorCalibracionTpSolicitud(BigDecimal factorCalibracionTpSolicitud) {
        this.factorCalibracionTpSolicitud = factorCalibracionTpSolicitud;
    }

    /**
     * @return the pfPersonaMes
     */
    public BigDecimal getPfPersonaMes() {
        return pfPersonaMes;
    }

    /**
     * @param pfPersonaMes the pfPersonaMes to set
     */
    public void setPfPersonaMes(BigDecimal pfPersonaMes) {
        this.pfPersonaMes = pfPersonaMes;
    }

    /**
     * @return the totalPorcDistribucion
     */
    public BigDecimal getTotalPorcDistribucion() {
        return totalPorcDistribucion;
    }

    /**
     * @param totalPorcDistribucion the totalPorcDistribucion to set
     */
    public void setTotalPorcDistribucion(BigDecimal totalPorcDistribucion) {
        this.totalPorcDistribucion = totalPorcDistribucion;
    }

    /**
     * @return the banderaBtnCalcularEsfuerzo
     */
    public boolean isBanderaBtnCalcularEsfuerzo() {
        return banderaBtnCalcularEsfuerzo;
    }

    /**
     * @param banderaBtnCalcularEsfuerzo the banderaBtnCalcularEsfuerzo to set
     */
    public void setBanderaBtnCalcularEsfuerzo(boolean banderaBtnCalcularEsfuerzo) {
        this.banderaBtnCalcularEsfuerzo = banderaBtnCalcularEsfuerzo;
    }

    /**
     * @return the totalEsfuerzoHoras
     */
    public BigDecimal getTotalEsfuerzoHoras() {
        return totalEsfuerzoHoras;
    }

    /**
     * @param totalEsfuerzoHoras the totalEsfuerzoHoras to set
     */
    public void setTotalEsfuerzoHoras(BigDecimal totalEsfuerzoHoras) {
        this.totalEsfuerzoHoras = totalEsfuerzoHoras;
    }

    /**
     * @return the totalEsfuerzoDias
     */
    public BigDecimal getTotalEsfuerzoDias() {
        return totalEsfuerzoDias;
    }

    /**
     * @param totalEsfuerzoDias the totalEsfuerzoDias to set
     */
    public void setTotalEsfuerzoDias(BigDecimal totalEsfuerzoDias) {
        this.totalEsfuerzoDias = totalEsfuerzoDias;
    }

    /**
     * @return the totalEsfuerzoMeses
     */
    public BigDecimal getTotalEsfuerzoMeses() {
        return totalEsfuerzoMeses;
    }

    /**
     * @param totalEsfuerzoMeses the totalEsfuerzoMeses to set
     */
    public void setTotalEsfuerzoMeses(BigDecimal totalEsfuerzoMeses) {
        this.totalEsfuerzoMeses = totalEsfuerzoMeses;
    }

    /**
     * @return the disableTabview
     */
    public boolean isActivarTabview() {
        return disableTabview;
    }

    /**
     * @param disableTabview the disableTabview to set
     */
    public void setDisableTabview(boolean disableTabview) {
        this.disableTabview = disableTabview;
    }

    /**
     * @return the banBtnGuardarEstimacion
     */
    public boolean isBanBtnGuardarEstimacion() {
        return banBtnGuardarEstimacion;
    }

    /**
     * @param banBtnGuardarEstimacion the banBtnGuardarEstimacion to set
     */
    public void setBanBtnGuardarEstimacion(boolean banBtnGuardarEstimacion) {
        this.banBtnGuardarEstimacion = banBtnGuardarEstimacion;
    }

    /**
     * @return the estados_estimacion
     */
    public byte getEstados_estimacion() {
        return estados_estimacion;
    }

    /**
     * @param estados_estimacion the estados_estimacion to set
     */
    public void setEstados_estimacion(byte estados_estimacion) {
        this.estados_estimacion = estados_estimacion;
    }

    /**
     * @return the bandera_bloqueo_multiusuario
     */
    public boolean isBandera_bloqueo_multiusuario() {
        return bandera_bloqueo_multiusuario;
    }

    /**
     * @param bandera_bloqueo_multiusuario the bandera_bloqueo_multiusuario to
     * set
     */
    public void setBandera_bloqueo_multiusuario(boolean bandera_bloqueo_multiusuario) {
        this.bandera_bloqueo_multiusuario = bandera_bloqueo_multiusuario;
    }
}

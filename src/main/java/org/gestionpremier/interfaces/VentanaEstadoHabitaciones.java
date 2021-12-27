package org.gestionpremier.interfaces;

import org.gestionpremier.dto.EstadiaDTO;
import org.gestionpremier.dto.EstadoHabitacionDTO;
import org.gestionpremier.dto.EstadoHabitacionDiaDTO;
import org.gestionpremier.dto.HabitacionDTO;
import org.gestionpremier.excepciones.ObjetoNoEncontradoException;
import org.gestionpremier.excepciones.RangoDeFechaErroneoException;
import org.gestionpremier.negocio.entidades.TipoHabitacion;
import org.gestionpremier.negocio.gestores.GestorEstadia;
import org.gestionpremier.negocio.gestores.GestorHabitacion;
import org.gestionpremier.negocio.entidades.EstadoHabitacion;
import org.hibernate.HibernateException;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaEstadoHabitaciones extends JDialog {

    /***************************************************************************************************************
     *                                      ATRIBUTOS - ELEMENTOS GRÁFICOS
     * *************************************************************************************************************/

    private JPanel panelPrincipal;
    private JLabel labelTituloVentana;
    private JComboBox comboFechaDesdeDia;
    private JComboBox comboFechaDesdeMes;
    private JComboBox comboFechaDesdeAnyo;
    private JComboBox comboFechaHastaDia;
    private JComboBox comboFechaHastaMes;
    private JComboBox comboFechaHastaAnyo;
    private JPanel panelTituloVentana;
    private JPanel panelControlesFecha;
    private JPanel panelAccion;
    private JScrollPane panelTabla;
    private JPanel panelLeyendas;
    private JPanel panelBotonesInferiores;
    private JPanel panelFechaDesde;
    private JPanel panelFechaHasta;
    private JComboBox comboAccion;
    private JTable tablaVerEstadoHabitaciones;
    private JLabel labelIconoDesocupada;
    private JLabel labelIconoReservada;
    private JLabel labelIconoOcupada;
    private JLabel labelIconoFueraDeServicio;
    private JButton btnCancelar;
    private JButton btnAplicar;

    private TablaOcuparHabitaciones tablaOcuparHabitaciones;

    /***************************************************************************************************************
     *                                           ATRIBUTOS - LÓGICA
     * *************************************************************************************************************/

    private final int TABLA_VER_ESTADO_HABITACIONES = 0;
    private final int TABLA_OCUPAR_HABITACIONES = 2;
    private final int FECHA_DESDE = 0;
    private final int FECHA_HASTA = 1;
    private final int DIA_DESDE = 0;
    private final int MES_DESDE = 1;
    private final int ANYO_DESDE = 2;
    private final int DIA_HASTA = 3;
    private final int MES_HASTA = 4;
    private final int ANYO_HASTA = 5;
    private final int INDICE_ANYO_ACTUAL_COMBO = 6;
    private final int MAQ4_CARGAR_OTRA = 0;
    private final int MAQ4_CANCELAR = 1;
    private final int MAQ4_ANTERIOR = 2;
    private final int MAQ4_SALIR = 3;
    private int opcionMaqueta4 = MAQ4_ANTERIOR;

    private LocalDate ultimaFechaDesde = LocalDate.MIN;
    private LocalDate ultimaFechaHasta = LocalDate.MIN;
    private int ultimoComboDesdeSeleccionado = 0;
    private int ultimoComboHastaSeleccionado = 0;
    private int ultimoGrupoFechaSeleccionado = 0;
    private int ultimoIndiceAccion = TABLA_VER_ESTADO_HABITACIONES;

    /***************************************************************************************************************
     *                                                CONSTRUCTORES
     * *************************************************************************************************************/

    public VentanaEstadoHabitaciones(Frame owner, boolean modal) {

        super(owner, modal);

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    public VentanaEstadoHabitaciones () {

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    /***************************************************************************************************************
     *                                  MÉTODOS DE CREACIÓN LLAMADOS EN EL CONSTRUCTOR
     * *************************************************************************************************************/

    private void inicializarComponentes() {

        btnAplicar.setEnabled(false);
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setBackground(new Color(10, 132, 255));

        inicializarTablas();

        inicializarCombosFecha();

        comboAccion.setModel(new DefaultComboBoxModel(new String[] {"Revisar estado", "Reservar habitación", "Ocupar habitación"}));

    }

    private void agregarListeners() {

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancelar();
            }
        });

        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aplicar();
            }
        });

        comboAccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioAccionTabla();
            }
        });

        comboFechaDesdeDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboDiaDesde();
            }
        });

        comboFechaHastaDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboDiaHasta();
            }
        });

        comboFechaDesdeMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboMesDesde();
            }
        });

        comboFechaHastaMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboMesHasta();
            }
        });

        comboFechaDesdeAnyo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboAnyoDesde();
            }
        });

        comboFechaHastaAnyo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cambioComboAnyoHasta();
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> this.cancelar(), k, w);

    }

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        setTitle("Estado de las Habitaciones");
//        setResizable(false);
        pack();
        setLocationRelativeTo(null);
//        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    private void inicializarTablas() {

        tablaOcuparHabitaciones = new TablaOcuparHabitaciones(this);

        tablaVerEstadoHabitaciones.setRowHeight(30);
        tablaOcuparHabitaciones.setRowHeight(30);
        tablaVerEstadoHabitaciones.setAutoResizeMode(0);
        tablaOcuparHabitaciones.setAutoResizeMode(0);
        tablaVerEstadoHabitaciones.setModel(new ModeloTablaEstadoHabitaciones());
        tablaOcuparHabitaciones.setModel(new ModeloTablaEstadoHabitaciones());
        tablaVerEstadoHabitaciones.getTableHeader().setReorderingAllowed(false);
        tablaOcuparHabitaciones.getTableHeader().setReorderingAllowed(false);
        tablaVerEstadoHabitaciones.getTableHeader().setReorderingAllowed(false);
        tablaOcuparHabitaciones.getTableHeader().setReorderingAllowed(false);

    }

    private void inicializarCombosFecha() {

        cargarDias();
        cargarMeses();
        cargarAnyos();

    }

    private void cargarDias() {

        comboFechaDesdeDia.setModel(new DefaultComboBoxModel());
        comboFechaHastaDia.setModel(new DefaultComboBoxModel());

        comboFechaDesdeDia.addItem("Día...");
        comboFechaHastaDia.addItem("Día...");

        for (int i = 1; i < 32; i++) {

            comboFechaDesdeDia.addItem(Integer.toString(i));
            comboFechaHastaDia.addItem(Integer.toString(i));

        }

        comboFechaDesdeDia.setSelectedIndex(LocalDate.now().getDayOfMonth());

    }

    private void cargarMeses() {

        comboFechaDesdeMes.setModel(new DefaultComboBoxModel());

        comboFechaDesdeMes.addItem("Mes...");
        comboFechaDesdeMes.addItem("Enero");
        comboFechaDesdeMes.addItem("Febrero");
        comboFechaDesdeMes.addItem("Marzo");
        comboFechaDesdeMes.addItem("Abril");
        comboFechaDesdeMes.addItem("Mayo");
        comboFechaDesdeMes.addItem("Junio");
        comboFechaDesdeMes.addItem("Julio");
        comboFechaDesdeMes.addItem("Agosto");
        comboFechaDesdeMes.addItem("Septiembre");
        comboFechaDesdeMes.addItem("Octubre");
        comboFechaDesdeMes.addItem("Noviembre");
        comboFechaDesdeMes.addItem("Diciembre");

        comboFechaHastaMes.setModel(new DefaultComboBoxModel());

        comboFechaHastaMes.addItem("Mes...");
        comboFechaHastaMes.addItem("Enero");
        comboFechaHastaMes.addItem("Febrero");
        comboFechaHastaMes.addItem("Marzo");
        comboFechaHastaMes.addItem("Abril");
        comboFechaHastaMes.addItem("Mayo");
        comboFechaHastaMes.addItem("Junio");
        comboFechaHastaMes.addItem("Julio");
        comboFechaHastaMes.addItem("Agosto");
        comboFechaHastaMes.addItem("Septiembre");
        comboFechaHastaMes.addItem("Octubre");
        comboFechaHastaMes.addItem("Noviembre");
        comboFechaHastaMes.addItem("Diciembre");

        comboFechaDesdeMes.setSelectedIndex(LocalDate.now().getMonthValue());
        comboFechaHastaMes.setSelectedIndex(LocalDate.now().getMonthValue());

    }

    private void cargarAnyos() {

        comboFechaDesdeAnyo.setModel(new DefaultComboBoxModel());
        comboFechaHastaAnyo.setModel(new DefaultComboBoxModel());

        comboFechaDesdeAnyo.addItem("Año...");
        comboFechaHastaAnyo.addItem("Año...");

        final int CANT_ANYOS_FUTUROS = 5;
        final int CANT_ANYOS_PASADOS = 50;

        int anyoMayorASeleccionar = LocalDate.now().getYear() + CANT_ANYOS_FUTUROS;
        int anyoMenorASeleccionar = anyoMayorASeleccionar - (CANT_ANYOS_PASADOS + CANT_ANYOS_FUTUROS);

        for (int i = anyoMayorASeleccionar; i > anyoMenorASeleccionar; i--) {

            comboFechaDesdeAnyo.addItem(Integer.toString(i));
            comboFechaHastaAnyo.addItem(Integer.toString(i));

        }

        comboFechaDesdeAnyo.setSelectedIndex(CANT_ANYOS_FUTUROS + 1);
        comboFechaHastaAnyo.setSelectedIndex(CANT_ANYOS_FUTUROS + 1);

    }

    /***************************************************************************************************************
     *                                      MÉTODOS ACCIONADOS CON LOS BOTONES
     * *************************************************************************************************************/

    private void cancelar() {

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

    private void aplicar() {

        if (this.validarRangoOcupacion()) {

            EstadiaDTO estadiaDTO;

            try {

                estadiaDTO = this.crearEstadiaDTO();


            } catch (ObjetoNoEncontradoException e) {

                JOptionPane.showMessageDialog(this, e.getMessage());
                return;

            }

            if (this.opcionMaqueta4 == MAQ4_CARGAR_OTRA || this.opcionMaqueta4 == MAQ4_SALIR) {

                long idEstadiaCreada = -1;

                try {

                    idEstadiaCreada = GestorEstadia.obtenerInstancia().crearEstadia(estadiaDTO);

                } catch (HibernateException e) {

                    JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para insertar la nueva estadía.\n\n" + e.getMessage());
                    cancelar();

                }

                if (idEstadiaCreada < 0) {

                    JOptionPane.showMessageDialog(this, "Se produjo un error al crear la estadía.\n\nFuente: GestorEstadia.crearEstadia()");

                } else {

                    JOptionPane.showMessageDialog(this, "La estadía fue creada exitosamente con id = " + idEstadiaCreada + ".");

                }

                this.tablaOcuparHabitaciones.clearSelecciones();

            }

            if (this.opcionMaqueta4 == MAQ4_SALIR || this.opcionMaqueta4 == MAQ4_CANCELAR){

                this.cancelar();

            }

            if (this.opcionMaqueta4 == MAQ4_CARGAR_OTRA){

                this.cargarTabla(ultimaFechaDesde, ultimaFechaHasta);

            }

        } else {

            this.tablaOcuparHabitaciones.clearSelecciones();

        }

    }

    private void cambioAccionTabla() {

        if (ultimoIndiceAccion == TABLA_OCUPAR_HABITACIONES && comboAccion.getSelectedIndex() != TABLA_OCUPAR_HABITACIONES) {

            tablaOcuparHabitaciones.clearSelecciones();

        }

        comboAccion.getUI().setPopupVisible(comboAccion, false);

        cambioEnFechaSeleccionada();

    }

    private void cambioComboDiaDesde() {
        ultimoComboDesdeSeleccionado = DIA_DESDE;
        ultimoGrupoFechaSeleccionado = FECHA_DESDE;
        comboFechaDesdeDia.getUI().setPopupVisible(comboFechaDesdeDia, false);
        cambioEnFechaSeleccionada();
    }

    private void cambioComboDiaHasta() {
        ultimoComboHastaSeleccionado = DIA_HASTA;
        ultimoGrupoFechaSeleccionado = FECHA_HASTA;
        comboFechaHastaDia.getUI().setPopupVisible(comboFechaHastaDia, false);
        cambioEnFechaSeleccionada();
    }

    private void cambioComboMesDesde() {
        ultimoComboDesdeSeleccionado = MES_DESDE;
        ultimoGrupoFechaSeleccionado = FECHA_DESDE;
        comboFechaDesdeMes.getUI().setPopupVisible(comboFechaDesdeMes, false);
        cambioEnFechaSeleccionada();
    }

    private void cambioComboMesHasta() {
        ultimoComboHastaSeleccionado = MES_HASTA;
        ultimoGrupoFechaSeleccionado = FECHA_HASTA;
        comboFechaHastaMes.getUI().setPopupVisible(comboFechaHastaMes, false);
        cambioEnFechaSeleccionada();
    }

    private void cambioComboAnyoDesde() {
        ultimoComboDesdeSeleccionado = ANYO_DESDE;
        ultimoGrupoFechaSeleccionado = FECHA_DESDE;
        comboFechaDesdeAnyo.getUI().setPopupVisible(comboFechaDesdeAnyo, false);
        cambioEnFechaSeleccionada();
    }

    private void cambioComboAnyoHasta() {
        ultimoComboHastaSeleccionado = ANYO_HASTA;
        ultimoGrupoFechaSeleccionado = FECHA_HASTA;
        comboFechaHastaAnyo.getUI().setPopupVisible(comboFechaHastaAnyo, false);
        cambioEnFechaSeleccionada();
    }

    private void cargarTabla(LocalDate fechaDesde, LocalDate fechaHasta) {

        int tipoTabla = comboAccion.getSelectedIndex();

        JTable tablaActual;

        switch (tipoTabla) {

            case TABLA_OCUPAR_HABITACIONES:
                tablaActual = tablaOcuparHabitaciones;
                this.panelTabla.getViewport().remove(tablaVerEstadoHabitaciones);
                this.panelTabla.getViewport().add(tablaOcuparHabitaciones);
                break;

            default:
                tablaActual = tablaVerEstadoHabitaciones;
                this.panelTabla.getViewport().remove(tablaOcuparHabitaciones);
                this.panelTabla.getViewport().add(tablaVerEstadoHabitaciones);
                this.btnAplicar.setEnabled(false);

        }

        ModeloTablaEstadoHabitaciones modeloTabla = (ModeloTablaEstadoHabitaciones) tablaActual.getModel();

//            DESCOMENTAR AL INTEGRAR LA VENTANA CON EL GESTOR.
        GestorHabitacion gestorHabitacion = GestorHabitacion.obtenerInstancia();

        List<EstadoHabitacionDTO> listaEstados = new ArrayList<>();

        try {

            listaEstados = gestorHabitacion.getEstadoHabitaciones(LocalDateTime.of(fechaDesde, LocalTime.now()), LocalDateTime.of(fechaHasta.plusDays(1), LocalTime.of(10,0,0)));

        } catch (RangoDeFechaErroneoException e) {

            JOptionPane.showMessageDialog(this, "El rango de fecha seleccionado no es válido (validación del gestor).");
            return;

        } catch (HibernateException e2) {

            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para consultar el estado de las habitaciones.\n\n" + e2.getMessage());
            cancelar();

        }

        // BORRAR AL INTEGRAR LA VENTANA CON EL GESTOR.
//        List<EstadoHabitacionDTO> listaEstados = mockGetEstadoHabitaciones(fechaDesde, fechaHasta);

        modeloTabla.setListaEstadosHabitacion( listaEstados );

        TableColumnModel modeloColumna = tablaActual.getColumnModel();
        RendererTablaEstadoHabitaciones rendererQuePintaCeldas = new RendererTablaEstadoHabitaciones();

        for (int i = 0; i < tablaActual.getColumnCount(); i++) {

            modeloColumna.getColumn(i).setCellRenderer(rendererQuePintaCeldas);
            modeloColumna.getColumn(i).setPreferredWidth(100);

        }

        btnAplicar.setEnabled(false);

    }

    private void cambioEnFechaSeleccionada() {

        boolean todasLasFechasDesdeEstanSeleccionadas;
        boolean todasLasFechasHastaEstanSeleccionadas;

        if (comboAccion.getSelectedIndex() == TABLA_OCUPAR_HABITACIONES) {

            comboFechaDesdeDia.setSelectedIndex(LocalDate.now().getDayOfMonth());
            comboFechaDesdeDia.setEnabled(false);
            comboFechaDesdeMes.setSelectedIndex(LocalDate.now().getMonthValue());
            comboFechaDesdeMes.setEnabled(false);
            comboFechaDesdeAnyo.setSelectedIndex(INDICE_ANYO_ACTUAL_COMBO);
            comboFechaDesdeAnyo.setEnabled(false);

            todasLasFechasDesdeEstanSeleccionadas = true;

        } else {

            comboFechaDesdeDia.setEnabled(true);
            comboFechaDesdeMes.setEnabled(true);
            comboFechaDesdeAnyo.setEnabled(true);

            todasLasFechasDesdeEstanSeleccionadas =
                               comboFechaDesdeDia.getSelectedIndex() != 0
                            && comboFechaDesdeMes.getSelectedIndex() != 0
                            && comboFechaDesdeAnyo.getSelectedIndex() != 0;

        }

        todasLasFechasHastaEstanSeleccionadas =
                           comboFechaHastaDia.getSelectedIndex() != 0
                        && comboFechaHastaMes.getSelectedIndex() != 0
                        && comboFechaHastaAnyo.getSelectedIndex() != 0;

        boolean fechaDesdeValida = false;
        boolean fechaHastaValida = false;

        if (todasLasFechasDesdeEstanSeleccionadas) {
            fechaDesdeValida = validarUnaFechaComboBox(FECHA_DESDE);
        }

        if (todasLasFechasHastaEstanSeleccionadas) {
            fechaHastaValida = validarUnaFechaComboBox(FECHA_HASTA);
        }

        if (fechaDesdeValida && fechaHastaValida) {

            if (validarRangoFechaComboBox()) {

                LocalDate fechaDesde = getFechaComboBox(FECHA_DESDE);
                LocalDate fechaHasta = getFechaComboBox(FECHA_HASTA);

                this.cargarTabla(fechaDesde, fechaHasta);

                if ((ultimaFechaDesde != fechaDesde || ultimaFechaHasta != fechaHasta) && comboAccion.getSelectedIndex() == TABLA_OCUPAR_HABITACIONES ) {
                    tablaOcuparHabitaciones.clearSelecciones();
                }

                ultimaFechaDesde = fechaDesde;
                ultimaFechaHasta = fechaHasta;

            } else {

                this.borrarTabla();

            }

        } else {

            this.borrarTabla();

        }

        ultimoIndiceAccion = comboAccion.getSelectedIndex();

    }

    /*********** A INTEGRAR CON OCUPAR HABITACION MAQUETA 4 ***********/
    private EstadiaDTO crearEstadiaDTO () throws ObjetoNoEncontradoException {

        LocalDateTime fechaDesde = LocalDateTime.of( this.tablaOcuparHabitaciones.getFechaDesdeSeleccionada() , LocalTime.now() );
        LocalDateTime fechaHasta = LocalDateTime.of( this.tablaOcuparHabitaciones.getFechaHastaSeleccionada().plusDays(1) , LocalTime.of(10,0,0) );
        long idHabitacion = this.tablaOcuparHabitaciones.getIdHabitacionSeleccionada();

        GestorHabitacion gestorHabitacion = GestorHabitacion.obtenerInstancia();

        HabitacionDTO habitacionDTO = new HabitacionDTO();

         try {

             habitacionDTO = gestorHabitacion.getHabitacion(idHabitacion);

         } catch (HibernateException e) {

             JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para consultar la habitación de la estadía.\n\n" + e.getMessage());
             cancelar();

         }

        EstadiaDTO estadiaDTO = new EstadiaDTO();

        estadiaDTO.setHabitacionDTO(habitacionDTO);
        estadiaDTO.setFechaDesde(fechaDesde);
        estadiaDTO.setFechaHasta(fechaHasta);

        // A IMPLEMENTAR: LLAMO A LA MAQUETA 4 DE OCUPAR HABITACION, LE MANDO EL DTO Y ME LO DEVUELVE CON LOS PASAJEROS SETEADOS.

        VentanaOcuparHabitacionMaqueta4 maqueta4 = new VentanaOcuparHabitacionMaqueta4((Frame) getOwner(), true, estadiaDTO);

        this.opcionMaqueta4 = maqueta4.getBotonPresionado();
        maqueta4.desecharVentana();

        return estadiaDTO;

    }

    /***************************************************************************************************************
     *                                           VALIDACIÓN DE CAMPOS
     * *************************************************************************************************************/

    private boolean validarUnaFechaComboBox(int queFecha) {

        switch (queFecha) {

            case FECHA_DESDE:

                LocalDate fechaDesde =  getFechaComboBox(FECHA_DESDE);

                if (fechaDesde == null) {
                    JOptionPane.showMessageDialog(this, "Error: La fecha inicial del rango no es válida.");

                    switch (ultimoComboDesdeSeleccionado) {

                        case DIA_DESDE:
                            comboFechaDesdeDia.setSelectedIndex(0);
                            break;

                        case MES_DESDE:
                            comboFechaDesdeMes.setSelectedIndex(0);
                            break;

                        case ANYO_DESDE:
                            comboFechaDesdeAnyo.setSelectedIndex(0);
                            break;

                        default:
                            comboFechaDesdeDia.setSelectedIndex(0);
                            comboFechaDesdeMes.setSelectedIndex(0);
                            comboFechaDesdeAnyo.setSelectedIndex(0);

                    }

                    return false;
                }

                return true;

            case FECHA_HASTA:

                LocalDate fechaHasta = getFechaComboBox(FECHA_HASTA);

                if (fechaHasta == null) {
                    JOptionPane.showMessageDialog(this, "Error: La fecha final del rango no es válida.");

                    switch (ultimoComboHastaSeleccionado) {

                        case DIA_HASTA:
                            comboFechaHastaDia.setSelectedIndex(0);
                            break;

                        case MES_HASTA:
                            comboFechaHastaMes.setSelectedIndex(0);
                            break;

                        case ANYO_HASTA:
                            comboFechaHastaAnyo.setSelectedIndex(0);
                            break;

                        default:
                            comboFechaHastaDia.setSelectedIndex(0);
                            comboFechaHastaMes.setSelectedIndex(0);
                            comboFechaHastaAnyo.setSelectedIndex(0);

                    }

                    return false;
                }

                return true;

            default:
                return false;

        }

    }

    private boolean validarRangoFechaComboBox() {

        LocalDate fechaDesde;
        if (comboAccion.getSelectedIndex() == TABLA_OCUPAR_HABITACIONES) {
            fechaDesde =  LocalDate.now();
        } else {
            fechaDesde =  getFechaComboBox(FECHA_DESDE);
        }

        LocalDate fechaHasta = getFechaComboBox(FECHA_HASTA);

        if ( fechaDesde.isAfter(fechaHasta) ) {

            if (ultimoGrupoFechaSeleccionado == FECHA_DESDE && comboAccion.getSelectedIndex() != TABLA_OCUPAR_HABITACIONES) {

                JOptionPane.showMessageDialog(this, "Error: La fecha inicial seleccionada es posterior a la fecha final.");

                switch (ultimoComboDesdeSeleccionado) {

                    case DIA_DESDE:
                        comboFechaDesdeDia.setSelectedIndex(0);
                        break;

                    case MES_DESDE:
                        comboFechaDesdeMes.setSelectedIndex(0);
                        break;

                    case ANYO_DESDE:
                        comboFechaDesdeAnyo.setSelectedIndex(0);
                        break;

                    default:
                        comboFechaDesdeDia.setSelectedIndex(0);
                        comboFechaDesdeMes.setSelectedIndex(0);
                        comboFechaDesdeAnyo.setSelectedIndex(0);

                }

            } else if (ultimoIndiceAccion != TABLA_OCUPAR_HABITACIONES && comboAccion.getSelectedIndex() == TABLA_OCUPAR_HABITACIONES) {

                comboFechaHastaDia.setSelectedIndex(0);
                comboFechaHastaMes.setSelectedIndex(0);
                comboFechaHastaAnyo.setSelectedIndex(0);

            } else {

                JOptionPane.showMessageDialog(this, "Error: La fecha inicial seleccionada es posterior a la fecha final.");

                switch (ultimoComboHastaSeleccionado) {

                    case DIA_HASTA:
                        comboFechaHastaDia.setSelectedIndex(0);
                        break;

                    case MES_HASTA:
                        comboFechaHastaMes.setSelectedIndex(0);
                        break;

                    case ANYO_HASTA:
                        comboFechaHastaAnyo.setSelectedIndex(0);
                        break;

                    default:
                        comboFechaHastaDia.setSelectedIndex(0);
                        comboFechaHastaMes.setSelectedIndex(0);
                        comboFechaHastaAnyo.setSelectedIndex(0);

                }

                ultimoGrupoFechaSeleccionado = FECHA_HASTA;

            }

            return false;
        }

        return true;

    }

    private LocalDate getFechaComboBox(int cualFecha) {

        int nroDia;
        int nroMes;
        int nroAnyo;

        switch (cualFecha) {

            case FECHA_DESDE:

                if (comboAccion.getSelectedIndex() == TABLA_OCUPAR_HABITACIONES) {

                    nroDia = LocalDate.now().getDayOfMonth();
                    nroMes = LocalDate.now().getMonthValue();
                    nroAnyo = LocalDate.now().getYear();

                } else {

                    nroDia = comboFechaDesdeDia.getSelectedIndex();
                    nroMes = comboFechaDesdeMes.getSelectedIndex();
                    nroAnyo = Integer.parseInt( comboFechaDesdeAnyo.getSelectedItem().toString() );

                }

                break;

            default:
                nroDia = comboFechaHastaDia.getSelectedIndex();
                nroMes = comboFechaHastaMes.getSelectedIndex();
                nroAnyo = Integer.parseInt( comboFechaHastaAnyo.getSelectedItem().toString() );

        }

        LocalDate fecha;

        try {

            fecha = LocalDate.of(nroAnyo, nroMes, nroDia);

        } catch (DateTimeException e) {

            return null;

        }

        return fecha;

    }

    /******** A INTEGRAR CON OCUPAR MAQUETA 2 *********/
    private boolean validarRangoOcupacion() {

        ModeloTablaEstadoHabitaciones modeloTabla = (ModeloTablaEstadoHabitaciones) tablaOcuparHabitaciones.getModel();

        List<Long> listaIdReservas = new ArrayList<>();

        List<Point> selecciones = this.tablaOcuparHabitaciones.getSelecciones();

        EstadoHabitacionDiaDTO estadoHabitacionDiaDTO;

        for (Point p :
                selecciones) {

            estadoHabitacionDiaDTO = modeloTabla.getEstadoHabitacionDia((int) p.getX(), (int) p.getY());

            if ( estadoHabitacionDiaDTO.getEstado().equals(EstadoHabitacion.OCUPADA)
            || estadoHabitacionDiaDTO.getEstado().equals(EstadoHabitacion.EN_MANTENIMIENTO)) {

                return false;

            }

            if ( estadoHabitacionDiaDTO.getEstado().equals(EstadoHabitacion.RESERVADA)
                && !listaIdReservas.contains(estadoHabitacionDiaDTO.getIdReserva()) ) {

                Boolean opcion;
                GestorHabitacion gestorHabitacion = GestorHabitacion.obtenerInstancia();

                try {

                    VentanaOcuparHabitacionMaqueta2 maqueta2 = new VentanaOcuparHabitacionMaqueta2((Frame) getOwner(), true, gestorHabitacion.getReserva(estadoHabitacionDiaDTO.getIdReserva()));

                    opcion = maqueta2.ocuparIgual();

                } catch (ObjetoNoEncontradoException e) {

                    JOptionPane.showMessageDialog(this, "La reserva a consultar no se encuentra en la base de datos.");
                    opcion = false;

                } catch (HibernateException e2) {

                    JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para consultar la reserva seleccionada.\n\n" + e2.getMessage());
                    opcion = false;
                    cancelar();

                }

                if (opcion) {

                    listaIdReservas.add(estadoHabitacionDiaDTO.getIdReserva());

                } else {

                    return false;

                }

            }

        }

        return true;

    }

    /***************************************************************************************************************
     *                                              OTROS MÉTODOS
     * *************************************************************************************************************/

    protected void habilitarBtnAplicar(boolean habilitar) {

        btnAplicar.setEnabled(habilitar);

    }

    private void borrarTabla() {

        this.panelTabla.getViewport().remove(tablaVerEstadoHabitaciones);
        this.panelTabla.getViewport().remove(tablaOcuparHabitaciones);

    }

    /***************************************************************************************************************
     *                                      MÉTODOS DE PRUEBA A BORRAR DESPUÉS
     * *************************************************************************************************************/

    List<EstadoHabitacionDTO> mockGetEstadoHabitaciones(LocalDate fechaDesde, LocalDate fechaHasta) {

        List<EstadoHabitacionDTO> listaEstados = new ArrayList<>();

        final int CANT_HABITACIONES = 20;

        for (int i = 1; i <= CANT_HABITACIONES; i++) {

            listaEstados.add( mockGetRandomEstadoHabitacionDTO(i, fechaDesde, fechaHasta) );

        }

        return listaEstados;

    }

    EstadoHabitacionDTO mockGetRandomEstadoHabitacionDTO(int nro, LocalDate fechaDesde, LocalDate fechaHasta) {

        EstadoHabitacionDTO estadoHabitacionDTO = new EstadoHabitacionDTO();

        estadoHabitacionDTO.setIdHab((long) nro);

        Random random = new Random();
        char c = (char)(random.nextInt(26) + 'A');
        estadoHabitacionDTO.setNroHab(Integer.toString(nro) + c);

        switch (random.nextInt(5)) {

            case 0:
                estadoHabitacionDTO.setTipoHabitacion(TipoHabitacion.INDIVIDUAL_ESTANDAR);
                break;
            case 1:
                estadoHabitacionDTO.setTipoHabitacion(TipoHabitacion.DOBLE_ESTANDAR);
                break;
            case 2:
                estadoHabitacionDTO.setTipoHabitacion(TipoHabitacion.DOBLE_SUPERIOR);
                break;
            case 3:
                estadoHabitacionDTO.setTipoHabitacion(TipoHabitacion.SUPERIOR_FAMILY_PLAN);
                break;
            default:
                estadoHabitacionDTO.setTipoHabitacion(TipoHabitacion.SUITE_DOBLE);

        }

        estadoHabitacionDTO.setEstadosHabitacionXDia( mockGetEstadosHabitacionDia((long) nro, fechaDesde, fechaHasta) );

        return estadoHabitacionDTO;

    }

    List<EstadoHabitacionDiaDTO> mockGetEstadosHabitacionDia(long id, LocalDate fechaDesde, LocalDate fechaHasta) {

        List<EstadoHabitacionDiaDTO> lista = new ArrayList<>();

        long CANT_DIAS = ChronoUnit.DAYS.between(fechaDesde, fechaHasta);

        for (int i = 0; i <= CANT_DIAS; i++) {

            lista.add(new EstadoHabitacionDiaDTO());
            lista.get(i).setIdHabitacion(id);

            Random random = new Random();

            switch (random.nextInt(4)) {

                case 0:
                    lista.get(i).setEstado(EstadoHabitacion.RESERVADA);
                    break;
                case 1:
                    lista.get(i).setEstado(EstadoHabitacion.OCUPADA);
                    break;
                case 2:
                    lista.get(i).setEstado(EstadoHabitacion.EN_MANTENIMIENTO);
                    break;
                default:
                    lista.get(i).setEstado(EstadoHabitacion.DESOCUPADA);

            }

            lista.get(i).setFecha(LocalDateTime.of(fechaDesde.plusDays(i), LocalTime.now()));

        }

        return lista;

    }

}

package org.gestionpremier.interfaces;

import org.gestionpremier.dto.BusquedaPasajerosDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.utilidades.CriterioOrdenPasajeros;
import org.gestionpremier.utilidades.ListaPaginada;
import org.gestionpremier.negocio.gestores.GestorEstadia;
import org.gestionpremier.negocio.gestores.GestorPersona;
import org.hibernate.HibernateException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class VentanaGestionarPasajero extends JDialog {


    /***************************************************************************************************************
     *                                      ATRIBUTOS - ELEMENTOS GRÁFICOS
     * *************************************************************************************************************/

    private JPanel panelPrincipal;
    private JPanel panelTitulo;
    private JPanel panelFormulario;
    private JPanel panelOrdenamiento;
    private JPanel panelTabla;
    private JPanel panelPaginado;
    private JPanel panelNavegacion;
    private JLabel labelTitulo;
    private JButton btnBuscar;
    private JTextField campoApellido;
    private JLabel labelApellido;
    private JLabel labelTipoDoc;
    private JComboBox comboTipoDoc;
    private JTextField campoNombre;
    private JTextField campoNroDoc;
    private JLabel labelNombre;
    private JLabel labelNroDoc;
    private JPanel panelTituloResultado;
    private JLabel labelResultado;
    private JComboBox comboOrdenar;
    private JLabel labelOrdenar;
    private JTable tabla;
    private JLabel labelPag;
    private JButton btnPagPrimera;
    private JButton btnPag1;
    private JButton btnPag2;
    private JButton btnPagNd;
    private JButton btnPagUlt;
    private JButton btnPagUltima;
    private JPanel panelGlobalBusqueda;
    private JPanel panelGlobalResultado;
    private JButton btnSiguiente;
    private JButton btnCancelar;
    private JButton btnPag3;
    private JButton btnPag5;
    private JButton btnPag4;
    private JLabel labelErrorApellido;
    private JLabel labelErrorNombre;
    private JLabel labelErrorNroDoc;

    /***************************************************************************************************************
     *                                           ATRIBUTOS - LÓGICA
     * *************************************************************************************************************/

    private final int ORDEN_APELLIDO_ASC = 0;
    private final int ORDEN_APELLIDO_DESC = 1;
    private final int ORDEN_DNI_ASC = 2;
    private final int ORDEN_DNI_DESC = 3;
    private final int MAX_SIZE_NOMBRE = 50;
    private final int MAX_SIZE_APELLIDO = 50;
    private final int MAX_SIZE_NRO_DOC = 15;
    private final int MAX_FILAS_TABLA = 10;
    private final Color COLOR_SELECCIONADO = new Color(0, 184, 187);
    private boolean ocuparHabitacion = false;
    private VentanaOcuparHabitacionMaqueta4 maqueta4;

    Color colorFuenteError;
    Color colorFuenteValido;
    Color colorBotonDeseleccionado;
    List<JButton> paginadores;

    int ordenAnterior;

    /***************************************************************************************************************
     *                                                CONSTRUCTORES
     * *************************************************************************************************************/

    public VentanaGestionarPasajero () {

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    /** Esta constructor de Jdialog indica mediante el parametro modal si la ventana q llamó a esta ventana debe quedar
     * congelada de fondo hasta q esta se cierra. **/
    public VentanaGestionarPasajero(Frame owner, boolean modal) {
        super(owner, modal);
        this.ocuparHabitacion = false;
        inicializarComponentes();
        agregarListeners();
        configurarVentana();
    }

    public VentanaGestionarPasajero(Frame owner, boolean modal, VentanaOcuparHabitacionMaqueta4 maqueta4) {
        super(owner, modal);
        this.ocuparHabitacion = true;
        this.maqueta4 = maqueta4;
        inicializarComponentes();
        agregarListeners();
        configurarVentana();
    }


    /***************************************************************************************************************
     *                                  MÉTODOS DE CREACIÓN LLAMADOS EN EL CONSTRUCTOR
     * *************************************************************************************************************/


    private void inicializarComponentes () {

        comboTipoDoc.setModel(new DefaultComboBoxModel(new String[] {"", "DNI", "LE", "LC", "PASAPORTE", "OTRO"}));
        comboOrdenar.setModel(new DefaultComboBoxModel(new String[] {"Apellido A-Z", "Apellido Z-A", "Documento 1-9", "Documento 9-1"}));

        btnPag1.setEnabled(false);
        btnPag2.setEnabled(false);
        btnPag3.setEnabled(false);
        btnPag4.setEnabled(false);
        btnPag5.setEnabled(false);
        btnPagPrimera.setEnabled(false);
        btnPagUltima.setEnabled(false);

        colorFuenteError = labelErrorApellido.getForeground();
        colorFuenteValido = getBackground();
        colorBotonDeseleccionado = btnBuscar.getBackground();

        labelErrorNombre.setForeground(colorFuenteValido);
        labelErrorNombre.setText("El nombre no puede contener caracteres especiales.");
        labelErrorApellido.setForeground(colorFuenteValido);
        labelErrorApellido.setText("El apellido no puede contener caracteres especiales.");
        labelErrorNroDoc.setForeground(colorFuenteValido);
        labelErrorNroDoc.setText("El formato no es válido para el tipo de documento seleccionado.");

        paginadores = new ArrayList<>();
        paginadores.add(btnPagPrimera);
        paginadores.add(btnPag1);
        paginadores.add(btnPag2);
        paginadores.add(btnPag3);
        paginadores.add(btnPag4);
        paginadores.add(btnPag5);
        paginadores.add(btnPagUltima);

        tabla.setModel(new ModeloTablaGestionarPasajero());
        tabla.getTableHeader().setReorderingAllowed(false);

        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setBackground(new Color(10, 132, 255));

    }

    private void agregarListeners() {

        comboOrdenar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ordenarTabla();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancelar();
            }
        });

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                siguiente();
            }
        });

        btnPagPrimera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(0);
            }
        });

        btnPagUltima.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(- 1);
            }
        });

        btnPag1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(Integer.parseInt(btnPag1.getText()) - 1);
            }
        });

        btnPag2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(Integer.parseInt(btnPag2.getText()) - 1);
            }
        });

        btnPag3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(Integer.parseInt(btnPag3.getText()) - 1);
            }
        });

        btnPag4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(Integer.parseInt(btnPag4.getText()) - 1);
            }
        });

        btnPag5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutarBusqueda(Integer.parseInt(btnPag5.getText()) - 1);
            }
        });



        /** Lo siguiente hace que el texto que el usuario escribe en el campo de nombre
         * aparezca en mayúsculas siempre (por especificación de requerimientos), lo hace el agregado de .toUpperCase().
         * Por otro lado, el if se encarga de limitar la longitud de caracteres. **/
        ((AbstractDocument) campoNombre.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NOMBRE) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NOMBRE) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        /** Lo siguiente hace que el texto que el usuario escribe en el campo de apellido
         * aparezca en mayúsculas siempre (por especificación de requerimientos), lo hace el agregado de .toUpperCase().
         *          * Por otro lado, el if se encarga de limitar la longitud de caracteres. **/
        ((AbstractDocument) campoApellido.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_APELLIDO) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_APELLIDO) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        /** Lo siguiente limita la cantidad de caracteres que se pueden ingresar en el campo nro de documento. **/
        ((AbstractDocument) campoNroDoc.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NRO_DOC) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NRO_DOC) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        campoApellido.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    buscar();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        campoNombre.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    buscar();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        campoNroDoc.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode()==KeyEvent.VK_ENTER){
                    buscar();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                btnSiguiente.setEnabled(true);
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> this.cancelar(), k, w);

    }

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        if(this.ocuparHabitacion){
            if(maqueta4.getPrimerPasajero()){
                setTitle("Seleccionar responsable de la habitación " + maqueta4.getEstadiaDTO().getHabitacionDTO().getNro());
            }
            else {
                setTitle("Ingresar ocupante a la habitación " + maqueta4.getEstadiaDTO().getHabitacionDTO().getNro());
            }
        }
        else {
            setTitle("Gestionar Pasajeros");
        }
        setMinimumSize(new Dimension(1024, 720));
        setResizable(false);
//        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /***************************************************************************************************************
     *                                      MÉTODOS ACCIONADOS CON LOS BOTONES
     * *************************************************************************************************************/

    private void buscar() {

        ejecutarBusqueda(0);

    }

    private void cancelar() {

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

    private void siguiente(){

        if (!tabla.getSelectionModel().isSelectionEmpty()) {

            PasajeroDTO pasajeroSeleccionado = ((ModeloTablaGestionarPasajero) tabla.getModel()).getPasajero(tabla.getSelectedRow());

            if(ocuparHabitacion){

                GestorEstadia gestorEstadia = GestorEstadia.obtenerInstancia();

                if(maqueta4.getPrimerPasajero()){
                    Long diferencia = Math.abs(ChronoUnit.YEARS.between(pasajeroSeleccionado.getFechaNacimiento(), LocalDateTime.now()));
                    if(diferencia>=18) {
                        maqueta4.setPasajeroResponsableDTO(pasajeroSeleccionado);
                        if (JOptionPane.showConfirmDialog(null, "¿El responsable se hospedará en esta habitación?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {

                            try {

                                if (gestorEstadia.ocupanteCargadoEnOtraHabitacion(pasajeroSeleccionado.getId(), maqueta4.getEstadiaDTO().getFechaDesde(), maqueta4.getEstadiaDTO().getFechaHasta())) {
                                    JOptionPane.showMessageDialog(null, "Este pasajero ya está ocupando otra habitación dentro del rango de fechas seleccionado. Por lo tanto solo ha sido añadido como responsable de esta habitación.", "Pasajero hospedado", JOptionPane.INFORMATION_MESSAGE);
                                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                                } else {
                                    maqueta4.addPasajeroDTO(pasajeroSeleccionado);
                                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                                }

                            } catch (HibernateException e) {

                                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para buscar al ocupante en otras habitaciones.\n\n" + e.getMessage());
                                cancelar();

                            }

                        } else {
                            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Este pasajero no puede ser responsable de la habitación porque es menor de edad.", "Pasajero menor de edad", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else{
                    Boolean ocupanteExistente = false;
                    List<PasajeroDTO> pasajerosDTO = maqueta4.getPasajerosDTO();

                    for(int i=1; i<pasajerosDTO.size(); i++){
                        if(pasajerosDTO.get(i).getId() == pasajeroSeleccionado.getId()){
                            ocupanteExistente = true;
                        }
                    }

                    if(ocupanteExistente){
                        JOptionPane.showMessageDialog(null, "Este pasajero ya está hospedado en esta habitación", "Pasajero repetido", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        if(gestorEstadia.ocupanteCargadoEnOtraHabitacion(pasajeroSeleccionado.getId(), maqueta4.getEstadiaDTO().getFechaDesde(), maqueta4.getEstadiaDTO().getFechaHasta())){
                            JOptionPane.showMessageDialog(null, "Este pasajero ya está ocupando otra habitación, dentro del rango de fechas seleccionado", "Pasajero hospedado", JOptionPane.INFORMATION_MESSAGE);
                        }else {
                            maqueta4.addPasajeroDTO(pasajeroSeleccionado);
                            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                        }
                    }
                }

                //dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

            }
            else {
                /** Acá en vez de llamar el dialog, se tendría q llamar al CU12 Modificar Pasajero **/

                JOptionPane.showMessageDialog(null,
                        "Pasajero seleccionado:\n\n"
                                + "Nombre: "+pasajeroSeleccionado.getNombre()+".\n"
                                + "Apellido: "+pasajeroSeleccionado.getApellido()+".\n"
                                + "Tipo doc: "+pasajeroSeleccionado.getTipoDoc()+".\n"
                                + "Nro doc: "+pasajeroSeleccionado.getNroDoc()+".\n"
                                + "Id: "+pasajeroSeleccionado.getId());
            }
        }
        else {

            borrarTabla();

            altaDePasajero();

        }

    }

    /***************************************************************************************************************
     *                                     MÉTODOS RELACIONADOS CON LA TABLA
     * *************************************************************************************************************/

    private void ejecutarBusqueda(int paginaAMostrar) {

        borrarTabla();

        if (validar()) {

            ModeloTablaGestionarPasajero modeloTabla = (ModeloTablaGestionarPasajero) tabla.getModel();

            CriterioOrdenPasajeros orden;

            switch (comboOrdenar.getSelectedIndex()) {
                case ORDEN_APELLIDO_DESC:
                    orden = CriterioOrdenPasajeros.APELLIDO_ZA;
                    break;
                case ORDEN_DNI_ASC:
                    orden = CriterioOrdenPasajeros.NRO_DOC_1_9;
                    break;
                case ORDEN_DNI_DESC:
                    orden = CriterioOrdenPasajeros.NRO_DOC_9_1;
                    break;
                default:
                    orden = CriterioOrdenPasajeros.APELLIDO_AZ;
            }

            BusquedaPasajerosDTO pasajeroBuscado = crearDTO();

            GestorPersona gestorPersona = GestorPersona.obtenerInstancia();

            /** Si el nro de la página es negativo,
             * es porq el método fue invocado al presionarse el botón para ir a la última página. **/
            if (paginaAMostrar<0) {
                paginaAMostrar = modeloTabla.getListaPasajeros().getCantPaginas() - 1;
            }

            try {

                ListaPaginada<PasajeroDTO> resultadoBusqueda = gestorPersona.getPasajerosPaginado(pasajeroBuscado, paginaAMostrar, MAX_FILAS_TABLA, orden);

                modeloTabla.setListaPasajeros(resultadoBusqueda);

                inicializarPaginadores();

                if (resultadoBusqueda.isEmpty()) {

                    JOptionPane.showMessageDialog(this, "No se han encontrado coincidencias.\n\nProcediendo a la alta de nuevo pasajero...");

                    altaDePasajero();

                }

            } catch (DatosErroneosPasajeroException e) {

                JOptionPane.showMessageDialog(null, e.getMessage());

            } catch (HibernateException e) {

                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para consultar los pasajeros.\n\n" + e.getMessage());
                cancelar();

            }

        }

    }

    private void ordenarTabla() {

        final boolean SELECCIONO_NUEVO_ORDEN = ordenAnterior != comboOrdenar.getSelectedIndex();
        final boolean TABLA_TIENE_DATOS = ((ModeloTablaGestionarPasajero) tabla.getModel()).getRowCount() != 0;

        if (SELECCIONO_NUEVO_ORDEN) {

            ordenAnterior = comboOrdenar.getSelectedIndex();

            if (TABLA_TIENE_DATOS) {

                ejecutarBusqueda(0);

            }

        }

    }

    private void inicializarPaginadores() {

        ModeloTablaGestionarPasajero modeloTabla = (ModeloTablaGestionarPasajero) tabla.getModel();

        int pagActual = modeloTabla.getListaPasajeros().getPaginaActual();
        int cantPagSig = modeloTabla.getListaPasajeros().getCantPaginasSiguientes();
        int cantPagAnt = modeloTabla.getListaPasajeros().getCantPaginasAnteriores();
        int cantPagTotal = modeloTabla.getListaPasajeros().getCantPaginas();

        JButton botonActual;
        int nroBoton;

        for (int i = 1; i < 6; i++) {

            botonActual = paginadores.get(i);

            /** Setear números a los botones de los paginadores.
             * Los números se comportan de la siguiente forma:
             * Hay 5 botones con números de página. En general, la página actual siempre será la del botón del medio,
             * para que el usuario siempre tenga botones disponibles para desplazarse hacia páginas anteriores o
             * siguientes, de a una o dos páginas a la vez. Las excepciones son cuando se llega hasta las páginas
             * iniciales y finales. En dichos casos, la página final siempre será la del botón más hacia la derecha,
             * mientras que la página inicial será la del botón más hacia la izquierda. **/
            if (cantPagAnt>=3 && cantPagSig <=2 && cantPagTotal > 4 ) {

                nroBoton = pagActual + i + cantPagSig - 4;

            } else if (cantPagAnt>=3) {

                nroBoton = pagActual + i - 2;

            } else {

                nroBoton = i;
            }

            botonActual.setText(Integer.toString(nroBoton));

            /** Resaltar visualmente el botón con el número de la página actual. **/
            if (nroBoton-1 == pagActual) {

                botonActual.setBackground(COLOR_SELECCIONADO);

            } else {

                botonActual.setBackground(colorBotonDeseleccionado);

            }

        }

        /** Habilitar los botones correspondientes **/
        for (int i = 1; i < 6; i++) {

            botonActual = paginadores.get(i);

            if (Integer.parseInt(botonActual.getText()) <= pagActual+cantPagSig+1) {

                botonActual.setEnabled(true);

            } else {

                botonActual.setEnabled(false);

            }

        }

        btnPagPrimera.setEnabled(true);
        btnPagUltima.setEnabled(true);

    }

    private void borrarTabla() {

        tabla.clearSelection();

        ModeloTablaGestionarPasajero modeloTabla = (ModeloTablaGestionarPasajero) tabla.getModel();

        modeloTabla.setListaPasajeros(new ListaPaginada<>());

        inicializarPaginadores();

    }

    /***************************************************************************************************************
     *                                           VALIDACIÓN DE CAMPOS
     * *************************************************************************************************************/

    private boolean validar() {

        boolean nombreValido = validarNombre();
        boolean apellidoValido = validarApellido();
        boolean nroDocValido = validarNroDoc();

        return nombreValido && apellidoValido && nroDocValido;

    }

    private boolean validarNombre() {

        String contenido = campoNombre.getText();

        if (!contenido.matches("^$|^[A-ZÑÁÉÍÓÚ\\.\s]+$")) {

            labelErrorNombre.setForeground(colorFuenteError);
            return false;
        }

        labelErrorNombre.setForeground(colorFuenteValido);
        return true;

    }

    private boolean validarApellido() {

        String contenido = campoApellido.getText();

        if (!contenido.matches("^$|^[A-ZÑÁÉÍÓÚ\\.\s]+$")) {

            labelErrorApellido.setForeground(colorFuenteError);
            return false;

        }

        labelErrorApellido.setForeground(colorFuenteValido);
        return true;

    }

    private boolean validarNroDoc() {

        String contenido = campoNroDoc.getText();
        String tipoDoc = comboTipoDoc.getSelectedItem().toString();

        if ((!contenido.matches("^$|^[1-9][0-9]{7}$") && tipoDoc.equals("DNI"))
                || (!contenido.matches("^$|^[0-9]{1,8}$|^[1-9][0-9]{0,6}$") && ((tipoDoc.equals("LE")) || (tipoDoc.equals("LC"))))
                || (!contenido.matches("^$|^[A-Z0-9]{6,9}$") && tipoDoc.equals("PASAPORTE"))
                || (!contenido.matches("^$|^[A-Z0-9]{1,15}$") && (tipoDoc.equals("OTRO") || tipoDoc.equals("")))) {

            labelErrorNroDoc.setForeground(colorFuenteError);
            return false;

        }

        labelErrorNroDoc.setForeground(colorFuenteValido);
        return true;

    }

    /***************************************************************************************************************
     *                                           OTROS MÉTODOS
     * *************************************************************************************************************/

    private BusquedaPasajerosDTO crearDTO() {

        BusquedaPasajerosDTO dto = new BusquedaPasajerosDTO();

        if (!campoNombre.getText().equals("")) {
            dto.setNombre(campoNombre.getText().trim());
        } else {
            dto.setNombre("");
        }
        if (!campoApellido.getText().equals("")) {
            dto.setApellido(campoApellido.getText().trim());
        } else {
            dto.setApellido("");
        }
        if (!comboTipoDoc.getSelectedItem().toString().equals("")) {
            dto.setTipoDoc(comboTipoDoc.getSelectedItem().toString().toUpperCase());
        } else {
            dto.setTipoDoc("");
        }
        if (!campoNroDoc.getText().equals("")) {
            dto.setNroDoc(campoNroDoc.getText());
        } else {
            dto.setNroDoc("");
        }

        return dto;

    }

    private void altaDePasajero() {

        VentanaAltaPasajero ventanaAltaPasajero = new VentanaAltaPasajero((Frame) getOwner(), true);

        PasajeroDTO pasajeroCreado = ventanaAltaPasajero.getPasajeroCreado();

        if (pasajeroCreado != null) {

            campoApellido.setText("");
            campoApellido.setText(pasajeroCreado.getApellido());
            campoNombre.setText("");
            campoNombre.setText(pasajeroCreado.getNombre());
            campoNroDoc.setText("");
            campoNroDoc.setText(pasajeroCreado.getNroDoc());
            //System.out.println("Texto campo doc "+campoNroDoc.getText());

            switch (pasajeroCreado.getTipoDoc()) {
                case DNI:
                    comboTipoDoc.setSelectedIndex(1);
                    break;
                case LE:
                    comboTipoDoc.setSelectedIndex(2);
                    break;
                case LC:
                    comboTipoDoc.setSelectedIndex(3);
                    break;
                case PASAPORTE:
                    comboTipoDoc.setSelectedIndex(4);
                    break;
                case OTRO:
                    comboTipoDoc.setSelectedIndex(5);
            }

            ejecutarBusqueda(0);

        }

    }

}

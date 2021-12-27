package org.gestionpremier.interfaces;

import org.gestionpremier.dto.*;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.excepciones.PasajeroExisteException;
import org.gestionpremier.negocio.entidades.PosicionIva;
import org.gestionpremier.negocio.entidades.TipoDoc;
import org.gestionpremier.dto.*;
import org.gestionpremier.negocio.gestores.GestorGeografico;
import org.gestionpremier.negocio.gestores.GestorPersona;
import org.hibernate.HibernateException;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaAltaPasajero extends JDialog {

    /***************************************************************************************************************
     *                                      ATRIBUTOS - ELEMENTOS GRÁFICOS
     * *************************************************************************************************************/

    private JPanel panelPrincipal;
    private JLabel labelTitulo;
    private JTextField campoApellido;
    private JTextField campoNombre;
    private JTextField campoNroDoc;
    private JTextField campoTelefono;
    private JTextField campoEmail;
    private JTextField campoNacionalidad;
    private JTextField campoCalle;
    private JTextField campoNroCalle;
    private JTextField campoDepto;
    private JTextField campoPiso;
    private JTextField campoCp;
    private JTextField campoCuit;
    private JTextField campoOcupacion;
    private JComboBox comboNacDia;
    private JComboBox comboNacMes;
    private JComboBox comboNacAnyo;
    private JComboBox comboTipoDoc;
    private JComboBox comboPosIva;
    private JComboBox comboPais;
    private JComboBox comboProvincia;
    private JComboBox comboLocalidad;
    private JButton btnCancelar;
    private JButton btnSiguiente;
    private JLabel invisibleLabel1;
    private JLabel invisibleLabel2;
    private JLabel invisibleLabel3;
    private JLabel invisibleLabel4;
    private JLabel invisibleLabel5;
    private JLabel invisibleLabel6;
    private JLabel invisibleLabel7;
    private JLabel invisibleLabel8;
    private JLabel invisibleLabel9;
    private JLabel invisibleLabel10;
    private JLabel invisibleLabel11;
    private JLabel invisibleLabel12;
    private JLabel invisibleLabel13;
    private JLabel invisibleLabel14;
    private JLabel invisibleLabel15;
    private JLabel invisibleLabel16;
    private JLabel invisibleLabel17;
    private JLabel invisibleLabel18;
    private JLabel invisibleLabel19;
    private JLabel invisibleLabel20;
    private JLabel invisibleLabel21;
    private JLabel invisibleLabel22;
    private JLabel invisibleLabel23;
    private JLabel invisibleLabel24;
    private JLabel invisibleLabel25;
    private JLabel invisibleLabel26;
    private JLabel invisibleLabel27;
    private JLabel invisibleLabel28;
    private JLabel invisibleLabel29;
    private JLabel invisibleLabel30;
    private JLabel invisibleLabel31;
    private JLabel invisibleLabel32;
    private JLabel invisibleLabel33;
    private JLabel invisibleLabel34;
    private JLabel invisibleLabel35;
    private JLabel invisibleLabel36;
    private JLabel invisibleLabel37;
    private JLabel invisibleLabel38;
    private JLabel invisibleLabel39;
    private JLabel labelErrorApellido;
    private JLabel labelErrorTipoDoc;
    private JLabel labelErrorTelefono;
    private JLabel labelErrorNacDia;
    private JLabel labelErrorNacMes;
    private JLabel labelErrorNacAnyo;
    private JLabel labelErrorCalle;
    private JLabel labelErrorPais;
    private JLabel labelErrorLocalidad;
    private JLabel labelErrorCuit;
    private JLabel labelErrorNombre;
    private JLabel labelErrorNroDoc;
    private JLabel labelErrorEmail;
    private JLabel labelErrorNacionalidad;
    private JLabel labelErrorNroCalle;
    private JLabel labelErrorDepto;
    private JLabel labelErrorPiso;
    private JLabel labelErrorProvincia;
    private JLabel labelErrorCp;
    private JLabel labelErrorPosIva;
    private JLabel labelErrorOcupacion;

    /***************************************************************************************************************
     *                                           ATRIBUTOS - LÓGICA
     * *************************************************************************************************************/

    private final Color COLOR_FUENTE_ERROR = new Color(255, 0, 0);
    private final int MAX_SIZE_APELLIDO = 50;
    private final int MAX_SIZE_NOMBRES = 50;
    private final int MAX_SIZE_NRO_DOC = 15;
    private final int MAX_SIZE_TELEFONO = 20;
    private final int MAX_SIZE_EMAIL = 30;
    private final int MAX_SIZE_NACIONALIDAD = 20;
    private final int MAX_SIZE_CALLE = 30;
    private final int MAX_SIZE_NRO_CALLE = 5;
    private final int MAX_SIZE_DEPTO = 5;
    private final int MAX_SIZE_PISO = 3;
    private final int MAX_SIZE_CP = 10;
    private final int MAX_SIZE_CUIT = 13;
    private final int MAX_SIZE_OCUPACION = 30;
    private PasajeroDTO pasajeroCreado = null;

    /***************************************************************************************************************
     *                                                CONSTRUCTORES
     * *************************************************************************************************************/

    public VentanaAltaPasajero(Frame owner, boolean modal) {

        super(owner, modal);

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    /***************************************************************************************************************
     *                                  MÉTODOS DE CREACIÓN LLAMADOS EN EL CONSTRUCTOR
     * *************************************************************************************************************/

    private void inicializarComponentes() {

        comboTipoDoc.setModel(new DefaultComboBoxModel(new String[] {"Seleccione un tipo...", "DNI", "LE", "LC", "Pasaporte", "Otro"}));
        comboPosIva.setModel(new DefaultComboBoxModel(new String[] {"Seleccione una posición...", "CONSUMIDOR FINAL", "RESPONSABLE INSCRIPTO"}));

        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setBackground(new Color(10, 132, 255));

        formatearLabels();

        cargarDias();
        cargarMeses();
        cargarAnyos();

        cargarPaises();

    }

    private void agregarListeners() {

        comboPais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                paisSeleccionado();
            }
        });

        comboProvincia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                provinciaSeleccionada();
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

        setearFocusLostEnCampos();

        /** Lo siguiente hace que el texto que el usuario escribe en los campos
         * aparezca en mayúsculas siempre (por especificación de requerimientos), lo hace el agregado de .toUpperCase().
         * Por otro lado, el if se encarga de limitar la longitud de caracteres. **/

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

        ((AbstractDocument) campoNombre.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NOMBRES) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NOMBRES) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoNroDoc.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NRO_DOC) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NRO_DOC) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoTelefono.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_TELEFONO) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_TELEFONO) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoEmail.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_EMAIL) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_EMAIL) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoNacionalidad.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NACIONALIDAD) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NACIONALIDAD) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoCalle.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_CALLE) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_CALLE) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoNroCalle.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_NRO_CALLE) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_NRO_CALLE) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoDepto.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_DEPTO) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_DEPTO) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoPiso.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_PISO) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_PISO) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoCp.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_CP) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_CP) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoCuit.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_CUIT) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_CUIT) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        ((AbstractDocument) campoOcupacion.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= MAX_SIZE_OCUPACION) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length()) <= MAX_SIZE_OCUPACION) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> this.cancelar(), k, w);

        /** Presionar btnSiguiente cuando presiona ENTER
         *  Sólo para los componentes de texto y el rootPane**/
        KeyStroke kenter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        this.getRootPane().registerKeyboardAction(e -> this.siguiente(), kenter, w);

    }

    private void setearFocusLostEnCampos(){
        comboPais.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboPais.putClientProperty("JComponent.outline", null);
            }
        });
        comboProvincia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboProvincia.putClientProperty("JComponent.outline", null);
            }
        });
        comboLocalidad.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboLocalidad.putClientProperty("JComponent.outline", null);
            }
        });
        comboPosIva.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboPosIva.putClientProperty("JComponent.outline", null);
            }
        });
        comboNacMes.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboNacMes.putClientProperty("JComponent.outline", null);
            }
        });
        comboNacAnyo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboNacAnyo.putClientProperty("JComponent.outline", null);
            }
        });
        comboNacDia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboNacDia.putClientProperty("JComponent.outline", null);
            }
        });
        comboTipoDoc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                comboTipoDoc.putClientProperty("JComponent.outline", null);
            }
        });
        //---------
        campoOcupacion.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoOcupacion.putClientProperty("JComponent.outline", null);
            }
        });
        campoCuit.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoCuit.putClientProperty("JComponent.outline", null);
            }
        });
        campoCp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoCp.putClientProperty("JComponent.outline", null);
            }
        });
        campoPiso.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoPiso.putClientProperty("JComponent.outline", null);
            }
        });
        campoDepto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoDepto.putClientProperty("JComponent.outline", null);
            }
        });
        campoNroCalle.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoNroCalle.putClientProperty("JComponent.outline", null);
            }
        });
        campoCalle.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoCalle.putClientProperty("JComponent.outline", null);
            }
        });
        campoNacionalidad.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoNacionalidad.putClientProperty("JComponent.outline", null);
            }
        });
        campoEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoEmail.putClientProperty("JComponent.outline", null);
            }
        });
        campoNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoNombre.putClientProperty("JComponent.outline", null);
            }
        });
        campoApellido.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoApellido.putClientProperty("JComponent.outline", null);
            }
        });
        campoTelefono.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoTelefono.putClientProperty("JComponent.outline", null);
            }
        });
        campoNroDoc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                campoNroDoc.putClientProperty("JComponent.outline", null);
            }
        });
    }

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        setTitle("Alta de pasajeros");
//        setMinimumSize(new Dimension(1100, 600));
        setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void cargarPaises() {

        GestorGeografico gg = GestorGeografico.obtenerInstancia();

        List<PaisDTO> paises = new ArrayList<>();

        try {

            paises = gg.getAllPaises();

        } catch (HibernateException e) {

            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar obtener los países de la base de datos.\n\n" + e.getMessage());
            this.cancelar();

        }

        comboPais.addItem(new ComboItemNombreId(-1, "Seleccione un país..."));

        for (PaisDTO p: paises) {

            comboPais.addItem(new ComboItemNombreId(p.getId_pais(), p.getNombre()));

        }

        comboPais.setSelectedItem(new ComboItemNombreId(1,"Argentina"));
        paisSeleccionado();
        provinciaSeleccionada();

    }

    private void cargarDias() {

        comboNacDia.setModel(new DefaultComboBoxModel());

        comboNacDia.addItem("Día...");

        for (int i = 1; i < 32; i++) {
            comboNacDia.addItem(Integer.toString(i));
        }

    }

    private void cargarMeses() {

        comboNacMes.setModel(new DefaultComboBoxModel());

        comboNacMes.addItem("Mes...");
        comboNacMes.addItem("Enero");
        comboNacMes.addItem("Febrero");
        comboNacMes.addItem("Marzo");
        comboNacMes.addItem("Abril");
        comboNacMes.addItem("Mayo");
        comboNacMes.addItem("Junio");
        comboNacMes.addItem("Julio");
        comboNacMes.addItem("Agosto");
        comboNacMes.addItem("Septiembre");
        comboNacMes.addItem("Octubre");
        comboNacMes.addItem("Noviembre");
        comboNacMes.addItem("Diciembre");

    }

    private void cargarAnyos() {

        comboNacAnyo.setModel(new DefaultComboBoxModel());

        comboNacAnyo.addItem("Año...");

        int anyoActual = LocalDate.now().getYear();

        for (int i = anyoActual; i > (anyoActual-150); i--) {
            comboNacAnyo.addItem(Integer.toString(i));
        }

    }

    private void formatearLabels() {

        Color colorFuenteValido = getBackground();

        invisibleLabel1.setForeground(colorFuenteValido);
        invisibleLabel2.setForeground(colorFuenteValido);
        invisibleLabel3.setForeground(colorFuenteValido);
        invisibleLabel4.setForeground(colorFuenteValido);
        invisibleLabel5.setForeground(colorFuenteValido);
        invisibleLabel6.setForeground(colorFuenteValido);
        invisibleLabel7.setForeground(colorFuenteValido);
        invisibleLabel8.setForeground(colorFuenteValido);
        invisibleLabel9.setForeground(colorFuenteValido);
        invisibleLabel10.setForeground(colorFuenteValido);
        invisibleLabel11.setForeground(colorFuenteValido);
        invisibleLabel12.setForeground(colorFuenteValido);
        invisibleLabel13.setForeground(colorFuenteValido);
        invisibleLabel14.setForeground(colorFuenteValido);
        invisibleLabel15.setForeground(colorFuenteValido);
        invisibleLabel16.setForeground(colorFuenteValido);
        invisibleLabel17.setForeground(colorFuenteValido);
        invisibleLabel18.setForeground(colorFuenteValido);
        invisibleLabel19.setForeground(colorFuenteValido);
        invisibleLabel20.setForeground(colorFuenteValido);
        invisibleLabel21.setForeground(colorFuenteValido);
        invisibleLabel22.setForeground(colorFuenteValido);
        invisibleLabel23.setForeground(colorFuenteValido);
        invisibleLabel24.setForeground(colorFuenteValido);
        invisibleLabel25.setForeground(colorFuenteValido);
        invisibleLabel26.setForeground(colorFuenteValido);
        invisibleLabel27.setForeground(colorFuenteValido);
        invisibleLabel28.setForeground(colorFuenteValido);
        invisibleLabel29.setForeground(colorFuenteValido);
        invisibleLabel30.setForeground(colorFuenteValido);
        invisibleLabel31.setForeground(colorFuenteValido);
        invisibleLabel32.setForeground(colorFuenteValido);
        invisibleLabel33.setForeground(colorFuenteValido);
        invisibleLabel34.setForeground(colorFuenteValido);
        invisibleLabel35.setForeground(colorFuenteValido);
        invisibleLabel36.setForeground(colorFuenteValido);
        invisibleLabel37.setForeground(colorFuenteValido);
        invisibleLabel38.setForeground(colorFuenteValido);
        invisibleLabel39.setForeground(colorFuenteValido);

        labelErrorApellido.setForeground(colorFuenteValido);
        labelErrorNombre.setForeground(colorFuenteValido);
        labelErrorTipoDoc.setForeground(colorFuenteValido);
        labelErrorNroDoc.setForeground(colorFuenteValido);
        labelErrorTelefono.setForeground(colorFuenteValido);
        labelErrorEmail.setForeground(colorFuenteValido);
        labelErrorNacDia.setForeground(colorFuenteValido);
        labelErrorNacMes.setForeground(colorFuenteValido);
        labelErrorNacAnyo.setForeground(colorFuenteValido);
        labelErrorNacionalidad.setForeground(colorFuenteValido);
        labelErrorCalle.setForeground(colorFuenteValido);
        labelErrorNroCalle.setForeground(colorFuenteValido);
        labelErrorDepto.setForeground(colorFuenteValido);
        labelErrorPiso.setForeground(colorFuenteValido);
        labelErrorPais.setForeground(colorFuenteValido);
        labelErrorProvincia.setForeground(colorFuenteValido);
        labelErrorCp.setForeground(colorFuenteValido);
        labelErrorLocalidad.setForeground(colorFuenteValido);
        labelErrorPosIva.setForeground(colorFuenteValido);
        labelErrorCuit.setForeground(colorFuenteValido);
        labelErrorOcupacion.setForeground(colorFuenteValido);

    }

    /***************************************************************************************************************
     *                                      MÉTODOS ACCIONADOS CON LOS BOTONES
     * *************************************************************************************************************/

    private void siguiente() {

        if (validarCampos()) {

            PasajeroDTO nuevoPasajeroDTO = crearDTO();
            long id = -1;

            try {

                id = GestorPersona.obtenerInstancia().crearPasajero(nuevoPasajeroDTO, true);

                JOptionPane.showMessageDialog(this, "Se ha creado el pasajero: "+nuevoPasajeroDTO+"\n\nCon id = "+id);

                pasajeroCreado = nuevoPasajeroDTO;
                pasajeroCreado.setId(id);

                cancelar();

            } catch (PasajeroExisteException e1) {

                if (JOptionPane.showConfirmDialog(this, "Ya existe un pasajero con el mismo tipo y número de documento.\n\n¿Desea continuar de todos modos*") == 0) {

                    try {

                        id = GestorPersona.obtenerInstancia().crearPasajero(nuevoPasajeroDTO, false);

                        JOptionPane.showMessageDialog(this, "Se ha creado el pasajero: "+nuevoPasajeroDTO+"\n\nCon id = "+id);

                        pasajeroCreado = nuevoPasajeroDTO;
                        pasajeroCreado.setId(id);

                        cancelar();

                    } catch (DatosErroneosPasajeroException e3) {

                        JOptionPane.showMessageDialog(this, "Datos no válidos: "+ e1.getMessage());

                    } catch (PasajeroExisteException e4) {
                        // Ya se manejó el error antes;
                    } catch (HibernateException e5) {

                        JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para insertar el nuevo pasajero.\n\n" + e5.getMessage());
                        cancelar();

                    }

                }

            } catch (DatosErroneosPasajeroException e2) {

                JOptionPane.showMessageDialog(this, "Datos no válidos: "+ e2.getMessage());

            } catch (HibernateException e3) {

                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para insertar el nuevo pasajero.\n\n" + e3.getMessage());
                cancelar();

            }

        }

        this.repaint();

    }

    private void cancelar() {

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

    private void paisSeleccionado() {

        cargarProvincias();

    }

    private void provinciaSeleccionada() {

        cargarLocalidades();

    }

    private void cargarProvincias() {

        if (((ComboItemNombreId)comboPais.getSelectedItem()).getId() >= 0) {

            GestorGeografico gg = GestorGeografico.obtenerInstancia();

            PaisDTO paisDTO = new PaisDTO();
            paisDTO.setId_pais(((ComboItemNombreId)comboPais.getSelectedItem()).getId());
            paisDTO.setNombre(((ComboItemNombreId)comboPais.getSelectedItem()).getNombre());

            List<ProvinciaDTO> provincias = new ArrayList<>();

            try {

                provincias = gg.getAllProvinciasPais(paisDTO);

            } catch (HibernateException e) {

                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar obtener las provincias de la base de datos.\n\n" + e.getMessage());

            }

            comboProvincia.setModel(new DefaultComboBoxModel());

            comboProvincia.addItem(new ComboItemNombreId(-1, "Seleccione una provincia..."));

            for (ProvinciaDTO p: provincias) {

                comboProvincia.addItem(new ComboItemNombreId(p.getId_provincia(), p.getNombre()));

            }

        } else {

            comboProvincia.setModel(new DefaultComboBoxModel());
            comboProvincia.addItem(new ComboItemNombreId(-1, ""));

        }

        if (((ComboItemNombreId)comboPais.getSelectedItem()).getId() == 1) {

            comboProvincia.setSelectedItem(new ComboItemNombreId(82,"Santa Fe"));

        } else {

            comboProvincia.setSelectedItem(new ComboItemNombreId(-1,""));

        }

    }

    private void cargarLocalidades() {

        if (((ComboItemNombreId)comboProvincia.getSelectedItem()).getId() >= 0) {

            GestorGeografico gg = GestorGeografico.obtenerInstancia();

            ProvinciaDTO provinciaDTO = new ProvinciaDTO();

            provinciaDTO.setId_provincia(((ComboItemNombreId)comboProvincia.getSelectedItem()).getId());
            provinciaDTO.setNombre(((ComboItemNombreId)comboProvincia.getSelectedItem()).getNombre());

            List<LocalidadDTO> localidades = new ArrayList<>();

            try {

                localidades= gg.getAllLocalidadesProvincia(provinciaDTO);

            } catch (HibernateException e) {

                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar obtener las localidades de la base de datos.\n\n" + e.getMessage());

            }

            comboLocalidad.setModel(new DefaultComboBoxModel());
            comboLocalidad.addItem(new ComboItemNombreId(-1, "Seleccione una localidad..."));

            for (LocalidadDTO l: localidades) {

                comboLocalidad.addItem(new ComboItemNombreId(l.getId_localidad(), l.getNombre()));

            }

        } else {

            comboLocalidad.setModel(new DefaultComboBoxModel());
            comboLocalidad.addItem(new ComboItemNombreId(-1, ""));

        }

        if (((ComboItemNombreId)comboProvincia.getSelectedItem()).getId() == 82) {

            comboLocalidad.setSelectedItem(new ComboItemNombreId(82063170,"Santa Fe"));

        } else {

            comboLocalidad.setSelectedItem(new ComboItemNombreId(-1,""));

        }

    }

    /***************************************************************************************************************
     *                                           VALIDACIÓN DE CAMPOS
     * *************************************************************************************************************/

    private boolean validarCampos() {

        /* Se almacenan en variables para forzar a que se llame a todas las funciones. Si en lugar de esto se llamaran
         directamente las funciones en el return, el operador AND finalizaría las evaluaciones en el primer falso que
          encuentra y entonces no se llamaría a todas las demás funciones que todavía no se evaluaron. */
        boolean apellidoValido = validarApellido();
        boolean nombreValido = validarNombre();
        boolean tipoDocValido = validarTipoDoc();
        boolean nroDocValido = validarNroDoc();
        boolean telefonoValido = validarTelefono();
        boolean emailValido = validarEmail();
        boolean fechaNacValida = validarFechaNac();
        boolean nacionalidadValida = validarNacionalidad();
        boolean calleValida = validarCalle();
        boolean nroCalleValido = validarNroCalle();
        boolean deptoValido = validarDepto();
        boolean pisoValido = validarPiso();
        boolean cpValido = validarCp();
        boolean paisValido = validarPais();
        boolean provinciaValida = validarProvincia();
        boolean localidadValida = validarLocalidad();
        boolean posIvaValida = validarPosIva();
        boolean cuitValido = validarCuit();
        boolean ocupacionValida = validarOcupacion();

        pack();

        return
                apellidoValido
                        && nombreValido
                        && tipoDocValido
                        && nroDocValido
                        && telefonoValido
                        && emailValido
                        && fechaNacValida
                        && nacionalidadValida
                        && calleValida
                        && nroCalleValido
                        && deptoValido
                        && pisoValido
                        && cpValido
                        && paisValido
                        && provinciaValida
                        && localidadValida
                        && posIvaValida
                        && cuitValido
                        && ocupacionValida;

    }

    private boolean validarApellido() {

        String apellido = campoApellido.getText().trim();

        if (apellido.equals("")) {

            labelErrorApellido.setText("Campo obligatorio.");
            labelErrorApellido.setForeground(COLOR_FUENTE_ERROR);
            campoApellido.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!apellido.matches("^[A-ZÁÉÍÓÚÑ\\.\s]+$")) {

            labelErrorApellido.setText("Caracteres no válidos.");
            labelErrorApellido.setForeground(COLOR_FUENTE_ERROR);
            campoApellido.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorApellido.setText(".");
        labelErrorApellido.setForeground(getBackground());
        return true;

    }

    private boolean validarNombre() {

        String nombre = campoNombre.getText().trim();

        if (nombre.equals("")) {

            labelErrorNombre.setText("Campo obligatorio.");
            labelErrorNombre.setForeground(COLOR_FUENTE_ERROR);
            campoNombre.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!nombre.matches("^[A-ZÁÉÍÓÚÑ\\.\s]+$")) {

            labelErrorNombre.setText("Caracteres no válidos.");
            labelErrorNombre.setForeground(COLOR_FUENTE_ERROR);
            campoNombre.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorNombre.setText(".");
        labelErrorNombre.setForeground(getBackground());
        return true;

    }

    private boolean validarTipoDoc() {

        if (comboTipoDoc.getSelectedIndex()==0) {

            labelErrorTipoDoc.setText("Seleccione un tipo.");
            labelErrorTipoDoc.setForeground(COLOR_FUENTE_ERROR);
            comboTipoDoc.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorTipoDoc.setText(".");
        labelErrorTipoDoc.setForeground(getBackground());
        return true;

    }

    private boolean validarNroDoc() {

        String nroDoc = campoNroDoc.getText().trim();
        String tipoDoc = comboTipoDoc.getSelectedItem().toString();

        if (nroDoc.equals("")) {

            labelErrorNroDoc.setText("Campo obligatorio.");
            labelErrorNroDoc.setForeground(COLOR_FUENTE_ERROR);
            campoNroDoc.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if ((!nroDoc.matches("^$|^[1-9][0-9]{7}$") && tipoDoc.equals("DNI"))
                || (!nroDoc.matches("^$|^[0-9]{1,8}$|^[1-9][0-9]{0,6}$") && ((tipoDoc.equals("LE")) || (tipoDoc.equals("LC"))))
                || (!nroDoc.matches("^$|^[A-Z0-9]{6,9}$") && tipoDoc.equals("Pasaporte"))
                || (!nroDoc.matches("^$|^[A-Z0-9]{1,15}$") && (tipoDoc.equals("Otro") || tipoDoc.equals("")))) {

            labelErrorNroDoc.setText("Formato no válido para el tipo especificado.");
            labelErrorNroDoc.setForeground(COLOR_FUENTE_ERROR);
            campoNroDoc.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorNroDoc.setText(".");
        labelErrorNroDoc.setForeground(getBackground());
        return true;

    }

    private boolean validarTelefono() {

        String telefono = campoTelefono.getText().trim();

        if (!telefono.matches("^$|^[0-9\\-\\(\\)\s]+$")) {
            labelErrorTelefono.setText("Caracteres no válidos.");
            labelErrorTelefono.setForeground(COLOR_FUENTE_ERROR);
            campoTelefono.putClientProperty("JComponent.outline", "error");
            return false;
        }

        labelErrorTelefono.setText(".");
        labelErrorTelefono.setForeground(getBackground());
        return true;

    }

    private boolean validarEmail() {

        String email = campoEmail.getText().trim();

        if (!email.matches("^$|^[A-ZÑ0-9\\_\\-\\.]+@[A-ZÑ0-9]+\\.[A-ZÑ0-9]+(\\.[A-ZÑ]+)*$")) {

            labelErrorEmail.setText("Formato no válido.");
            labelErrorEmail.setForeground(COLOR_FUENTE_ERROR);
            campoEmail.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorEmail.setText(".");
        labelErrorEmail.setForeground(getBackground());
        return true;

    }

    private boolean validarFechaNac() {

        int nroDia = comboNacDia.getSelectedIndex();
        int nroMes = comboNacMes.getSelectedIndex();
        int nroAnyo = comboNacAnyo.getSelectedIndex();
        boolean diasErroneosParaMes = (nroDia == 31 && (nroMes == 2 || nroMes == 4 || nroMes == 6 || nroMes == 9 || nroMes == 11)) || (nroDia > 29 && nroMes == 2);
        boolean hayErrores = false;

        if (nroDia == 0) {

            labelErrorNacDia.setText("Seleccione un día.");
            labelErrorNacDia.setForeground(COLOR_FUENTE_ERROR);
            comboNacDia.putClientProperty("JComponent.outline", "error");
            hayErrores = true;

        } else if (diasErroneosParaMes) {

            labelErrorNacDia.setText("Día no válido.");
            labelErrorNacDia.setForeground(COLOR_FUENTE_ERROR);
            comboNacDia.putClientProperty("JComponent.outline", "error");

            hayErrores = true;

        } else {

            labelErrorNacDia.setText(".");
            labelErrorNacDia.setForeground(getBackground());

        }

        if (nroMes == 0) {

            labelErrorNacMes.setText("Seleccione un mes.");
            labelErrorNacMes.setForeground(COLOR_FUENTE_ERROR);
            comboNacMes.putClientProperty("JComponent.outline", "error");
            hayErrores = true;

        } else {

            labelErrorNacMes.setText(".");
            labelErrorNacMes.setForeground(getBackground());

        }

        if (nroAnyo == 0) {

            labelErrorNacAnyo.setText("Seleccione un año.");
            labelErrorNacAnyo.setForeground(COLOR_FUENTE_ERROR);
            comboNacAnyo.putClientProperty("JComponent.outline", "error");
            hayErrores = true;

        } else {

            labelErrorNacAnyo.setText(".");
            labelErrorNacAnyo.setForeground(getBackground());

        }

        return !hayErrores;

    }

    private boolean validarNacionalidad() {

        String nacionalidad = campoNacionalidad.getText().trim();

        if (nacionalidad.equals("")) {

            labelErrorNacionalidad.setText("Campo obligatorio.");
            labelErrorNacionalidad.setForeground(COLOR_FUENTE_ERROR);
            campoNacionalidad.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!nacionalidad.matches("^[A-ZÁÉÍÓÚÑ\s]+$")) {

            labelErrorNacionalidad.setText("Caracteres no válidos.");
            labelErrorNacionalidad.setForeground(COLOR_FUENTE_ERROR);
            campoNacionalidad.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorNacionalidad.setText(".");
        labelErrorNacionalidad.setForeground(getBackground());
        return true;

    }

    private boolean validarCalle() {

        String calle = campoCalle.getText().trim();

        if (calle.equals("")) {

            labelErrorCalle.setText("Campo obligatorio.");
            labelErrorCalle.setForeground(COLOR_FUENTE_ERROR);
            campoCalle.putClientProperty("JComponent.outline", "error");
            return false;

        }
        if (!calle.matches("^[A-Z0-9ÁÉÍÓÚÑ\\.\s]+$")) {

            labelErrorCalle.setText("Caracteres no válidos.");
            labelErrorCalle.setForeground(COLOR_FUENTE_ERROR);
            campoCalle.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorCalle.setText(".");
        labelErrorCalle.setForeground(getBackground());
        return true;

    }

    private boolean validarNroCalle() {

        String nroCalle = campoNroCalle.getText().trim();

        if (nroCalle.equals("")) {

            labelErrorNroCalle.setText("Campo obligatorio.");
            labelErrorNroCalle.setForeground(COLOR_FUENTE_ERROR);
            campoNroCalle.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!nroCalle.matches("^[A-ZÑ0-9\s]+$")) {

            labelErrorNroCalle.setText("Caracteres no válidos.");
            labelErrorNroCalle.setForeground(COLOR_FUENTE_ERROR);
            campoNroCalle.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorNroCalle.setText(".");
        labelErrorNroCalle.setForeground(getBackground());
        return true;

    }

    private boolean validarDepto() {

        String depto = campoDepto.getText().trim();

        if (!depto.matches("^$|^[A-ZÑ0-9\s]+$")) {

            labelErrorDepto.setText("Caracteres no válidos.");
            labelErrorDepto.setForeground(COLOR_FUENTE_ERROR);
            campoDepto.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorDepto.setText(".");
        labelErrorDepto.setForeground(getBackground());
        return true;

    }

    private boolean validarPiso() {

        String piso = campoPiso.getText().trim();

        if (!piso.matches("^$|^[A-ZÑ0-9\s]+$")) {

            labelErrorPiso.setText("Caracteres no válidos.");
            labelErrorPiso.setForeground(COLOR_FUENTE_ERROR);
            campoPiso.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorPiso.setText(".");
        labelErrorPiso.setForeground(getBackground());
        return true;

    }

    private boolean validarCp() {

        String cp = campoCp.getText().trim();

        if (cp.equals("")) {

            labelErrorCp.setText("Campo obligatorio.");
            labelErrorCp.setForeground(COLOR_FUENTE_ERROR);
            campoCp.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!cp.matches("^[A-ZÑ0-9\s]+$")) {

            labelErrorCp.setText("Caracteres no válidos.");
            labelErrorCp.setForeground(COLOR_FUENTE_ERROR);
            campoCp.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorCp.setText(".");
        labelErrorCp.setForeground(getBackground());
        return true;

    }

    private boolean validarPais() {

        if (comboPais.getSelectedIndex() == 0) {

            labelErrorPais.setText("Seleccione un país.");
            labelErrorPais.setForeground(COLOR_FUENTE_ERROR);
            comboPais.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorPais.setText(".");
        labelErrorPais.setForeground(getBackground());
        return true;

    }

    private boolean validarProvincia() {

        if (comboProvincia.getSelectedIndex() == 0) {

            labelErrorProvincia.setText("Seleccione una provincia.");
            labelErrorProvincia.setForeground(COLOR_FUENTE_ERROR);
            comboProvincia.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorProvincia.setText(".");
        labelErrorProvincia.setForeground(getBackground());
        return true;

    }

    private boolean validarLocalidad() {

        if (comboLocalidad.getSelectedIndex() == 0) {

            labelErrorLocalidad.setText("Seleccione una localidad.");
            labelErrorLocalidad.setForeground(COLOR_FUENTE_ERROR);
            comboLocalidad.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorLocalidad.setText(".");
        labelErrorLocalidad.setForeground(getBackground());
        return true;

    }

    private boolean validarPosIva() {

        if (comboPosIva.getSelectedIndex() == 0) {

            labelErrorPosIva.setText("Seleccione una posición.");
            labelErrorPosIva.setForeground(COLOR_FUENTE_ERROR);
            comboPosIva.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorPosIva.setText(".");
        labelErrorPosIva.setForeground(getBackground());
        return true;

    }

    private boolean validarCuit() {

        String cuit = campoCuit.getText().trim();

        if (cuit.equals("")) {

            if (comboPosIva.getSelectedItem().toString().equals("RESPONSABLE INSCRIPTO")) {

                labelErrorCuit.setText("Campo obligatorio para responsable inscripto.");
                labelErrorCuit.setForeground(COLOR_FUENTE_ERROR);
                campoCuit.putClientProperty("JComponent.outline", "error");
                return false;

            }

        } else if (!cuit.matches("(^[0-9]{2}-[0-9]{8}-[0-9]$)|(^[0-9]{11}$)")) {

            labelErrorCuit.setText("Ingrese un formato del tipo 99-99999999-9 o 99999999999.");
            labelErrorCuit.setForeground(COLOR_FUENTE_ERROR);
            campoCuit.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorCuit.setText(".");
        labelErrorCuit.setForeground(getBackground());
        return true;

    }

    private boolean validarOcupacion() {

        String ocupacion = campoOcupacion.getText().trim();

        if (ocupacion.equals("")) {

            labelErrorOcupacion.setText("Campo obligatorio.");
            labelErrorOcupacion.setForeground(COLOR_FUENTE_ERROR);
            campoOcupacion.putClientProperty("JComponent.outline", "error");
            return false;

        }

        if (!ocupacion.matches("[A-Z0-9ÁÉÍÓÚÑ\s]+")) {

            labelErrorOcupacion.setText("Caracteres no válidos.");
            labelErrorOcupacion.setForeground(COLOR_FUENTE_ERROR);
            campoOcupacion.putClientProperty("JComponent.outline", "error");
            return false;

        }

        labelErrorOcupacion.setText(".");
        labelErrorOcupacion.setForeground(getBackground());
        return true;

    }

    /***************************************************************************************************************
     *                                           OTROS MÉTODOS
     * *************************************************************************************************************/

    private DireccionDTO crearDireccionDTO() {

        DireccionDTO direccion = new DireccionDTO();

        direccion.setCalle(campoCalle.getText().trim());
        direccion.setNroCalle(campoNroCalle.getText().trim());
        direccion.setDepto(campoDepto.getText().trim());
        direccion.setPiso(campoPiso.getText().trim());
        direccion.setCp(campoCp.getText().trim());

        PaisDTO pais = new PaisDTO();
        pais.setNombre(((ComboItemNombreId) comboPais.getSelectedItem()).getNombre());
        pais.setId_pais(((ComboItemNombreId) comboPais.getSelectedItem()).getId());

        ProvinciaDTO provincia = new ProvinciaDTO();
        provincia.setNombre(((ComboItemNombreId) comboProvincia.getSelectedItem()).getNombre());
        provincia.setId_provincia(((ComboItemNombreId) comboProvincia.getSelectedItem()).getId());
        provincia.setPais(pais);

        LocalidadDTO localidad = new LocalidadDTO();
        localidad.setId_localidad(((ComboItemNombreId) comboLocalidad.getSelectedItem()).getId());
        localidad.setNombre(((ComboItemNombreId) comboLocalidad.getSelectedItem()).getNombre());
        localidad.setProvincia(provincia);

        direccion.setLocalidad(localidad);

        return direccion;

    }

    private PasajeroDTO crearDTO() {

        PasajeroDTO nuevoPasajero = new PasajeroDTO();

        nuevoPasajero.setApellido(campoApellido.getText().trim());
        nuevoPasajero.setNombre(campoNombre.getText().trim());
        nuevoPasajero.setNroDoc(campoNroDoc.getText().trim());
        nuevoPasajero.setTelefono(campoTelefono.getText().trim());
        nuevoPasajero.setEmail(campoEmail.getText().trim());
        nuevoPasajero.setNacionalidad(campoNacionalidad.getText().trim());
        nuevoPasajero.setOcupacion(campoOcupacion.getText().trim());

        String cuit = campoCuit.getText().trim();
        if (cuit.matches("^[0-9]{11}$")) {
            cuit = cuit.substring(0, 2) + "-" + cuit.substring(2,10) + "-" + cuit.substring(10);
        }
        nuevoPasajero.setCuit(cuit);

        nuevoPasajero.setFechaNacimiento(LocalDate.of(
                Integer.parseInt(comboNacAnyo.getSelectedItem().toString()),
                comboNacMes.getSelectedIndex(),
                comboNacDia.getSelectedIndex()
        ));

        nuevoPasajero.setDireccion(crearDireccionDTO());

        switch (comboPosIva.getSelectedIndex()) {
            case 1:
                nuevoPasajero.setPosicionIva(PosicionIva.CONSUMIDOR_FINAL);
                break;
            case 2:
                nuevoPasajero.setPosicionIva(PosicionIva.RESPONSABLE_INSCRIPTO);
                break;
            default:
                nuevoPasajero.setPosicionIva(null);
        }

        switch (comboTipoDoc.getSelectedIndex()) {
            case 1:
                nuevoPasajero.setTipoDoc(TipoDoc.DNI);
                break;
            case 2:
                nuevoPasajero.setTipoDoc(TipoDoc.LE);
                break;
            case 3:
                nuevoPasajero.setTipoDoc(TipoDoc.LC);
                break;
            case 4:
                nuevoPasajero.setTipoDoc(TipoDoc.PASAPORTE);
                break;
            case 5:
                nuevoPasajero.setTipoDoc(TipoDoc.OTRO);
                break;
            default:
                nuevoPasajero.setTipoDoc(null);
        }

        return nuevoPasajero;

    }

    public PasajeroDTO getPasajeroCreado() {
        return pasajeroCreado;
    }

}

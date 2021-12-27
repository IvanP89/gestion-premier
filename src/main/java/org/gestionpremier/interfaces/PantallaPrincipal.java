package org.gestionpremier.interfaces;

import org.gestionpremier.dao.Cleaner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class PantallaPrincipal extends JFrame {

    /***************************************************************************************************************
     *                                      ATRIBUTOS - ELEMENTOS GRÁFICOS
     * *************************************************************************************************************/

    private JPanel panelPrincipal;
    private JButton btnCerrarSesion;
    private JLabel nombreUsuarioLogueado;
    private JLabel iconoUsuario;
    private JPanel panelToolbar;
    private JPanel panelBotonesPrincipales;
    private JLabel tituloHoteleria;
    private JButton btnGestionarHabitaciones;
    private JButton btnGestionarPasajero;
    private JButton btnCancelarReservas;
    private JButton btnGestionarResponsable;
    private JButton btnFacturar;
    private JButton btnIngresarPago;
    private JButton btnIngresarNotaDeCredito;
    private JButton btnGestionarListados;
    private JLabel tituloContabilidad;

    /***************************************************************************************************************
     *                                                CONSTRUCTORES
     * *************************************************************************************************************/

    public PantallaPrincipal() {

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    /***************************************************************************************************************
     *                                  MÉTODOS DE CREACIÓN LLAMADOS EN EL CONSTRUCTOR
     * *************************************************************************************************************/

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        setTitle("Inicio - Gestión Premier");
        setMinimumSize(new Dimension(850, 650));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void inicializarComponentes() {

        cargarIconos();
        formatearTextos();

        btnCancelarReservas.setEnabled(false);
        //btnFacturar.setEnabled(false);
        btnGestionarListados.setEnabled(false);
        btnGestionarResponsable.setEnabled(false);
        btnIngresarNotaDeCredito.setEnabled(false);
        btnIngresarPago.setEnabled(false);
        btnCerrarSesion.setEnabled(false);

    }

    private void cargarIconos() {

        // Carga del ícono de usuario en la toolbar.
        URL pathRecurso = this.getClass().getClassLoader().getResource("icons/user.png");
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(pathRecurso).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        iconoUsuario.setIcon(imageIcon);
        iconoUsuario.setText("");

        // Carga del ícono del programa en la barra superior.
        pathRecurso = this.getClass().getClassLoader().getResource("icons/hotel.png");
        this.setIconImage(new ImageIcon(pathRecurso).getImage());

    }

    private void formatearTextos() {

        String fuenteDefault = new JLabel().getFont().getFontName();

        Font fuenteTitulo = new Font(fuenteDefault, Font.BOLD, 30);
        tituloHoteleria.setFont(fuenteTitulo);
        tituloContabilidad.setFont(fuenteTitulo);

        Font fuenteBtnPrincipal = new Font(fuenteDefault, Font.PLAIN, 20);
        btnGestionarHabitaciones.setFont(fuenteBtnPrincipal);
        btnGestionarPasajero.setFont(fuenteBtnPrincipal);
        btnCancelarReservas.setFont(fuenteBtnPrincipal);
        btnGestionarResponsable.setFont(fuenteBtnPrincipal);
        btnFacturar.setFont(fuenteBtnPrincipal);
        btnIngresarPago.setFont(fuenteBtnPrincipal);
        btnIngresarNotaDeCredito.setFont(fuenteBtnPrincipal);
        btnGestionarListados.setFont(fuenteBtnPrincipal);

    }

    private void agregarListeners() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrandoVentana();
                super.windowClosing(e);
            }
        });

        btnGestionarPasajero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gestionarPasajero();
            }
        });

        btnGestionarHabitaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gestionarHabitaciones();
            }
        });

        btnFacturar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                facturar();
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> this.esc(), k, w);

    }

    /***************************************************************************************************************
     *                                      MÉTODOS ACCIONADOS CON LOS BOTONES
     * *************************************************************************************************************/

    private void gestionarPasajero() {

        new VentanaGestionarPasajero(this, true);

    }

    private void gestionarHabitaciones() {

        new VentanaEstadoHabitaciones(this, true);

    }

    private void facturar(){

        new VentanaFacturar1(this, true);

    }

    /***************************************************************************************************************
     *                                               OTROS MÉTODOS
     * *************************************************************************************************************/

    private void cerrandoVentana() {

        Cleaner.cerrarConexiones();

    }

    private void esc() {

        this.cerrandoVentana();
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

}

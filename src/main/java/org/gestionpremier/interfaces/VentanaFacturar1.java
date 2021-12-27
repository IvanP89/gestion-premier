package org.gestionpremier.interfaces;

import com.formdev.flatlaf.FlatClientProperties;
import org.gestionpremier.dto.*;
import org.gestionpremier.excepciones.DatosErroneosResponsablePagoException;
import org.gestionpremier.excepciones.IdErroneoException;
import org.gestionpremier.excepciones.ObjetoNoEncontradoException;
import org.gestionpremier.dto.*;
import org.gestionpremier.negocio.gestores.GestorEstadia;
import org.gestionpremier.negocio.gestores.GestorHabitacion;
import org.gestionpremier.negocio.gestores.GestorPersona;
import org.hibernate.HibernateException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaFacturar1 extends JDialog{
    private static final short NADA=0;
    private static final short PASAJERO=1;
    private static final short RESPONSABLE=2;


    private JTextField tfNumero;
    private JButton buscarButton;
    private JTable tablaPasajeros;
    private JComboBox comboHora;
    private JComboBox comboMinutos;
    private JComboBox comboSegundos;
    private JButton ingresarResponsableButton;
    private JLabel labelCUIT;
    private JLabel labelRazonSocial;
    private JPanel panelResponsable;
    private JPanel panelPasajero;
    private JButton cancelarButton;
    private JButton siguienteButton;
    private JPanel contenedor;
    private JPanel panelBuscar;

    private PasajeroDTO pasajeroDTO;
    private ResponsableDePagoDTO responsableDTO;
    private EstadiaDTO estadiaDTO;
    private short seleccionado=NADA;

    public VentanaFacturar1(Frame owner, boolean modal){
        //FlatIntelliJLaf.setup();
        super(owner, modal);

        inicializarComponentes();
        agregarListeners();
        configurarVentana();

    }

    private void inicializarComponentes(){

        tfNumero.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "");

        buscarButton.setBackground(new Color(10, 132, 255));
        buscarButton.setForeground(Color.WHITE);
        buscarButton.putClientProperty("JButton.buttonType", "roundRect");
        buscarButton.setFont(new Font(null, Font.PLAIN, 16));


        siguienteButton.setBackground(new Color(10, 132, 255));
        siguienteButton.setForeground(Color.WHITE);
        siguienteButton.setEnabled(false);

        ingresarResponsableButton.setBackground(new Color(10, 132, 255));
        ingresarResponsableButton.setForeground(Color.WHITE);
        ingresarResponsableButton.setEnabled(false);
        ingresarResponsableButton.putClientProperty("JButton.buttonType", "roundRect");
        ingresarResponsableButton.setFont(new Font(null, Font.PLAIN, 16));


        tablaPasajeros.setModel(new ModeloTablaFacturarPasajeros(new LinkedList<PasajeroDTO>()));
        tablaPasajeros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        comboHora.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        comboHora.setSelectedItem(String.valueOf(LocalTime.now().getHour()));

        comboMinutos.setModel(new DefaultComboBoxModel(new String[] {"0", "15", "30", "45"}));
        comboMinutos.setSelectedItem(String.valueOf(LocalTime.now().getMinute()));

        comboSegundos.setModel(new DefaultComboBoxModel(new String[] {"0", "15", "30", "45"}));
        comboSegundos.setSelectedItem(String.valueOf(LocalTime.now().getSecond()));

        panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación"));

        panelBuscar.setBorder(new LineBorder(Color.LIGHT_GRAY));

        panelResponsable.setBorder(new TitledBorder("Facturar a tercero"));

        labelCUIT.setText("00-00.000.000-0");

        labelRazonSocial.setText("No seleccionado");

    }

    private void agregarListeners(){

        GestorPersona gestorPersona=GestorPersona.obtenerInstancia();
        GestorEstadia gestorEstadia=GestorEstadia.obtenerInstancia();

        tfNumero.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                tfNumero.putClientProperty("JComponent.border", null);
            }
        });

        buscarButton.addActionListener(this::botonBuscar);

        cancelarButton.addActionListener(this::botonCancelar);

        siguienteButton.addActionListener(this::cambiarPantalla);

        ingresarResponsableButton.addActionListener(this::botonIngresarResponsable);

        tablaPasajeros.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(!tablaPasajeros.getSelectionModel().isSelectionEmpty()){
                    panelPasajero.setBorder(new TitledBorder(new LineBorder(new Color(10, 132, 255)), "Ocupantes de la habitación "+tfNumero.getText()));
                    panelResponsable.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Facturar a tercero"));
                    siguienteButton.setEnabled(true);
                    seleccionado=PASAJERO;
                    pasajeroDTO=((ModeloTablaFacturarPasajeros) tablaPasajeros.getModel()).getPasajero(tablaPasajeros.getSelectedRow());
                    responsableDTO=null;
                }
                //System.out.println("SE EJECUTO CAMBIO EN LA TABLA");
            }
        });

        ((AbstractDocument) tfNumero.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                super.insertString(fb, offset, string.toUpperCase(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                super.replace(fb, offset, length, text.toUpperCase(), attrs);
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)), k, w);

        /** Presionar buscar cuando toca ENTER en Numero de habitacion **/
        int kenter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0).getKeyCode();
        tfNumero.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==kenter){
                    buscarButton.doClick();
                }
            }
        });


    }

    private void configurarVentana(){


        this.setTitle("Facturar");
        this.setContentPane(contenedor);
//        this.setSize(new Dimension(1024,720));
        pack();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
//        this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

    }
/*
    private List<PasajeroDTO> dummyListaPasajeros(){
        List<PasajeroDTO> list=new ArrayList<>(4);
        for(int i=0; i<4; i++){
            PasajeroDTO nuevo=new PasajeroDTO();
            nuevo.setId(-1);
            nuevo.setNombre("Juan");
            nuevo.setApellido("Dummy");
            nuevo.setNroDoc(String.valueOf((int)(Math.random()*60000000)));
            list.add(nuevo);
        }

        return list;
    }*/
    /*
    private ResponsableDePagoDTO dummyResponsable(){
        ResponsableDePagoDTO resp = new ResponsableDePagoDTO();
        resp.setCuit(String.valueOf((int)(Math.random()*89)+10) + "-" + String.valueOf((int)(Math.random()*99999999)) + "-" + String.valueOf((int)(Math.random()*9)));
        resp.setId(-1);
        resp.setRazonSocial("Dummy 10 INC");

        return resp;
    }*/

    private void botonBuscar(ActionEvent e){

        GestorEstadia gestorEstadia = GestorEstadia.obtenerInstancia();

        String numero=tfNumero.getText();

        tfNumero.putClientProperty("JComponent.outline", null);

        long idHabitacion = -1;
        try {
            idHabitacion = GestorHabitacion.obtenerInstancia().getHabitacion(numero).getId();
            EstadiaDTO estadiadto = gestorEstadia.getUltimaEstadiaImpagaYaEmpezada(idHabitacion);

            //---------------

            estadiaDTO=null;
            responsableDTO=null;
            siguienteButton.setEnabled(false);
            seleccionado=NADA;
            labelRazonSocial.setText("No seleccionado");
            labelCUIT.setText("00-00000000-0");
            panelResponsable.setBorder(new TitledBorder("Facturar a tercero"));
            panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación"));

            if(estadiadto!=null){
                //Se encontro la estadia impaga
                tfNumero.putClientProperty("JComponent.outline",null);
                estadiaDTO=estadiadto;
                panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación "+tfNumero.getText()));
                ingresarResponsableButton.setEnabled(true);

                List<PasajeroDTO> posiblesPagadores = estadiadto.getPasajeros();
                if(!posiblesPagadores.contains(estadiadto.getPasajeroResponsable()))
                    posiblesPagadores.add(estadiadto.getPasajeroResponsable());
                tablaPasajeros.setModel(new ModeloTablaFacturarPasajeros(posiblesPagadores));
                //tablaPasajeros.setModel(new ModeloTablaFacturarPasajeros(estadiadto.getPasajeros()));
            } else {
                //No hay estadia sin pagar
                JOptionPane.showMessageDialog(this.getParent(), "No hay estadías impagas para esta habitación", "No hay deudas", JOptionPane.PLAIN_MESSAGE);
                tfNumero.putClientProperty("JComponent.outline", "error");
                tablaPasajeros.setModel(new ModeloTablaFacturarPasajeros(new ArrayList<PasajeroDTO>()));
                ingresarResponsableButton.setEnabled(false);
            }
        } catch (ObjetoNoEncontradoException ex) {

            JOptionPane.showMessageDialog(this.getParent(), "No existe habitación "+numero, "Numero de habitación erróneo", JOptionPane.WARNING_MESSAGE);
            estadiaDTO=null;
            responsableDTO=null;
            siguienteButton.setEnabled(false);
            seleccionado=NADA;
            labelRazonSocial.setText("No seleccionado");
            labelCUIT.setText("00-00000000-0");
            panelResponsable.setBorder(new TitledBorder("Facturar a tercero"));
            panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación"));
            tablaPasajeros.setModel(new ModeloTablaFacturarPasajeros(new LinkedList<>()));
            tfNumero.putClientProperty("JComponent.border", "error");

        } catch (HibernateException e2) {

            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos.\n\n" + e2.getMessage());
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        }


    }

    private void cambiarPantalla(ActionEvent e){

        if(seleccionado!=NADA){

            if (seleccionado == PASAJERO && !GestorPersona.obtenerInstancia().esMayorDeEdad(pasajeroDTO)) {
                JOptionPane.showMessageDialog(this, "El cliente a facturar no puede ser menor de edad.");
                return;
            }

            if(hayDeuda(estadiaDTO.getId())) {
                FacturaDTO facturaDTO=new FacturaDTO();

                facturaDTO.setTipo((seleccionado==PASAJERO)? 'B' : 'A');
                facturaDTO.setClienteResponsable(responsableDTO);
                facturaDTO.setClientePasajero(pasajeroDTO);
                facturaDTO.setIdEstadia(estadiaDTO.getId());
                facturaDTO.setIva(0.21f);

                LocalTime input = getHoraSalida();

                try {
                    GestorEstadia.obtenerInstancia().finalizarEstadia(estadiaDTO.getId(), input);
                } catch (IdErroneoException ex) {
                    //Id mal configurado
                    JOptionPane.showMessageDialog(this.getParent(), "ID Habitación erróneo", "ERROR", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (ObjetoNoEncontradoException ex) {
                    //No existe la estadia en la DB 🤨
                    JOptionPane.showMessageDialog(this.getParent(), "No existe estadía "+estadiaDTO.getId(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (HibernateException ex) {

                    JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para finalizar la estadía.\n\n" + ex.getMessage());
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

                }

                facturaDTO.setRenglones(getRenglonesFacturaConsumosImpagos(facturaDTO.getIdEstadia(), facturaDTO));

                VentanaFacturar2 ventanaFacturar2 = new VentanaFacturar2((Frame) getOwner(), true, facturaDTO);

                if (!hayDeuda(facturaDTO.getIdEstadia())) {

                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

                }

            } else {
                siguienteButton.setEnabled(false);
            }

        } else {
            JOptionPane.showMessageDialog(this, "No hay seleccionado nada");
        }

    }

    private List<RenglonFacturaDTO> getRenglonesFacturaTodosConsumos(long idEstadia, FacturaDTO fdto){
        try {
            List<ConsumoDTO> lista = GestorEstadia.obtenerInstancia().getConsumosEstadia(idEstadia);
            return lista.stream().map(c -> generarRenglonDTO(c, fdto)).collect(Collectors.toList());
        } catch (IdErroneoException e) {
            return new LinkedList<>();
        } catch (HibernateException e) {

            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para obtener loc consumos de la estadía.\n\n" + e.getMessage());
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            return new LinkedList<>();
        }
    }

    private List<RenglonFacturaDTO> getRenglonesFacturaConsumosImpagos(long idEstadia, FacturaDTO fdto){
        try {
            List<ConsumoDTO> lista = GestorEstadia.obtenerInstancia().getConsumosImpagosEstadia(idEstadia);
            return lista.stream().map(c -> generarRenglonDTO(c, fdto)).collect(Collectors.toList());
        } catch (IdErroneoException e) {
            return new LinkedList<>();
        } catch (HibernateException e) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para obtener los consumos de la estadía.\n\n" + e.getMessage());
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            return new LinkedList<>();
        }
    }

    private RenglonFacturaDTO generarRenglonDTO(ConsumoDTO c, FacturaDTO fdto){

        RenglonFacturaDTO rdto = new RenglonFacturaDTO();
        rdto.setCantidad(c.getCantidad() - c.getCantidadFacturada());
        rdto.setImporteUnitario(c.getImporteUnitario());
        rdto.setNombreConsumo(c.getDetalle());
        rdto.setConsumoId(c.getId());
        rdto.setFacturaDTO(fdto);

        return rdto;

    }

    private void botonCancelar(ActionEvent e){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void botonIngresarResponsable(ActionEvent e){

        String cuit;

        cuit= (String) JOptionPane.showInputDialog(this, "Ingrese CUIT: ", "Ingresar responsable de pago", JOptionPane.PLAIN_MESSAGE, null, null,"");

        if(cuit!=null){
            try {
                responsableDTO = GestorPersona.obtenerInstancia().getResponsable(cuit);
                labelCUIT.setText(responsableDTO.getCuit());
                labelRazonSocial.setText(responsableDTO.getRazonSocial());
                panelResponsable.setBorder(new TitledBorder(new LineBorder(new Color(10, 132, 255)),"Facturar a tercero"));
                panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación "+tfNumero.getText()));
                buscarButton.setEnabled(true);
                seleccionado=RESPONSABLE;
                pasajeroDTO=null;
                tablaPasajeros.clearSelection();
            } catch (DatosErroneosResponsablePagoException ex) {
                labelCUIT.setText(cuit);
                labelRazonSocial.setText("ERROR: CUIT erróneo");
                panelResponsable.setBorder(new TitledBorder(new LineBorder(Color.RED),"Facturar a tercero"));
                panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación "+tfNumero.getText()));
                seleccionado=NADA;
                pasajeroDTO=null;
                responsableDTO=null;
                tablaPasajeros.clearSelection();
            } catch (ObjetoNoEncontradoException ex) {
                labelCUIT.setText(cuit);
                labelRazonSocial.setText("ERROR: CUIT no encontrado");
                panelResponsable.setBorder(new TitledBorder(new LineBorder(Color.RED),"Facturar a tercero"));
                panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación "+tfNumero.getText()));
                seleccionado=NADA;
                pasajeroDTO=null;
                responsableDTO=null;
                tablaPasajeros.clearSelection();
            } catch (HibernateException ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para obtener el responsable de pago.\n\n" + ex.getMessage());
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

        } else{
            labelCUIT.setText("00-00000000-0");
            labelRazonSocial.setText("ERROR: CUIT no ingresado");
            panelResponsable.setBorder(new TitledBorder(new LineBorder(Color.RED),"Facturar a tercero"));
            panelPasajero.setBorder(new TitledBorder("Ocupantes de la habitación "+tfNumero.getText()));
            seleccionado=NADA;
            pasajeroDTO=null;
            responsableDTO=null;
            tablaPasajeros.clearSelection();
        }

    }

    private boolean hayDeuda(long idEstadia){

        EstadiaDTO estadia = new EstadiaDTO();

        try {

            estadia = GestorEstadia.obtenerInstancia().getEstadia(idEstadia);

        } catch (HibernateException e) {

            JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para consultar la estadía.\n\n" + e.getMessage());
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

        }

        return estadia!=null && (estadia.getDeuda()>1f || estadia.getImporte() < 1f);

    }

    private LocalTime getHoraSalida(){

        int hora=Integer.parseInt((String) comboHora.getSelectedItem());
        int minutos=Integer.parseInt((String) comboMinutos.getSelectedItem());
        int segundos=Integer.parseInt((String) comboSegundos.getSelectedItem());

        return LocalTime.of(hora, minutos, segundos);

    }

    /*private List<RenglonFacturaDTO> getRenglonesEstadia(long idEstadia){

        GestorEstadia gestorEstadia = GestorEstadia.obtenerInstancia();
        List<ConsumoDTO> consumos;
        try {
            consumos = gestorEstadia.getConsumosEstadia(idEstadia);
        } catch (idErroneoException e) {
            consumos = new ArrayList<ConsumoDTO>();
        }

        List<RenglonFacturaDTO> ret = consumos.stream().map(c -> generarRenglonDTO(c)).toList();

        return ret;
    }*/ //A mitad me di cuenta que no debo hacer esto en esta maqueta, es responsabilidad de VentanaFacturar2


}

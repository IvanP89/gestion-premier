package org.gestionpremier.interfaces;

import org.gestionpremier.dto.EstadiaDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.negocio.entidades.TipoHabitacion;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaOcuparHabitacionMaqueta4 extends JDialog {

    private Boolean primerPasajero;
    private List<PasajeroDTO> pasajerosDTO; //En pasajerosDTO[0], es decir la primer posicion, se encuentra el responsable de la habitación.
    private EstadiaDTO estadiaDTO;
    private Integer capacidadMaxHabitacion;

    private final int CARGAR_OTRA = 0;
    private final int CANCELAR = 1;
    private final int ANTERIOR = 2;
    private final int SALIR = 3;

    private int botonPresionado = ANTERIOR;


    private JPanel panelPrincipal;
    private JPanel panelNavegacion;
    private JButton salirButton;
    private JButton anteriorButton;
    private JButton cancelarButton;
    private JPanel panelTitulo;
    private JPanel panelDeCarga;
    private JButton cargarOtraHabitaciónButton;
    private JButton seguirCargandoButton;
    private JPanel panelDeTabla;
    private JTable table1;

    public  VentanaOcuparHabitacionMaqueta4(Frame owner, boolean modal, EstadiaDTO estadiaDTO){
        super(owner, modal);
        pasajerosDTO = new ArrayList<>();
        this.setPrimerPasajero(true);
        this.setEstadiaDTO(estadiaDTO);
        this.setCapacidadMaxHabitacion();
        this.inicializarComponentes();
        this.agregarListeners();
        this.configurarVentana();
        this.seguirCargando();
        setVisible(true);
    }

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        setTitle("Ocupar habitación");
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }


    private void inicializarComponentes() {

        Border borderPanelTitulo = BorderFactory.createTitledBorder(" Ocupantes de la habitación " + this.estadiaDTO.getHabitacionDTO().getNro());
        panelTitulo.setBorder(borderPanelTitulo);

        ModeloTablaOcuparHabitacionMaqueta4 tablaOcuparHabitacion = new ModeloTablaOcuparHabitacionMaqueta4();
        table1.setModel(tablaOcuparHabitacion);
        tablaOcuparHabitacion.setListaPasajeros(this.pasajerosDTO);

    }

    private void agregarListeners() {

        seguirCargandoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { seguirCargando(); }
        });
        cargarOtraHabitaciónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {cargarOtraHabitacion();}
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {cancelar();}
        });
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {anterior();}
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {salir();}
        });

    }

    private void seguirCargando(){

        if(this.getPasajerosDTO().size() < this.getCapacidadMaxHabitacion()){

            new VentanaGestionarPasajero((Frame) getOwner(), true, this);

            ((ModeloTablaOcuparHabitacionMaqueta4) table1.getModel()).fireTableDataChanged();
        }
        else {

            JOptionPane.showMessageDialog(null, "Ha sido alcanzada la capacidad maxima de esta habitacion", "Habitación completa", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    private void cargarOtraHabitacion(){

        this.botonPresionado = this.CARGAR_OTRA;
        this.cargarPasajerosEnEstadia();
    }

    private void cancelar(){

        this.botonPresionado = this.CANCELAR;

        setVisible(false);

    }

    private void anterior(){

        this.botonPresionado = this.ANTERIOR;
       setVisible(false);

    }

    public void salir() {

        this.botonPresionado = this.SALIR;
        this.cargarPasajerosEnEstadia();
    }

    public int getBotonPresionado() {
        return botonPresionado;
    }

    public void desecharVentana() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    //GETERS Y SETERS

    public Boolean getPrimerPasajero() {
        return primerPasajero;
    }

    public void setPrimerPasajero(Boolean primerPasajero) {
        this.primerPasajero = primerPasajero;
    }

    public List<PasajeroDTO> getPasajerosDTO() {
        return pasajerosDTO;
    }

    public void addPasajeroDTO(PasajeroDTO pasajeroDTO) {
        this.pasajerosDTO.add(pasajeroDTO);
    }

    public void setPasajeroResponsableDTO(PasajeroDTO pasajeroResponsableDTO) {
        this.pasajerosDTO.add(pasajeroResponsableDTO);
        this.primerPasajero = false;
    }

    public EstadiaDTO getEstadiaDTO() {
        return estadiaDTO;
    }

    public void setEstadiaDTO(EstadiaDTO estadiaDTO) {
        this.estadiaDTO = estadiaDTO;
    }

    public Integer getCapacidadMaxHabitacion() {
        return capacidadMaxHabitacion;
    }

    public void setCapacidadMaxHabitacion() {

        if (this.getEstadiaDTO().getHabitacionDTO().getTipo() == TipoHabitacion.INDIVIDUAL_ESTANDAR){

            this.capacidadMaxHabitacion = 2; // Un ocupante + el responsable

        }

        if(this.getEstadiaDTO().getHabitacionDTO().getTipo() == TipoHabitacion.DOBLE_ESTANDAR
                || this.getEstadiaDTO().getHabitacionDTO().getTipo() == TipoHabitacion.DOBLE_SUPERIOR ||
        this.getEstadiaDTO().getHabitacionDTO().getTipo() == TipoHabitacion.SUITE_DOBLE){

            this.capacidadMaxHabitacion = 3; // Dos ocupantes + el responsable

        }

        if (this.getEstadiaDTO().getHabitacionDTO().getTipo() == TipoHabitacion.SUPERIOR_FAMILY_PLAN){

            this.capacidadMaxHabitacion = 6; // Cinco ocupantes + el responsable

        }
    }

    public void cargarPasajerosEnEstadia(){

        if(this.pasajerosDTO.size()<2){
            JOptionPane.showMessageDialog(null, "Para crear la estadía se debe añadir al menos un ocupante en la habitación", "Habitación vacía", JOptionPane.INFORMATION_MESSAGE);
        }
        else {

            this.estadiaDTO.setPasajeroResponsable(this.pasajerosDTO.get(0));

            //Copio la lista de todos los pasajeros de la habitacion (pasajerosDTO), en una nueva lista llamada ocupantes,
            // quitandole el responsable de la habitacion, que es el primer pasajero de la lista pasajerosDTO.

            List<PasajeroDTO> ocupantes = new ArrayList<>();

            for (int i = 1; i < this.pasajerosDTO.size(); i++) {
                ocupantes.add(pasajerosDTO.get(i));
            }

            this.estadiaDTO.setPasajeros(ocupantes);

            setVisible(false);
        }
    }
}

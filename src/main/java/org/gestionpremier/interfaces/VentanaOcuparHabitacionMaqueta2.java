package org.gestionpremier.interfaces;

import org.gestionpremier.dto.ReservaDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;

public class VentanaOcuparHabitacionMaqueta2 extends JDialog {

    private ReservaDTO reservaDTO;
    private Boolean ocuparIgual;

    private JPanel panelPrincipal;
    private JPanel panelTitulo;
    private JPanel panelNavegacion;
    private JPanel panelCentral;
    private JButton ocuparIgualButton;
    private JButton volverButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    public VentanaOcuparHabitacionMaqueta2(Frame owner, boolean modal, ReservaDTO reservaDTO){
        super(owner, modal);
        this.reservaDTO = reservaDTO;
        this.inicializarComponentes();
        this.agregarListeners();
        this.configurarVentana();
    }

    private void inicializarComponentes() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/LL/yyyy 'a las:' HH:mm:ss 'hs' ");

        textField1.setText(reservaDTO.getFechaDesde().format(formatter));
        textField2.setText(reservaDTO.getTitular().getApellido() + " " + reservaDTO.getTitular().getNombre());
        textField3.setText(reservaDTO.getHabitacion().getNroHab());
        textField4.setText(reservaDTO.getFechaHasta().format(formatter));

        ocuparIgual = false;


    }

    private void configurarVentana(){

        setContentPane(panelPrincipal);
        setTitle("Confirmación");
        setMinimumSize(new Dimension(630, 360));
        setResizable(false);
        //pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void agregarListeners(){

        ocuparIgualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ocuparIgual = true;
                setVisible(false);
            }
        });

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    public Boolean ocuparIgual(){

        if(ocuparIgual){
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        else{
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            return false;
        }

    }

}

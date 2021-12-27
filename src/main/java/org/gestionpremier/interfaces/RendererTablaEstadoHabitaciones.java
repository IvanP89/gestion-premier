package org.gestionpremier.interfaces;

import org.gestionpremier.negocio.entidades.EstadoHabitacion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RendererTablaEstadoHabitaciones  extends DefaultTableCellRenderer {

    private final Color COLOR_HABITACION_LIBRE = new Color(50, 255, 0);
    private final Color COLOR_HABITACION_RESERVADA = new Color(255, 155, 0);
    private final Color COLOR_HABITACION_OCUPADA = new Color(148, 87, 235);
    private final Color COLOR_HABITACION_MANTENIMIENTO = new Color(255, 255, 0);
    private final Color COLOR_CELDA_SELECCIONADA = Color.BLUE;
    private final Color COLOR_CELDA_FECHA = Color.WHITE;
    private final Color COLOR_FUENTE_FECHA = Color.BLACK;

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object value, boolean isSelected, boolean hasFocus, int fila, int columna) {

        Component celda = super.getTableCellRendererComponent(tabla, value, isSelected, hasFocus, fila, columna);

        EstadoHabitacion estadoHabitacion;

        if (columna != 0 && !isSelected ) {

            estadoHabitacion = ((ModeloTablaEstadoHabitaciones) tabla.getModel()).getEstadoCelda(fila, columna);

            switch(estadoHabitacion) {

                case OCUPADA:
                    celda.setBackground(COLOR_HABITACION_OCUPADA);
                    break;

                case RESERVADA:
                    celda.setBackground(COLOR_HABITACION_RESERVADA);
                    break;

                case EN_MANTENIMIENTO:
                    celda.setBackground(COLOR_HABITACION_MANTENIMIENTO);
                    break;

                default:
                    celda.setBackground(COLOR_HABITACION_LIBRE);

            }

        } else if (columna == 0) {

            celda.setBackground(COLOR_CELDA_FECHA);
            celda.setForeground(COLOR_FUENTE_FECHA);

        } else {

            celda.setBackground(COLOR_CELDA_SELECCIONADA);

        }

        ((JComponent) celda).setBorder(BorderFactory.createLineBorder(Color.GRAY));

        return celda;

    }

}

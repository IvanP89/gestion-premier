package org.gestionpremier.interfaces;

import org.gestionpremier.negocio.entidades.EstadoHabitacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TablaOcuparHabitaciones extends JTable {

    static final long serialVersionUID = 1L;

    private final VentanaEstadoHabitaciones ventanaPadre;

    List<Point> selecciones = new ArrayList<Point>();

    boolean laUltimaSeleccionAbrioUnIntervalo = false;
    int ultimaFilaSeleccionada = 0;
    int ultimaColumnaSeleccionada = 0;

    public TablaOcuparHabitaciones(VentanaEstadoHabitaciones ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
    }

    @Override protected void processMouseEvent(MouseEvent e) {

        if(e.getID() != MouseEvent.MOUSE_PRESSED) {
            return;
        }

        int fila = ((JTable)e.getSource()).rowAtPoint(e.getPoint());
        int columna = ((JTable)e.getSource()).columnAtPoint(e.getPoint());
        final boolean INDICE_FILA_MINIMO_VALIDO = fila >= 0;
        final boolean INDICE_COLUMNA_MINIMO_VALIDO = columna >= 1;
        final boolean ABRE_INTERVALO_DESDE_FECHA_ACTUAL = !laUltimaSeleccionAbrioUnIntervalo && fila == 0;
        final boolean CIERRA_INTERVALO_EN_RANGO_VALIDO = (laUltimaSeleccionAbrioUnIntervalo && fila >= 0);

        if (
                   INDICE_FILA_MINIMO_VALIDO
                && INDICE_COLUMNA_MINIMO_VALIDO
                && (ABRE_INTERVALO_DESDE_FECHA_ACTUAL || CIERRA_INTERVALO_EN_RANGO_VALIDO)
        ) {

            EstadoHabitacion estadoHabitacion = ((ModeloTablaEstadoHabitaciones) this.getModel()).getEstadoCelda(fila, columna);

            if (estadoHabitacion != EstadoHabitacion.OCUPADA
                && estadoHabitacion != EstadoHabitacion.EN_MANTENIMIENTO) {

                Point p = new Point(fila, columna);

                if (laUltimaSeleccionAbrioUnIntervalo) {

                    if (ultimaColumnaSeleccionada == columna && ultimaFilaSeleccionada < fila) {

                        for (int i = ultimaFilaSeleccionada + 1; i <= fila; i++) {

                            estadoHabitacion = ((ModeloTablaEstadoHabitaciones) this.getModel()).getEstadoCelda(i, columna);

                            if (estadoHabitacion != EstadoHabitacion.OCUPADA && estadoHabitacion != EstadoHabitacion.EN_MANTENIMIENTO) {

                                p = new Point(i, columna);
                                selecciones.add(p);

                            } else {

                                selecciones.clear();
                                laUltimaSeleccionAbrioUnIntervalo = false;
                                break;

                            }

                        }

                        laUltimaSeleccionAbrioUnIntervalo = false;

                    } else if (ultimaColumnaSeleccionada == columna && ultimaFilaSeleccionada == fila) {

                        selecciones.remove(p);
                        laUltimaSeleccionAbrioUnIntervalo = false;

                    } else  if (ultimaColumnaSeleccionada != columna && fila == 0) {

                        selecciones.clear();
                        selecciones.add(p);
                        laUltimaSeleccionAbrioUnIntervalo = true;

                    } else {

                        selecciones.clear();
                        laUltimaSeleccionAbrioUnIntervalo = false;

                    }

                } else { // LA SELECCIÓN ANTERIOR HABÍA CERRADO UN INTERVALO

                    selecciones.clear();

                    if (fila == 0) {

                        selecciones.add(p);
                        laUltimaSeleccionAbrioUnIntervalo = true;

                    }

                }

                ultimaFilaSeleccionada = fila;
                ultimaColumnaSeleccionada = columna;

                if (selecciones.isEmpty()) {
                    ventanaPadre.habilitarBtnAplicar(false);
                } else {
                    ventanaPadre.habilitarBtnAplicar(true);
                }

            } else {

                selecciones.clear();
                ventanaPadre.habilitarBtnAplicar(false);
                laUltimaSeleccionAbrioUnIntervalo = false;
                ultimaFilaSeleccionada = fila;
                ultimaColumnaSeleccionada = columna;

            }

        } else {

            selecciones.clear();
            ventanaPadre.habilitarBtnAplicar(false);
            laUltimaSeleccionAbrioUnIntervalo = false;
            ultimaFilaSeleccionada = fila;
            ultimaColumnaSeleccionada = columna;

        }

//                ((JTable)e.getSource()).repaint();
        repaint();

    }

    @Override public boolean isCellSelected(int arg0, int arg1) {

        return selecciones.contains(new Point(arg0, arg1));

    }

    public boolean hayRangoSeleccionado() {

        return !selecciones.isEmpty();

    }

    public List<Point> getSelecciones() {
        return selecciones;
    }

    public int getCantDiasSeleccionados() {

        return selecciones.size();

    }

    public LocalDate getFechaDesdeSeleccionada() {

        ModeloTablaEstadoHabitaciones modeloTabla = (ModeloTablaEstadoHabitaciones) this.getModel();

        return (LocalDate) modeloTabla.getValueAt((int) selecciones.get(0).getX(), 0);

    }

    public LocalDate getFechaHastaSeleccionada() {

        ModeloTablaEstadoHabitaciones modeloTabla = (ModeloTablaEstadoHabitaciones) this.getModel();

        return (LocalDate) modeloTabla.getValueAt((int) selecciones.get(selecciones.size()-1).getX(), 0);

    }

    public long getIdHabitacionSeleccionada() {

        ModeloTablaEstadoHabitaciones modeloTabla = (ModeloTablaEstadoHabitaciones) this.getModel();

        return modeloTabla.getIdHabitacion((int) selecciones.get(0).getY());

    }

    public void clearSelecciones() {

        selecciones.clear();
        laUltimaSeleccionAbrioUnIntervalo = false;
        ((ModeloTablaEstadoHabitaciones) getModel()).fireTableDataChanged();

    }

}

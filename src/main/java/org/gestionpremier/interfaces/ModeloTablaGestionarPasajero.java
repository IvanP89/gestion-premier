package org.gestionpremier.interfaces;

import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.utilidades.ListaPaginada;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaGestionarPasajero extends AbstractTableModel {

    ListaPaginada<PasajeroDTO> listaPasajeros;
    List<String> columnas;

    public ModeloTablaGestionarPasajero() {
        listaPasajeros = new ListaPaginada<>();
        columnas = new ArrayList<>();
        columnas.add("Apellido");
        columnas.add("Nombres");
        columnas.add("Tipo de documento");
        columnas.add("N° de documento");
    }

    public ModeloTablaGestionarPasajero(ListaPaginada<PasajeroDTO> listaPasajeros, List<String> columnas) {

        if (listaPasajeros != null) {
            this.listaPasajeros = listaPasajeros;
        } else {
            listaPasajeros = new ListaPaginada<>();
        }

        if (columnas != null) {
            this.columnas = columnas;
        } else {
            columnas = new ArrayList<>();
            columnas.add("Apellido");
            columnas.add("Nombres");
            columnas.add("Tipo de documento");
            columnas.add("N° de documento");
        }

    }

    public void addColumna(String nombre) {
        if (nombre != null) {
            columnas.add(nombre);
        }
    }

    public ListaPaginada<PasajeroDTO> getListaPasajeros() {
        return listaPasajeros;
    }

    public void setListaPasajeros(ListaPaginada<PasajeroDTO> listaPasajeros) {

        this.listaPasajeros = listaPasajeros;

        fireTableDataChanged();

    }

    public List<String> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<String> columnas) {
        this.columnas = columnas;
    }

    public PasajeroDTO getPasajero(int fila) {

        return listaPasajeros.get(fila);

    }

    @Override
    public int getRowCount() {
        return listaPasajeros.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.size();
    }

    @Override
    public Object getValueAt(int fila, int columna) {

        switch (columna) {
            case 0:
                return listaPasajeros.get(fila).getApellido();
            case 1:
                return listaPasajeros.get(fila).getNombre();
            case 2:
                return listaPasajeros.get(fila).getTipoDoc().toString();
            case 3:
                return listaPasajeros.get(fila).getNroDoc();
            default:
                return null;
        }

    }

    @Override
    public String getColumnName(int columna) {

        if (columna<columnas.size()) {

            return columnas.get(columna);

        } else {

            return "";

        }

    }


}

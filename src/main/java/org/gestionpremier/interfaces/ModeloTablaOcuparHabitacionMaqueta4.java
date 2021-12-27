package org.gestionpremier.interfaces;

import org.gestionpremier.dto.PasajeroDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaOcuparHabitacionMaqueta4 extends AbstractTableModel {

    private final String[] COLUMNAS = {"APELLIDO","NOMBRES","DOCUMENTO","RESPONSABLE"};
    private final String SI = "SI";
    private final String NO = "NO";
    private List<PasajeroDTO> pasajerosOcupantes;

    public ModeloTablaOcuparHabitacionMaqueta4() {

        pasajerosOcupantes = new ArrayList<>();

    }

    public void setListaPasajeros(List<PasajeroDTO> listaPasajeros) {

        this.pasajerosOcupantes = listaPasajeros;

        fireTableDataChanged();

    }

    @Override
    public int getRowCount() {
        return pasajerosOcupantes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 : return pasajerosOcupantes.get(rowIndex).getApellido();
            case 1 : return pasajerosOcupantes.get(rowIndex).getNombre();
            case 2 : return pasajerosOcupantes.get(rowIndex).getNroDoc();
            case 3 : if(rowIndex==0){return this.SI;} else{return this.NO;}
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return COLUMNAS[0];
            case 1: return COLUMNAS[1];
            case 2: return COLUMNAS[2];
            case 3: return COLUMNAS[3];
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }



}

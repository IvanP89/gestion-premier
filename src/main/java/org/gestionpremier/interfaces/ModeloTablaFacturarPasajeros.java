package org.gestionpremier.interfaces;

import org.gestionpremier.dto.PasajeroDTO;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModeloTablaFacturarPasajeros extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Nombre y apellido", "DNI"};

    private ModeloTablaFacturarPasajeros(){/*deshabilitado*/}

    private List<PasajeroDTO> pasajeros;


    public ModeloTablaFacturarPasajeros(List<PasajeroDTO> pasajeros){
        this.pasajeros=pasajeros;
    }

    public PasajeroDTO getPasajero(int indice){
        return pasajeros.get(indice);
    }

    @Override
    public int getRowCount() {
        return pasajeros.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PasajeroDTO selected= pasajeros.get(rowIndex);
        switch (columnIndex){
            case 0:
                return selected.getNombre()+" "+selected.getApellido();
            case 1:
                return selected.getNroDoc();
            default:
                return "ERROR";
        }
    }



    @Override
    public String getColumnName(int columnIndex){
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int ri, int ci){
        return false;
    }

}

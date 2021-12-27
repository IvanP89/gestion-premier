package org.gestionpremier.interfaces;

import org.gestionpremier.dto.FacturaDTO;
import org.gestionpremier.dto.RenglonFacturaDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de datos que usa la tabla de VentanaFacturar2.
 * Le informa a la tabla la cantidad de filas, de columnas, cuáles son los encabezados de las columnas y qué datos van
 * a mostrarse en cada celda.
 */

public class ModeloTablaFacturar2 extends AbstractTableModel {

    //********************************************  ATRIBUTOS  ******************************************************

    private final int INDICE_COL_NOMBRE = 0;
    private final int INDICE_COL_PRECIO = 1;
    private final int INDICE_COL_CANT_POR_PAGAR = 2;
    private final int INDICE_COL_CANT_A_PAGAR = 3;
    private final int INDICE_COL_SUBTOTAL = 4;

    /**
     * La factura es la fuente de datos de la tabla, cada objeto RenglonFactura de la misma se verá como una fila.
     */
    private FacturaDTO facturaDTO;

    /**
     * Indica si entre los renglones de facturaDTO se encuentra el correspondiente al consumo del hospedaje.
     */
    private boolean hayEstadia = false;

    /**
     * Los encabezados de las columas.
     * Su tamaño es la cantidad de columnas de la tabla.
     */
    private List<String> columnas;

    /**
     * Los datos de la columna que muestra la cantidad que el usuario selecciona para facturar de un determinado
     * consumo.
     * Los datos de esta columna son los únicos que existen de forma separada en una fuente distinta a
     * FacturaDTO, luego se usarán para reemplazar a los atributos de cantidad de consumos a pagar de cada
     * renglón de la factura.
     */
    private List<Integer> cantConsumosAPagar;

    //******************************************  CONSTRUCTORES  ****************************************************

    public ModeloTablaFacturar2(FacturaDTO facturaDTO) {

        this.facturaDTO = facturaDTO;
        columnas = new ArrayList<>();
        cantConsumosAPagar = new ArrayList<>();

        int i = 0;
        RenglonFacturaDTO renglonActual;

        while (i < facturaDTO.getRenglones().size()) {

            renglonActual = facturaDTO.getRenglones().get(i);

            if (renglonActual.getNombreConsumo().toLowerCase().contains("estadía") || renglonActual.getNombreConsumo().toLowerCase().contains("estadia")) {

                this.hayEstadia = true;

                facturaDTO.getRenglones().remove(renglonActual);
                facturaDTO.getRenglones().add(0, renglonActual);
                cantConsumosAPagar.add(0, 0);

            } else {

                cantConsumosAPagar.add(renglonActual.getCantidad());

            }

            i++;

        }

        columnas.add("Item");
        columnas.add("Precio unit.");
        columnas.add("Consumidos");
        columnas.add("A pagar");
        columnas.add("Subtotal");

    }

    //****************************************  MÉTODOS DE LÓGICA  **************************************************

    /**
     * Indica si entre los renglones de facturaDTO se encuentra el correspondiente al consumo del hospedaje.
     *
     * @return  <code>true</code> si el consumo del hospedaje está disponible para ser facturado con la factura actual,
     *          <code>false</code> en el caso contrario.
     */
    public boolean hayEstadia() {
        return hayEstadia;
    }

    public void setHayEstadia(boolean hayEstadia) {
        this.hayEstadia = hayEstadia;
    }

    /**
     * Obtiene el valor del consumo del hospedaje.
     *
     * @return el valor del hospedaje (sin IVA), o cero si la factura no contiene el consumo del hospedaje.
     */
    public float getValorEstadia () {

        if (this.hayEstadia) {

            return facturaDTO.getRenglones().get(0).getImporteUnitario();

        }

        return 0;

    }

    /**
     * Marca o desmarca el consumo del hospedaje para ser facturado.
     *
     * @param pagarEstadia  <code>true</code> para indicar que el consumo del hospedaje, de estar disponible, va a ser
     *                      facturado en la factura actual, <code>false</code> en el caso contrario.
     */
    public void pagarEstadiaSeleccionado(boolean pagarEstadia) {

        if (hayEstadia) {

            if (pagarEstadia) {

                cantConsumosAPagar.set(0, 1);

            } else {

                cantConsumosAPagar.set(0, 0);

            }

        }

    }

    public List<Integer> getCantConsumosAPagar() {
        return cantConsumosAPagar;
    }

    //*********************************  MÉTODOS QUE USA EL MOTOR DE LA TABLA  **************************************

    /**
     * Método utilizado por el motor de la tabla para saber cuántas filas de datos hay en el modelo.
     *
     * @return  la cantidad defilas en el modelo de datos.
     */
    @Override
    public int getRowCount() {
        try {

            if (this.hayEstadia) {

                return facturaDTO.getRenglones().size() - 1;

            } else {

                return facturaDTO.getRenglones().size();

            }

        } catch (NullPointerException e) {

            return 0;

        }
    }

    /**
     * Método utilizado por el motor de la tabla para saber cuántas columnas hay en el modelo.
     *
     * @return  la cantidad de columnas en el modelo de datos.
     */
    @Override
    public int getColumnCount() {

        try {
            return this.columnas.size();
        } catch (NullPointerException e) {
            return 0;
        }

    }

    /**
     * Método utilizado por el motor de la tabla para saber qué valores mostrar en cada celda.
     * Para mostrar los datos en la tabla, primero obtiene la cantidad de filas y columnas mediante los dos métodos
     * anteriores y después recorre las celdas llamando a este método para mostrar los datos en cada una.
     *
     * @param fila      el índice de fila de una celda.
     * @param columna   el índice de columna de una celda.
     * @return          el valor que le corresponde a una celda específica en la tabla.
     */
    @Override
    public Object getValueAt(int fila, int columna) {

        if (this.hayEstadia) {
            fila = fila + 1;
        }

        float iva = 1;

        if (facturaDTO.getTipo() == 'B') {
            iva = 1 + facturaDTO.getIva();
        }

        switch (columna) {

            case INDICE_COL_NOMBRE:
                return facturaDTO.getRenglones().get(fila).getNombreConsumo();

            case INDICE_COL_PRECIO:
                return "$" + String.format("%.2f", facturaDTO.getRenglones().get(fila).getImporteUnitario() * iva);

            case INDICE_COL_CANT_POR_PAGAR:
                return facturaDTO.getRenglones().get(fila).getCantidad();

            case INDICE_COL_CANT_A_PAGAR:
                return cantConsumosAPagar.get(fila);

            case INDICE_COL_SUBTOTAL:
                return "$" + String.format("%.2f", ((float) cantConsumosAPagar.get(fila)) * facturaDTO.getRenglones().get(fila).getImporteUnitario() * iva);

        }

        return "COLUMNA NO VALIDA";

    }

    /**
     * Método utilizado por el motor de la tabla para obtener los encabezados que tiene que mostrar en las columnas.
     * El motor de la tabla primero obtiene la cantidad de columnas llamando a getColumnCount(), y luego las recorre
     * mientras va llamando a este método para obtener y mostrar los encabezados.
     *
     * @param columna   el índice de una columna.
     * @return          el encabezado de una columna.
     */
    @Override
    public String getColumnName(int columna) {

        return this.columnas.get(columna);

    }

    /**
     * Método utilizado por el motor de la tabla para saber si una celda específica debe habilitarse como editable
     * por parte del usuario.
     *
     * @param fila      el índice de fila de una celda.
     * @param columna   el índice de columna de una celda.
     * @return          <code>true</code> si la celda en los índices especificados debe ser editable, <code>false</code>
     *                  en el caso contrario.
     */
    @Override
    public boolean isCellEditable(int fila, int columna) {

        if (columna == INDICE_COL_CANT_A_PAGAR) {
            return true;
        }

        return false;

    }

    /**
     * Método utilizado por el motor de la tabla para insertar datos en una celda editable.
     * En realidad lo que hace es insertar datos en la fuente de datos del modelo, en el lugar correspondiente a
     * una celda en la tabla, luego refresca los datos de la tabla y el valor de la celda se ve cambiado.
     *
     * @param valor     el valor a insertar.
     * @param fila      el índice de la fila para la celda donde insertar el nuevo valor.
     * @param columna   el índice de la columna para la celda donde insertar el nuevo valor.
     */
    @Override
    public void setValueAt(Object valor, int fila, int columna) {

        if (this.hayEstadia) {
            fila = fila + 1;
        }

        if (columna == INDICE_COL_CANT_A_PAGAR) {

            cantConsumosAPagar.set(fila, Integer.parseInt((String) valor));

            fireTableCellUpdated(fila, columna);

        }

    }

}

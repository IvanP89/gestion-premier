package org.gestionpremier.interfaces;

import org.gestionpremier.dto.EstadoHabitacionDTO;
import org.gestionpremier.dto.EstadoHabitacionDiaDTO;
import org.gestionpremier.negocio.entidades.TipoHabitacion;
import org.gestionpremier.negocio.entidades.EstadoHabitacion;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Modelo de datos que usan ambas tablas de VentanaEstadoHabitaciones.
 * Le informa a la tabla la cantidad de filas, de columnas, cuáles son los encabezados de las columnas y qué datos van
 * a mostrarse en cada celda.
 */

public class ModeloTablaEstadoHabitaciones  extends AbstractTableModel {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Cada objeto de esta lista (que contienen a su vez otra lista) constituye todas las celdas de una columna en la
     * tabla, representando a una habitación individual.
     */
    private List<EstadoHabitacionDTO> listaEstadosHabitacion;

    /**
     * Los encabezados de las columas.
     * Su tamaño es la cantidad de columnas de la tabla.
     */
    private List<String> columnas;

    //******************************************  CONSTRUCTORES  ****************************************************

    public ModeloTablaEstadoHabitaciones() {

//        this.setListaEstadosHabitacion(null);

    }

    public ModeloTablaEstadoHabitaciones(List<EstadoHabitacionDTO> listaEstadosHabitacion, int cantDiasAMostrar) {

        this.setListaEstadosHabitacion(listaEstadosHabitacion);

    }

    //****************************************  MÉTODOS DE LÓGICA  **************************************************

    public void setListaEstadosHabitacion(List<EstadoHabitacionDTO> listaEstadosHabitacion) {

        this.listaEstadosHabitacion = listaEstadosHabitacion;

        if (this.listaEstadosHabitacion == null) {

            this.listaEstadosHabitacion = new ArrayList<>();

        }

        this.ordenarHabitaciones();

        this.setColumnas();

        // Le avisa a la tabla que hubo un cambio en los datos, así que tiene que refrescarlos.
        this.fireTableDataChanged();

        // Le avisa a la tabla que hubo un cambio en la estructura de la misma, por ej en la cantidad de columnas,
        // para que actualice sus dimensiones y refresque los datos.
        this.fireTableStructureChanged();

    }

    public List<EstadoHabitacionDTO> getListaEstadosHabitacion() {
        return listaEstadosHabitacion;
    }

    /**
     * Ordena las habitaciones primero por tipo y luego por número.
     */
    private void ordenarHabitaciones() {

        if (listaEstadosHabitacion.size() > 1) {

            Comparator<EstadoHabitacionDTO> comparadorHabitaciones = (hab1, hab2) -> hab1.getTipoHabitacion().compareTo(hab2.getTipoHabitacion());
            comparadorHabitaciones = comparadorHabitaciones.thenComparing((hab1, hab2) -> hab1.getNroHab().compareTo(hab2.getNroHab()));

            listaEstadosHabitacion.sort(comparadorHabitaciones);

        }

    }

    /**
     * Llena con datos la lista que es usada para indicar los encabezados de las columnas y la cantidad de las mismas.
     */
    private void setColumnas() {

        columnas = new ArrayList<>();

        if (listaEstadosHabitacion.size() != 0) {
            columnas.add("DÍA");
        }

        for (EstadoHabitacionDTO e :
                listaEstadosHabitacion) {

            columnas.add("Hab. " + e.getNroHab() + " (" + abreviarTipo(e.getTipoHabitacion()) + ")");

        }

    }

    public List<String> getColumnas() {
        return columnas;
    }

    /**
     * Indica el estado que tiene una Habitacion en una celda determinada.
     *
     * @param fila      el índice de fila de la tabla, semánticamente la fecha en la que buscar el estado.
     * @param columna   el íncide de columna en la tabla, semánticamente la identificación de una Habitacion en particular.
     * @return          el estado de una celda, semánticamente el estado de una Habitacion en un día determinado.
     */
    public EstadoHabitacion getEstadoCelda(int fila, int columna) {

        if (fila >= 0 && columna >=0 && fila < this.getRowCount() && columna < getColumnCount()) {

            return listaEstadosHabitacion.get(columna-1).getEstadosHabitacionXDia().get(fila).getEstado();

        } else {

            return null;

        }

    }

    /**
     * Abrevia el nombre del tipo de la habitación para ser utilizado en el encabezado de las columnas.
     *
     * @param tipo  el tipo de Habitacion.
     * @return      el nombre del tipo abreviado, o la cadena vacía si el tipo recibido es desconocido.
     */
    private String abreviarTipo(TipoHabitacion tipo) {

        switch (tipo) {

            case SUPERIOR_FAMILY_PLAN:
                return "S.F.P.";

            case INDIVIDUAL_ESTANDAR:
                return "I.E.";

            case DOBLE_SUPERIOR:
                return "D.S.";

            case DOBLE_ESTANDAR:
                return "D.E.";

            case SUITE_DOBLE:
                return "S.D.";

            default:
                return "";

        }

    }

    /**
     * Retorna el objeto entero contenido en una celda, que representa el estado de una Habitacion en un día específico.
     *
     * @param fila      el índice de fila de la tabla, semánticamente la fecha en la que buscar el estado.
     * @param columna   el íncide de columna en la tabla, semánticamente la identificación de una Habitacion en particular.
     * @return          el objeto contenido en una celda.
     */
    public EstadoHabitacionDiaDTO getEstadoHabitacionDia(int fila, int columna) {

        if (fila >= 0 && columna > 0 && fila < this.getRowCount() && columna <= getColumnCount()) {

            return listaEstadosHabitacion.get(columna-1).getEstadosHabitacionXDia().get(fila);

        } else {

            return null;

        }

    }

    public long getIdHabitacion(int columna) {

        if (columna > 0) {

            return listaEstadosHabitacion.get(columna-1).getIdHab();

        }

        return -1;

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

            return listaEstadosHabitacion.get(0).getEstadosHabitacionXDia().size();

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

            return columnas.size();

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

        if (columna != 0) {
            return "";
        }

        return listaEstadosHabitacion.get(0).getEstadosHabitacionXDia().get(fila).getFecha().toLocalDate();

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

        if (columna < columnas.size()) {
            return columnas.get(columna);
        }

        return "Columna out of range.";

    }

}

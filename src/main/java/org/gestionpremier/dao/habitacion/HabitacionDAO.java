package org.gestionpremier.dao.habitacion;

import org.gestionpremier.negocio.entidades.Habitacion;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Habitacion.
 */

public interface HabitacionDAO {

    /**
     * Busca todas las Habitaciones existentes en el sistema de persistencia de datos.
     *
     * @return  todas las Habitaciones existentes.
     */
    List<Habitacion> getHabitaciones();

    /**
     * Busca una Habitacion en el sistema de persistencia de datos.
     * El criterio de búsqueda es por coincidencia completa, por lo que solamente se busca una única habitación. Retorna
     * una lista por no ser el criterio de búsqueda el id.
     *
     * @param nroHabitacion     el número de la Habitacion a buscar. El número es un atributo distinto al id.
     * @return                  las Habitaciones encontradas, o una lista vacía si la búsqueda no arrojó resultados.
     */
    List<Habitacion> getHabitacion(String nroHabitacion);

    /**
     * Busca una habitación en el sistema de persistencia de datos.
     *
     * @param idHabitacion  el id de la Habitacion buscada.
     * @return              la Habitacion buscada, o <code>null</code> si la búsqueda no arrojó resultados.
     */
    Habitacion getHabitacion(long idHabitacion);

    /**
     * Actualiza los datos de una Habitacion en el sistema de persistencia de datos.
     *
     * @param habitacion    la Habitacion a actualizar.
     * @return              el id de la Habitacion.
     */
    long actualizarHabitacion(Habitacion habitacion);

}

package org.gestionpremier.dao.reserva;

import org.gestionpremier.negocio.entidades.Reserva;
import org.gestionpremier.negocio.entidades.TitularReserva;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de los Data Access Object de las clases entidad Reserva y TitularReserva.
 */

public interface ReservaDAO {

    /**
     * Inserta un nuevo TitularReserva en la base de datos, así como también sus Reservas asociadas.
     *
     * @param titularReserva    el nuevo titular a insertar.
     * @return                  el id generado para el titular insertado.
     */
    long insertarTitular(TitularReserva titularReserva);

    /**
     * Busca todas las Reservas correspondientes a un período de tiempo.
     * El criterio de búsqueda es la existencia de una superposición en cualquier punto del rango de fechas recibido
     * y el rango de fechas de las reservas.
     *
     * @param fechaDesde    la fecha de inicio del rango en el que buscar.
     * @param fechaHasta    la fecha final del rango en el que buscar.
     * @return              todas las Reservas que se superponen en algun punto con el rango de fecha recibido, o una
     *                      lista vacía si la búsqueda no arroja resultados.
     */
    List<Reserva> getReservas(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    /**
     * Busca una Reserva en el sistema de persistencia de datos.
     *
     * @param idReserva el id de la Reserva buscada.
     * @return          la Reserva buscada, o <code>null</code> si la búsqueda no arroja resultados.
     */
    Reserva getReserva(long idReserva);

}

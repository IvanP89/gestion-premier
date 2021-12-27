package org.gestionpremier.dao.estadia;

import org.gestionpremier.negocio.entidades.Estadia;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Estadia.
 */

public interface EstadiaDAO {

    /**
     * Busca la Estadia más reciente de una Habitación que tenga Consumos sin facturar.
     *
     * @param idHabitacion  el id de la Habitacion para la cual se desea buscar la Estadia.
     * @return              la Estadia más reciente que tenga Consumos sin facturar asociada a la Habitacion. El
     *                      criterio para identificar una Estadia "que tenga Consumos sin facturar" es que
     *                      Estadia.deuda sea distinto de cero (hay consumos sin facturar) OR que Estadia.importe sea
     *                      igual a cero (la Estadia no está finalizada y entonces el importe no está seteado).
     *                      Si la búsqueda no arroja resultados, retorna <code>null</code>.
     */
    Estadia getUltimaEstadiaImpagaYaEmpezada(long idHabitacion);

    /**
     * Busca en la base de datos las Estadias cuyas fechas se superpongan con el rango de fechas especificado.
     * El criterio es que las fechas de las estadías se superpongan en cualquier punto con el rango buscado.
     *
     * @param fechaDesde    la fecha inicial del rango en el que buscar las Estadias.
     * @param fechaHasta    la fecha final del rangoa en el que buscar las Estadias.
     * @return              las Estadias resultantes. Si la búsqueda no arroja resultados, se retorna una lista vacía.
     */
    List<Estadia> getEstadias(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    /**
     * Busca una Estadia en el sistema de persistencia de datos.
     *
     * @param idEstadia     el id de la Estadia buscada.
     * @return              una instancia de la Estadia buscada, o <code>null</code> si la búsqueda no arrojó resultados.
     */
    Estadia getEstadia(long idEstadia);

    /**
     * Actualiza los datos de una Estadia en el sistema de persistencia de datos.
     *
     * @param estadia   la Estadia a actualizar.
     * @return          el id de la Estadia.
     */
    long actualizarEstadia(Estadia estadia);

    /**
     * Inserta una nueva Estadia en el sistema de persistencia de datos.
     *
     * @param estadia   la Estadia a insertar.
     * @return          el id generado para la Estadia.
     */
    long insertarEstadia(Estadia estadia);

}

package org.gestionpremier.dao.responsable;

import org.gestionpremier.negocio.entidades.ResponsableDePago;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad ResponsableDePago.
 */

public interface ResponsablePagoDAO {

    /**
     * Busca un responsable de pago en el sistema de persistencia de datos.
     *
     * @param idResponsable el id del responsable de pago buscado.
     * @return              una instancia de la entidad del responsable de pago encontrado, o <code>null</code> si la
     *                      búsqueda no arrojó resultados.
     */
    ResponsableDePago getResponsable(long idResponsable);

    /**
     * Busca un responsable de pago en el sistema de persistencia de datos.
     * Busca un único responsable, el retorno es una lista como precaución por no ser el criterio de búsqueda el id.
     *
     * @param cuit  el cuit del responsable de pago, usado como criterio de búsqueda para encontrarlo.
     * @return      una instancia de la entidad del responsable de pago encontrado, o <code>null</code> si la
     *              búsqueda no arrojó resultados.
     */
    List<ResponsableDePago> getResponsables(String cuit);

}

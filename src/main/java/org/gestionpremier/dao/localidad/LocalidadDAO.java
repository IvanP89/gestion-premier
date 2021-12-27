package org.gestionpremier.dao.localidad;

import org.gestionpremier.negocio.entidades.Localidad;
import org.gestionpremier.negocio.entidades.Provincia;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Localidad.
 */

public interface LocalidadDAO {

    /**
     * Busca una Localidad en el sistema de persistencia de datos.
     *
     * @param idLocalidad   el id de la Localidad buscada.
     * @return              una instancia de la Localidad buscada, o <code>null</code> si la búsqueda no arroja
     *                      resultados.
     */
    Localidad getLocalidad(long idLocalidad);

    /**
     * Busca todas las localidades asociadas a una Provincia en el sistema de persistencia de datos.
     *
     * @param provincia la Provincia de la cual desean obtenerse las Localidades.
     * @return          las Localidades asociadas a la Provincia indicada, o una lista vacía si la búsqueda no arroja
     *                  resultados.
     */
    List<Localidad> getLocalidades(Provincia provincia);

}

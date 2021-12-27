package org.gestionpremier.dao.provincia;

import org.gestionpremier.negocio.entidades.Pais;
import org.gestionpremier.negocio.entidades.Provincia;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Provincia.
 */

public interface ProvinciaDAO {

    /**
     * Busca todas las Provincias asociadas a un Pais en el sistema de persistencia de datos.
     *
     * @param pais  el Pais del cual se desea obtener las Provincias.
     * @return      todas las Provincias asociadas al Pais recibido, o una lista vacía si la búsqueda no arroja
     *              resultados.
     */
    List<Provincia> getProvincias(Pais pais);

}

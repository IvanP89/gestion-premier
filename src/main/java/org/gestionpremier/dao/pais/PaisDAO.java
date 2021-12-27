package org.gestionpremier.dao.pais;

import org.gestionpremier.negocio.entidades.Pais;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Pais.
 */

public interface PaisDAO {

    /**
     * Busca todos los Paises existentes en el sistema de persistencia de datos.
     *
     * @return  todos los Paises encontrados, o una lista vacía si la búsqueda no arroja resultados.
     */
    List<Pais> getPaises();

}

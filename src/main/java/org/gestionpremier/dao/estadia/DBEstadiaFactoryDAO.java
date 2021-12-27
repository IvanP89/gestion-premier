package org.gestionpremier.dao.estadia;

/**
 * Factory que se encarga de instanciar el DAO de Estadia que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBEstadiaFactoryDAO extends EstadiaFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase Estadia que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Estadia que trabaja con base de datos.
     */
    @Override
    public EstadiaDAO getEstadiaDAO() {

        return new DBEstadiaDAO();

    }

}

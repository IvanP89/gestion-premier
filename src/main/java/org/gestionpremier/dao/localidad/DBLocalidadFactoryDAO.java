package org.gestionpremier.dao.localidad;

/**
 * Factory que se encarga de instanciar el DAO de Localidad que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBLocalidadFactoryDAO extends LocalidadFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase Localidad que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Localidad que trabaja con base de datos.
     */
    @Override
    public LocalidadDAO getLocalidadDAO() {

        return new DBLocalidadDAO();

    }

}

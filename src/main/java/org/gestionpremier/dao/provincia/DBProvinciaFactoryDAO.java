package org.gestionpremier.dao.provincia;

/**
 * Factory que se encarga de instanciar el DAO de Provincia que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBProvinciaFactoryDAO extends ProvinciaFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase Provincia que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Provincia que trabaja con base de datos.
     */
    @Override
    public ProvinciaDAO getProvinciaDAO() {

        return new DBProvinciaDAO();

    }

}

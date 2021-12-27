package org.gestionpremier.dao.pais;

/**
 * Factory que se encarga de instanciar el DAO de Pais que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBPaisFactoryDAO extends PaisFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase Pais que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Pais que trabaja con base de datos.
     */
    @Override
    public PaisDAO getPaisDAO() {

        return new DBPaisDAO();

    }

}

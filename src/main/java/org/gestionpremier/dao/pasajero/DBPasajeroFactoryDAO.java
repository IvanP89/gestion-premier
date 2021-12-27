package org.gestionpremier.dao.pasajero;

/**
 * Factory que se encarga de instanciar el DAO de Pasajero que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBPasajeroFactoryDAO extends PasajeroFactoryDAO{

    /**
     * Instancia el DAO para objetos de la clase Pasajero que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Pasajero que trabaja con base de datos.
     */
    @Override
    public PasajeroDAO getPasajeroDAO() {

        return new DBPasajeroDAO();

    }

}

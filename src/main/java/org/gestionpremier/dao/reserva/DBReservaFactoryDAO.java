package org.gestionpremier.dao.reserva;

/**
 * Factory que se encarga de instanciar el DAO de Reserva y TitularReserva que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBReservaFactoryDAO extends ReservaFactoryDAO {

    /**
     * Instancia el DAO para objetos de las clases Reserva y TitularReserva que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Reserva y TitularReserva que trabaja con base de datos.
     */
    @Override
    public ReservaDAO getReservaDAO() {

        return new DBReservaDAO();

    }

}
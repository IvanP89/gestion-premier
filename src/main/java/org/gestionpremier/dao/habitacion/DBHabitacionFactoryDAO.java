package org.gestionpremier.dao.habitacion;

/**
 * Factory que se encarga de instanciar el DAO de Habitacion que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBHabitacionFactoryDAO extends HabitacionFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase Habitacion que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Habitacion que trabaja con base de datos.
     */
    @Override
    public HabitacionDAO getHabitacionDAO() {

        return new DBHabitacionDAO();

    }

}

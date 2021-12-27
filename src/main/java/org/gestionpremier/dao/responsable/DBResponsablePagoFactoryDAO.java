package org.gestionpremier.dao.responsable;

/**
 * Factory que se encarga de instanciar el DAO de ResponsableDePago que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBResponsablePagoFactoryDAO extends ResponsablePagoFactoryDAO {

    /**
     * Instancia el DAO para objetos de la clase ResponsableDePago que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para ResponsableDePago que trabaja con base de datos.
     */
    @Override
    public ResponsablePagoDAO getResponsablePagoDAO() {

        return new DBResponsablePagoDAO();

    }

}

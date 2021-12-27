package org.gestionpremier.dao.consumo;

/**
 * Factory que se encarga de instanciar el DAO de Consumo que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBConsumoFactoryDAO extends ConsumoFactoryDAO{

    /**
     * Instancia el DAO para objetos de la clase Consumo que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Consumo que trabaja con base de datos.
     */
    @Override
    public ConsumoDAO getConsumoDAO() {

        return new DBConsumoDAO();

    }

}

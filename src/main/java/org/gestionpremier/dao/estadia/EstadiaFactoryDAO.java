package org.gestionpremier.dao.estadia;

/**
 * Instancia una Factory cuyo rol es instanciar un DAO para el objeto Estadia que trabaje con un sistema de
 * persistencia de datos específico.
 * Parte de la implementación del patrón Factory Method.
 */

public abstract class EstadiaFactoryDAO {


    /**
     * Identificador para el tipo de Factory que instancia un DAO que trabaja con postgresql.
     */
    public static final String POSTGRESQL = "postgresql";

    /**
     * Instancia el DAO para el objeto Estadia, del tipo especifico con el que trabaja la factory.
     *
     * @return una instancia del tipo de DAO de Estadia específico de la clase.
     */
    public abstract EstadiaDAO getEstadiaDAO();

    /**
     * Instancia una Factory que sirve para instanciar el tipo de DAO de Estadia específico indicado.
     *
     * @param tipoFactory   el identificador del tipo de DAO para el cual se desea obtener una factory.
     * @return              una instancia de la factory para el tipo de DAO de Estadia indicado.
     * @throws  UnsupportedOperationException   si se recibe como argumento el identificador de un tipo de DAO que no
     *                                          se encuentra implementado.
     */
    public static EstadiaFactoryDAO getFactoryDAO(String tipoFactory) {

        switch (tipoFactory) {

            case POSTGRESQL:
                return new DBEstadiaFactoryDAO();
            default:
                throw new UnsupportedOperationException("El tipo de persistencia que desea utilizar para las estadias no está implementado.");

        }

    }

}
package org.gestionpremier.dao.factura;

/**
 * Factory que se encarga de instanciar el DAO de Factura y RenglonFactura que trabaja con base de datos postgresql.
 * Parte de la implementación del patrón Factory Method.
 */

public class DBFacturaFactoryDAO extends FacturaFactoryDAO{

    /**
     * Instancia el DAO para objetos de las clases Factura y RenglonFactura que trabaja con base de datos postgresql.
     *
     * @return  una instancia del DAO para Factura y RenglonFactura que trabaja con base de datos.
     */
    @Override
    public FacturaDAO getFacturaDAO() {

        return new DBFacturaDAO();

    }

}

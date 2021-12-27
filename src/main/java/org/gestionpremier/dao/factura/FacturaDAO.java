package org.gestionpremier.dao.factura;

import org.gestionpremier.negocio.entidades.Factura;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Factura.
 */

public interface FacturaDAO {

    /**
     * Busca una Factura en el sistema de persistencia de datos.
     *
     * @param idFactura     el id de la Factura buscada.
     * @return              una instancia de la Factura buscada, o <code>null</code> si la búsqueda no arrojó resultados.
     */
    Factura getFactura(long idFactura);

    /**
     * Busca todas las Facturas relacionadas a una Estadia.
     *
     * @param estadiaId     el id de la Estadia para la cual desean consultarse las Facturas.
     * @return              las Facturas asociadas a la Estadia.
     */
    List<Factura> getFacturas(long estadiaId);

    /**
     * Inserta una nueva Factura en el sistema de persistencia de datos.
     *
     * @param factura   la nueva Factura a insertar.
     * @return          el id generado para la Factura insertada.
     */
    long insertarFactura(Factura factura);

    /**
     * Actualiza los datos de una Factura en el sistema de persistencia de datos.
     *
     * @param factura   la Factura a actualizar.
     * @return          el id de la Factura.
     */
    long actualizarFactura(Factura factura);
}

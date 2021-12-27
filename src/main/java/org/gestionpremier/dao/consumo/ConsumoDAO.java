package org.gestionpremier.dao.consumo;

import org.gestionpremier.negocio.entidades.Consumo;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Consumo.
 */

public interface ConsumoDAO {

    /**
     * Busca un Consumo en el sistema de persistencia de datos.
     *
     * @param idConsumo     el id del Consumo buscado.
     * @return              el Consumo buscado, o <code>null</code> si la búsqueda no arroja resultados.
     */
    Consumo getConsumo(long idConsumo);

    /**
     * Busca todos los consumos asociados a una Estadia.
     *
     * @param idEstadia     el id de la Estadia cuyos consumos se desean consultar.
     * @return              una lista de los consumos de la Estadia. En el caso de que la búsqueda no arroje resultados,
     *                      retorna una lista vacía.
     */
    List<Consumo> getConsumos(long idEstadia);

    /**
     * Busca los consumos asociados a una Estadia que no han sido facturados totalmente.
     *
     * @param idEstadia     el id de la Estadia cuyos consumos se desea consultar.
     * @return              una lista de los consumos de la Estadia. En el caso de que la búsqueda no arroje resultados,
     *                      retorna una lista vacía.
     */
    List<Consumo> getConsumosImpagos(long idEstadia);

    /**
     * Actualiza un Consumo en el sistema de persistencia de datos.
     *
     * @param consumo   el Consumo a actualizar.
     * @return          el id del Consumo.
     */
    long actualizarConsumo(Consumo consumo);

    /**
     * Inserta un nuevo Consumo en el sistema de persistencia de datos.
     *
     * @param consumo   el Consumo a insertar.
     * @return          el id generado para el Consumo.
     */
    long insertarConsumo(Consumo consumo);

}

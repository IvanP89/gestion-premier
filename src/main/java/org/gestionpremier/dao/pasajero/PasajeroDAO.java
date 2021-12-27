package org.gestionpremier.dao.pasajero;

import org.gestionpremier.negocio.entidades.Pasajero;
import org.gestionpremier.utilidades.CriterioOrdenPasajeros;
import org.gestionpremier.utilidades.ListaPaginada;

import java.util.List;

/**
 * Interface de los Data Access Object de la clase entidad Pasajero.
 */

public interface PasajeroDAO {

    /**
     * Busca un pasajero en el sistema de persistencia de datos.
     *
     * @param pasajeroId    el id del Pasajero buscado.
     * @return              una instancia del Pasajero buscado, o <code>null</code> si la búsqueda no arroja resultados.
     */
    Pasajero getPasajero(long pasajeroId);

    /**
     * Busca en el sistema de persistencia de datos todos los Pasajeros que coincidan con los criterios recibidos.
     * No es necesario que todos los campos recibidos como argumento constituyan un criterio de búsqueda, los argumentos
     * que no vayan a ser usados para buscar deben pasarse como una cadena vacía "" en cuyo caso serán ignorados.
     *
     * @param nombre        el nombre del Pasajero a buscar. El criterio de búsqueda es "inicia con". Si no se requiere
     *                      buscar por nombre, este argumento debe ser la cadena vacía "".
     * @param apellido      el apellido del Pasajero a buscar. El criterio de búsqueda es "inicia con". Si no se
     *                      requiere buscar por apellido, este argumento debe ser la cadena vacía "".
     * @param tipoDocumento el tipo de documento a buscar. El criterio de búsqueda es coincidencia total. Si no se
     *                      requiere buscar por tipo de documento, este argumento debe ser la cadena vacía "".
     * @param nroDocumento  el número de documento a buscar. El criterio de búsqueda es coincidencia total. Si no se
     *                      requiere buscar por tipo de documento, este argumento debe ser la cadena vacía "".
     * @return              la lista con los resultados de la búsqueda. En caso de no arrojar resultados, retorna una
     *                      lista vacía.
     */
    List<Pasajero> getPasajeros(String apellido, String nombre, String tipoDocumento, String nroDocumento);

    /**
     * Busca Pasajeros según los criterios recibidos, utilizando paginación y retorna una lista ordenada.
     * No es necesario que todos los campos recibidos como argumento para ser usados como criterio de búsqueda contengan
     * datos para este fin, los argumentos que no vayan a ser usados para buscar deben pasarse como una cadena vacía ""
     * en cuyo caso serán ignorados.
     *
     * @param apellido          el apellido del Pasajero a buscar. El criterio de búsqueda es "inicia con". Si no se
     *                          requiere buscar por apellido, este argumento debe ser la cadena vacía "".
     * @param nombre            el nombre del Pasajero a buscar. El criterio de búsqueda es "inicia con". Si no se
     *                          requiere buscar por nombre, este argumento debe ser la cadena vacía "".
     * @param tipoDocumento     el tipo de documento a buscar. El criterio de búsqueda es coincidencia total. Si no se
     *                          requiere buscar por tipo de documento, este argumento debe ser la cadena vacía "".
     * @param nroDocumento      el número de documento a buscar. El criterio de búsqueda es coincidencia total. Si no se
     *                          requiere buscar por número de documento, este argumento debe ser la cadena vacía "".
     * @param paginaAMostrar    el índice de la página de resultados a instanciar y retornar dentro del sistema de
     *                          paginación. Inicia con cero. Si este índice no es válido, retorna una lista vaćia.
     * @param tamanyoPagina     la cantidad de elementos que van a constituir una página en la división de resultados
     *                          por paginación. Este valor será el tamaño máximo de la lista que va a retornarse.
     * @param ordenamiento      El criterio para ordenar la lista de resultados. El ordenamiento se pasa por query y es
     *                          realizado por el sistema de persistencia de datos.
     * @return                  una lista ordenada con los resultados correspondientes a la página indicada. Esta lista
     *                          va a contener además un atributo cuyo valor numérico representa la cantidad de páginas
     *                          de resultados que arrojó la query de búsqueda para el tamaño de página indicado.
     *                          Si no se encuentran resultados, retorna una lista vacía. Si el índice de la página
     *                          indicado no es válido, retorna una lista vacía.
     */
    ListaPaginada<Pasajero> getPasajerosPaginado(String apellido, String nombre, String tipoDocumento, String nroDocumento, int paginaAMostrar, int tamanyoPagina, CriterioOrdenPasajeros ordenamiento);

    /**
     * Actualiza un Pasajero en el sistema de persistencia de datos.
     * Si el pasajero no existía, lo inserta.
     *
     * @param pasajero  el Pasajero que se desea actualizar.
     * @return          el id del Pasajero.
     */
    long actualizarPasajero(Pasajero pasajero);

    /**
     * Inserta un pasajero en el sistema de persistencia de datos.
     *
     * @param pasajero  el Pasajero a insertar.
     * @return          el id generado para el Pasajero. Este id se setea automáticamente en el atributo correspondiente
     *                  de la instancia de Pasajero recibida como argumento.
     */
    long insertarPasajero(Pasajero pasajero);

}

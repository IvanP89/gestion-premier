package org.gestionpremier.utilidades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ListaPaginada es una clase que envuelve un objeto de tipo List y le suma atributos necesarios para
 * interactuar con los métodos de los DAOs que trabajen con listas paginadas.
 *
 * Esta lista debería ser la receptora de una única consulta que no varía, de modo que pueda desplazarse
 * por las páginas de la misma.
 *
 * @param <T>   La clase del objeto que va a ser almacenado en la lista.
 */

public class ListaPaginada<T> implements Iterable<T> {

    /**
     * La lista principal que esta clase amplía.
     */
    private List<T> lista;

    /**
     * La cantidad total de páginas que se obtienen al realizar la consulta a la base de datos.
     * Depende de la cantidad de elementos por página.
     *
     * Si es cero significa que la consulta no arrojó resultados y la lista está vacía.
     * Si es negativo indica que la lista no está asociada a ninguna consulta, no está inicializada.
     */
    private int cantPaginas;

    /**
     * El índice de la página que se consultó en la query y cuyos resultados están actualmente almacenados en la lista.
     * Si es negativo indica que la lista no está asociada a ninguna consulta, no está inicializada.
     */
    private int paginaActual;

    /**
     * La cantidad de elementos que hay en cada página de la consulta.
     * Si es negativo indica que la lista no está asociada a ninguna consulta, no está inicializada.
     */
    private int cantElementosPorPagina;

    /**
     * Constructor por defecto.
     * Setea los indicadores con valores no válidos para indicar que actualmente no está almacenando el resultado de
     * ninguna consulta.
     */
    public ListaPaginada() {

        lista = new ArrayList<>();
        cantPaginas = -1;
        paginaActual = -1;
        cantElementosPorPagina = -1;

    }

    /**
     * Retorna la lista principal que almacena los objetos.
     *
     * @return  la lista estándar con los objetos almacenados.
     */
    public List<T> getLista() {
        return lista;
    }

    /**
     * Setea la lista que almacena los objetos.
     *
     * @param lista     la lista que contiene los objetos a almacenar, que deberían ser los resultantes de la consulta
     *                  al DAO.
     */
    public void setLista(List<T> lista) {

        if (lista == null) {
            this.lista = new ArrayList<>();
        } else {
            this.lista = lista;
        }

    }

    /**
     * Getter del atributo cantPaginas.
     *
     * @return  la cantidad de páginas resultantes de la consulta a la base de datos. Negativo si la lista no está
     * actualmente asociada a una consulta.
     */
    public int getCantPaginas() {
        return cantPaginas;
    }

    /**
     * Setter del atributo cantPaginas.
     *
     * @param cantPaginas   la cantidad de páginas que arroja la consulta a la base de datos cuyos
     *                      resultados se guardan en esta lista. Un valor negativo si la lista no está
     *                      asociada a ninguna consulta.
     */
    public void setCantPaginas(int cantPaginas) {
        this.cantPaginas = cantPaginas;
    }

    /**
     * Getter del atributo pagina actual.
     *
     * @return  el índice de la página de la consulta a la base de datos actualmente almacenada en la lista. Un número
     * negativo si la lista no está asociaada a ninguna consulta.
     */
    public int getPaginaActual() {
        return paginaActual;
    }

    /**
     * Setter del atributo paginaActual.
     *
     * @param paginaActual  el índice de la página de la consulta a la base de datos cuyos resultados se almacenan en
     *                      esta lista.
     */
    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }

    /**
     * Getter del atributo cantElementosPorPagina.
     *
     * @return  un entero que indica la cantidad de elementos por página de los resultados de la consulta a la base de
     * datos que se almacenan en esta lista.
     */
    public int getCantElementosPorPagina() {
        return cantElementosPorPagina;
    }

    /**
     * Setter del atributo cantElementosPorPagina.
     *
     * @param cantElementosPorPagina    un entero que indica la cantidad de elementos por página de los resultados de la consulta a la base de
     *                                  datos que se almacenan en esta lista.
     */
    public void setCantElementosPorPagina(int cantElementosPorPagina) {
        this.cantElementosPorPagina = cantElementosPorPagina;
    }

    /**
     * Calcula la cantidad de páginas de resultados de la consulta a la base de datos posteriores a la página que está
     * actualmente almacenada en la lista.
     *
     * @return  un entero que indica la cantidad de páginas posteriores a la actualmente almacenada en la lista.
     */
    public int getCantPaginasSiguientes() {

        return cantPaginas - (paginaActual + 1);

    }

    /**
     * Retorna la cantidad  de resultados de la consulta a la base de datos anteriores a la actualmente almacenada
     * en la lista.
     *
     * @return  un entero que indica la cantidad de páginas anteriores a la actual. Un valor negativo si la lista
     * no está asociada a una consulta / no está inicializada.
     */
    public int getCantPaginasAnteriores() {

        return paginaActual;

    }

    /**
     * Agrega un objeto a la lista.
     *
     * @param objeto un objeto a ser agregado a la lista.
     */
    public void add(T objeto) {

        lista.add(objeto);

    }

    /**
     * calcula la cantidad de elementos que hay almacenados en la lista.
     *
     * @return  un entero que indica la cantidad de elementos almacenados en la lista.
     */
    public int size() {

        return lista.size();

    }

    /**
     * Retorna un elemento de la lista ubicado en una posición determinada de la misma.
     *
     * @param i el índice del objeto que se va a retornar.
     *
     * @return  el objeto de la lista que se encuentra en la posición indicada.
     */
    public T get (int i) {

        return lista.get(i);

    }

    /**
     * Indica si la lista está vacía.
     *
     * @return  <code>true</code> si la lista está vacía, <code>false</code> si tiene al menos un elemento.
     */
    public boolean isEmpty() {

        return lista.isEmpty();

    }

    /**
     * Retorna el iterador de la lista. Obligatorio por implementar la interfaz Iterable.
     *
     * @return  el iterador estándar de la lista interna.
     */
    @Override
    public Iterator<T> iterator() {

        return this.lista.iterator();

    }

}

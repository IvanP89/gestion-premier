package org.gestionpremier.interfaces;

/**
 * Clase para ser usada como objetos listados en un elemento gráfico de tipo ComboBox.
 * El propósito es poder listar en un ComboBox objetos que, además de tener el nombre que constituye el atributo
 * visible en la lista del ComboBox, tiene además un atributo identificador id, que será el id del objeto en su forma
 * original que se está cargando en el ComboBox. De esta forma se pueden seleccionar objetos sin necesidad de
 * identificarlos por su nombre o de llevar un registro de su posición en la lista.
 */

public class ComboItemNombreId {

    /**
     * El id que tenía la entidad a ser cargada en su forma original.
     */
    private long id;

    /**
     * El nombre que va a ser visible en la lista desplegable del ComboBox.
     */
    private String nombre;

    public ComboItemNombreId() {
    }

    public ComboItemNombreId(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Sobreescritura de toString necesaria para que el ComboBox sepa cómo mostrar el objeto en la lista de items.
     *
     * @return  el texto con el que va a verse el objeto dentro del ComboBox.
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Define el método estándar de igualdad para los objetos.
     * Necesario para poder utilizar métodos del ComboBox como setSelectedItem().
     *
     * @param obj   otro ComboItemNombreId a comparar.
     * @return      <code>true</code> si tienen el mismo id, <code>false</code> en el caso contrario.
     */
    @Override
    public boolean equals(Object obj) {

        return ((ComboItemNombreId) obj).getId() == id;

    }
}


package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@Entity
public class Localidad {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    @Id
    private long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    private Provincia provincia;

    /**************************************************/
    /**                CONSTRUCTORES                 **/
    /**************************************************/

    public Localidad() {

    }

    public Localidad (long id, String nombre, Provincia provincia) {
        this.id = id;
        this.nombre = nombre;
        this.provincia = provincia;
    }

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

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

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    /**************************************************/
    /**                 OTROS MÉTODOS                **/
    /**************************************************/

    @Override
    public String toString() {
        return "Localidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", provincia=" + provincia +
                '}';
    }

}

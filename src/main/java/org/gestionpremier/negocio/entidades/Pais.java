package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.util.List;

@Entity
public class Pais {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    @Id
    private long id;

    private String nombre;

    @OneToMany(mappedBy = "pais")
    private List<Provincia> provincias;

    /**************************************************/
    /**                CONSTRUCTORES                 **/
    /**************************************************/

    public Pais() {

    }

    public Pais(long id, String nombre, List<Provincia> provincias) {
        this.id = id;
        this.nombre = nombre;
        this.provincias = provincias;
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

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    /**************************************************/
    /**                 OTROS MÉTODOS                **/
    /**************************************************/

    @Override
    public String toString() {
        return "Pais{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

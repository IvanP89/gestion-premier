package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.util.List;

@Entity
public class Provincia {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    @Id
    private long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id")
    private Pais pais;

    @OneToMany(mappedBy = "provincia")
    private List<Localidad> localidades;

    /**************************************************/
    /**                CONSTRUCTORES                 **/
    /**************************************************/

    public Provincia() {

    }

    public Provincia(long id, String nombre, Pais pais, List<Localidad> localidades) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.localidades = localidades;
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    /**************************************************/
    /**                 OTROS MÉTODOS                **/
    /**************************************************/

    @Override
    public String toString() {
        return "Provincia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pais=" + pais +
                '}';
    }
}

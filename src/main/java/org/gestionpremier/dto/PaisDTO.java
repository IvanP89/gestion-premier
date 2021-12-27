package org.gestionpremier.dto;

public class PaisDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private long id_pais;
    private String nombre;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public long getId_pais() {
        return id_pais;
    }

    public void setId_pais(long id_pais) {
        this.id_pais = id_pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

package org.gestionpremier.dto;

public class ProvinciaDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private long id_provincia;
    private String nombre;
    private PaisDTO pais;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public long getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(long id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PaisDTO getPais() {
        return pais;
    }

    public void setPais(PaisDTO pais) {
        this.pais = pais;
    }
}

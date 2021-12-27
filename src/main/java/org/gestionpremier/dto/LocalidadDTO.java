package org.gestionpremier.dto;

public class LocalidadDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private long id_localidad;
    private String nombre;
    private ProvinciaDTO provincia;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public long getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(long id_localidad) {
        this.id_localidad = id_localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ProvinciaDTO getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaDTO provincia) {
        this.provincia = provincia;
    }
}

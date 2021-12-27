package org.gestionpremier.dto;

public class BusquedaPasajerosDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private String apellido;
    private String nombre;
    private String tipoDoc;
    private String nroDoc;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }
}

package org.gestionpremier.dto;

public class DireccionDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private String calle;
    private String nroCalle;
    private String depto;
    private String piso;
    private String cp;
    private LocalidadDTO localidad;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNroCalle() {
        return nroCalle;
    }

    public void setNroCalle(String nroCalle) {
        this.nroCalle = nroCalle;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public LocalidadDTO getLocalidad() {
        return localidad;
    }

    public void setLocalidad(LocalidadDTO localidad) {
        this.localidad = localidad;
    }
}

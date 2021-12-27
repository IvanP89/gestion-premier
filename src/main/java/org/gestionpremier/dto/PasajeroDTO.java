package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.PosicionIva;
import org.gestionpremier.negocio.entidades.TipoDoc;
import java.time.LocalDate;

public class PasajeroDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private long id;
    private String nroDoc;
    private TipoDoc tipoDoc;
    private String apellido;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String cuit;
    private String ocupacion;
    private PosicionIva posicionIva;
    private DireccionDTO direccion;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCuit() {

        if (cuit == null) {
            return "";
        }

        return cuit;

    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public PosicionIva getPosicionIva() {
        return posicionIva;
    }

    public void setPosicionIva(PosicionIva posicionIva) {
        this.posicionIva = posicionIva;
    }

    public DireccionDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionDTO direccion) {
        this.direccion = direccion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PasajeroDTO{" +
                "apellido='" + apellido + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o){
        try {
            return this.getId() == ((PasajeroDTO) o).getId();
        } catch(ClassCastException e){
            return false;
        }
    }
}

package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.EstadoHabitacion;
import org.gestionpremier.negocio.entidades.TipoHabitacion;
import java.time.LocalDateTime;

public class HabitacionDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private Long id;
    private String nro;
    private LocalDateTime fechaEstado;
    private EstadoHabitacion estado;
    private TipoHabitacion tipo;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public LocalDateTime getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(LocalDateTime fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }
}

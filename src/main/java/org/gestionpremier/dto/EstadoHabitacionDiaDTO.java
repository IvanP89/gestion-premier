package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.EstadoHabitacion;

import java.time.LocalDateTime;

public class EstadoHabitacionDiaDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private LocalDateTime fecha;
    private EstadoHabitacion estado;
    private long idHabitacion;
    private long idEstadia;
    private long idReserva;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public long getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(long idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public long getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(long idEstadia) {
        this.idEstadia = idEstadia;
    }

    public long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(long idReserva) {
        this.idReserva = idReserva;
    }
}

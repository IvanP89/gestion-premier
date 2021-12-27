package org.gestionpremier.dto;

import java.time.LocalDateTime;

public class ReservaDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private TitularReservaDTO titular;
    private EstadoHabitacionDTO habitacion;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public TitularReservaDTO getTitular() {
        return titular;
    }

    public void setTitular(TitularReservaDTO titular) {
        this.titular = titular;
    }

    public EstadoHabitacionDTO getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(EstadoHabitacionDTO habitacion) {
        this.habitacion = habitacion;
    }
}
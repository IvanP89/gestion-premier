package org.gestionpremier.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EstadiaDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private long id;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private float importe;
    private float deuda;
    private HabitacionDTO habitacionDTO;
    List<PasajeroDTO> pasajeros;
    PasajeroDTO pasajeroResponsable;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public float getDeuda() {
        return deuda;
    }

    public void setDeuda(float deuda) {
        this.deuda = deuda;
    }

    public HabitacionDTO getHabitacionDTO() {
        return habitacionDTO;
    }

    public void setHabitacionDTO(HabitacionDTO habitacionDTO) {
        this.habitacionDTO = habitacionDTO;
    }

    public List<PasajeroDTO> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<PasajeroDTO> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public PasajeroDTO getPasajeroResponsable() {
        return pasajeroResponsable;
    }

    public void setPasajeroResponsable(PasajeroDTO pasajeroResponsable) {
        this.pasajeroResponsable = pasajeroResponsable;
    }

    public void addPasajero(PasajeroDTO pasajero) {
        pasajeros.add(pasajero);
    }

}

package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "reserva")
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fecha_desde", nullable = false)
    private LocalDateTime fechaDesde;

    @Column(name = "fecha_hasta", nullable = false)
    private LocalDateTime fechaHasta;

    @Column(name = "es_mantenimiento", nullable = false)
    private Boolean esMantenimiento;

    @ManyToOne
    @JoinColumn(name = "id_titular", referencedColumnName = "id")
    TitularReserva titular;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public TitularReserva getTitular() {
        return titular;
    }

    public void setTitular(TitularReserva titular) {
        this.titular = titular;
    }

    public LocalDateTime getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDateTime fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public LocalDateTime getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDateTime fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEsMantenimiento() {
        return esMantenimiento;
    }

    public void setEsMantenimiento(Boolean esMantenimiento) {
        this.esMantenimiento = esMantenimiento;
    }
}
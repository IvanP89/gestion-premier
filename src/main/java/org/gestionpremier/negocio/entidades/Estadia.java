package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "estadia")
@Entity
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fecha_desde")
    private LocalDateTime fechaDesde;

    @Column(name = "fecha_hasta")
    private LocalDateTime fechaHasta;

    @Column(name = "importe")
    private float importe;

    @Column(name = "deuda")
    private float deuda;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_habitacion", nullable = false)
    private Habitacion habitacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_responsable", nullable = false)
    private Pasajero responsable;

    @OneToMany(mappedBy = "estadia")
    private List<Consumo> consumos;

    @OneToMany(mappedBy = "estadia")
    private List<Factura> facturas;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "estadia_pasajeros",
            joinColumns = @JoinColumn(name = "id_estadia"),
            inverseJoinColumns = @JoinColumn(name = "id_pasajero"))
    private List<Pasajero> pasajeros;

    public Estadia() {
        consumos = new ArrayList<>();
        facturas = new ArrayList<>();
        pasajeros = new ArrayList<>();
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public List<Consumo> getConsumos() {
        return consumos;
    }

    public void addConsumo(Consumo consumo) {
        consumos.add(consumo);
    }

    public void setConsumos(List<Consumo> consumos) {
        this.consumos = consumos;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<Pasajero> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public void addPasajero(Pasajero pasajero) {
        pasajeros.add(pasajero);
    }

    public Pasajero getResponsable() {
        return responsable;
    }

    public void setResponsable(Pasajero responsable) {
        this.responsable = responsable;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public float getDeuda() {
        return deuda;
    }

    public void setDeuda(float deuda) {
        this.deuda = deuda;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
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

}
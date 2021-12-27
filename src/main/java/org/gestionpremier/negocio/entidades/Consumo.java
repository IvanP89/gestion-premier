package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "consumo")
@Entity
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "detalle", length = 100)
    private String detalle;

    @Column(name = "importe_unitario")
    private float importeUnitario;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "cantidad_facturada")
    private int cantidadFacturada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoServicio tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estadia", nullable = false)
    private Estadia estadia;

    public Consumo() {

        cantidadFacturada = 0;

    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        this.tipo = tipo;
    }

    public int getCantidadFacturada() {
        return cantidadFacturada;
    }

    public void setCantidadFacturada(int cantidadFacturada) {
        this.cantidadFacturada = cantidadFacturada;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getImporteUnitario() {
        return importeUnitario;
    }

    public void setImporteUnitario(float importeUnitario) {
        this.importeUnitario = importeUnitario;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
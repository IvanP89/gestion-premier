package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.TipoServicio;

import java.time.LocalDateTime;

public class ConsumoDTO {

    private Long id;
    private String detalle;
    private float importeUnitario;
    private int cantidad;
    private LocalDateTime fecha;
    private int cantidadFacturada;
    private TipoServicio tipo;
    private EstadiaDTO estadia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public float getImporteUnitario() {
        return importeUnitario;
    }

    public void setImporteUnitario(float importeUnitario) {
        this.importeUnitario = importeUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCantidadFacturada() {
        return cantidadFacturada;
    }

    public void setCantidadFacturada(int cantidadFacturada) {
        this.cantidadFacturada = cantidadFacturada;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        this.tipo = tipo;
    }

    public EstadiaDTO getEstadia() {
        return estadia;
    }

    public void setEstadia(EstadiaDTO estadia) {
        this.estadia = estadia;
    }
}

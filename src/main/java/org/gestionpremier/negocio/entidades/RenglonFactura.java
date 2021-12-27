package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

// LOS RENGLONES SE GUARDAN SOLOS EN LA BASE DE DATOS AL GUARDAR LA FACTURA A LA Q ESTÁN ASOCIADOS.
@Table(name = "renglon_factura")
@Entity
public class RenglonFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nro")
    private int nro;

    @Column(name = "nombre_consumo", length = 100)
    private String nombreConsumo;

    @Column(name = "importe_unitario")
    private float importeUnitario;

    @Column(name = "cantidad")
    private int cantidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_consumo", nullable = false)
    private Consumo consumo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "nro_factura", nullable = false)
    private Factura factura;

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Consumo getConsumo() {
        return consumo;
    }

    public void setConsumo(Consumo consumo) {
        this.consumo = consumo;
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

    public String getNombreConsumo() {
        return nombreConsumo;
    }

    public void setNombreConsumo(String nombreConsumo) {
        this.nombreConsumo = nombreConsumo;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
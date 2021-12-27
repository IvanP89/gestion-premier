package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name = "factura")
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro", nullable = false)
    private Long nro;

    @Column(name = "tipo")
    private char tipo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "total")
    private float total;

    @Column(name = "iva")
    private float iva;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoFactura estado;

    // LOS RENGLONES NO SE LEVANTAN SOLOS DE LA BASE DE DATOS AL LEVANTAR LA FACTURA.
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<RenglonFactura> renglones;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estadia", nullable = false)
    private Estadia estadia;

    @ManyToOne
    @JoinColumn(name = "id_cliente_pasajero")
    private Pasajero clientePasajero;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_responsable_pago")
    private ResponsableDePago clienteResponsable;

    @ManyToOne
    @JoinColumn(name = "nro_nota_credito")
    private NotaDeCredito notaDeCredito;

    @OneToOne(mappedBy = "factura", cascade = CascadeType.ALL)
    private Pago pago;

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public NotaDeCredito getNotaDeCredito() {
        return notaDeCredito;
    }

    public void setNotaDeCredito(NotaDeCredito notaDeCredito) {
        this.notaDeCredito = notaDeCredito;
    }

    public ResponsableDePago getClienteResponsable() {
        return clienteResponsable;
    }

    public void setClienteResponsable(ResponsableDePago clienteResponsable) {
        this.clienteResponsable = clienteResponsable;
    }

    public Pasajero getClientePasajero() {
        return clientePasajero;
    }

    public void setClientePasajero(Pasajero clientePasajero) {
        this.clientePasajero = clientePasajero;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }

    public List<RenglonFactura> getRenglones() {
        return renglones;
    }

    public void addRenglon(RenglonFactura renglon) {

        renglones.add(renglon);
        total = total + renglon.getImporteUnitario() * renglon.getCantidad();

    }

    public void setRenglones(List<RenglonFactura> renglones) {
        this.renglones = renglones;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

}
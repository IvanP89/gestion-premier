package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "pago")
@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro", nullable = false)
    private Long nro;

    @Column(name = "importe", nullable = false)
    private float importe;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "nro_factura", nullable = false)
    private Factura factura;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    private List<Efectivo> efectivo = new ArrayList<>();

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    private List<Cheque> cheques = new ArrayList<>();

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    private List<TarjetaDeCredito> tarjetasDeCredito = new ArrayList<>();

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    private List<TarjetaDeDebito> tarjetasDeDebito = new ArrayList<>();

    public List<TarjetaDeDebito> getTarjetasDeDebito() {
        return tarjetasDeDebito;
    }

    public void setTarjetasDeDebito(List<TarjetaDeDebito> tarjetasDeDebito) {
        this.tarjetasDeDebito = tarjetasDeDebito;
    }

    public List<TarjetaDeCredito> getTarjetasDeCredito() {
        return tarjetasDeCredito;
    }

    public void setTarjetasDeCredito(List<TarjetaDeCredito> tarjetasDeCredito) {
        this.tarjetasDeCredito = tarjetasDeCredito;
    }

    public List<Cheque> getCheques() {
        return cheques;
    }

    public void setCheques(List<Cheque> cheques) {
        this.cheques = cheques;
    }

    public List<Efectivo> getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(List<Efectivo> efectivo) {
        this.efectivo = efectivo;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

}
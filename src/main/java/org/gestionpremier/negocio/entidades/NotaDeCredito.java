package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@Table(name = "nota_credito")
@Entity
public class NotaDeCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nro", nullable = false)
    private Long nro;

    @Column(name = "tipo")
    private char tipo;

    @Column(name = "iva")
    private float iva;

    @Column(name = "importe_neto")
    private float importeNeto;

    @ManyToOne
    @JoinColumn(name = "id_pasajero")
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "id_responsable_pago")
    private ResponsableDePago titularResponsable;

    public ResponsableDePago getTitularResponsable() {
        return titularResponsable;
    }

    public void setTitularResponsable(ResponsableDePago titularResponsable) {
        this.titularResponsable = titularResponsable;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public float getImporteNeto() {
        return importeNeto;
    }

    public void setImporteNeto(float importeNeto) {
        this.importeNeto = importeNeto;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
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
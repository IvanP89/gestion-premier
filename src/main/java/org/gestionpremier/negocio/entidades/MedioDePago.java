package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cotizacion_moneda", nullable = false)
    private float cotizacionMoneda;

    @Column(name = "monto")
    private float monto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_moneda", nullable = false)
    private Moneda moneda;

    @ManyToOne
    @JoinColumn(name = "nro_pago")
    private Pago pago;

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public float getCotizacionMoneda() {
        return cotizacionMoneda;
    }

    public void setCotizacionMoneda(float cotizacionMoneda) {
        this.cotizacionMoneda = cotizacionMoneda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
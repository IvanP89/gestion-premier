package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "cheque")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Cheque extends MedioDePago {

    @Column(name = "nro", nullable = false)
    private Long nro;

    @Column(name = "nombre_titular", nullable = false, length = 70)
    private String nombreTitular;

    @Column(name = "plaza", nullable = false, length = 50)
    private String plaza;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

}
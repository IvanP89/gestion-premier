package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@Table(name = "tarjeta_de_credito")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TarjetaDeCredito extends MedioDePago {

    @Column(name = "entidad", nullable = false, length = 30)
    private String entidad;

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

}
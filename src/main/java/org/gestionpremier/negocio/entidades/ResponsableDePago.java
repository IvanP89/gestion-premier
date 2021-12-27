package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@Table(name = "responsable_de_pago")
@Entity
public class ResponsableDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "razon_social", length = 30)
    private String razonSocial;

    @Column(name = "cuit", length = 20)
    private String cuit;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @OneToOne(mappedBy = "responsableDePago", cascade = CascadeType.ALL)
    private Direccion direccion;

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        if (this.direccion == null) {

            this.direccion = direccion;
            this.direccion.setResponsableDePago(this);

        } else {

            this.direccion.setCalle(direccion.getCalle());
            this.direccion.setNroCalle(direccion.getNroCalle());
            this.direccion.setDepto(direccion.getDepto());
            this.direccion.setPiso(direccion.getPiso());
            this.direccion.setLocalidad(direccion.getLocalidad());

        }
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
package org.gestionpremier.negocio.entidades;

import javax.persistence.*;

@Entity
public class Direccion {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String calle;

    @Column(name = "nro_calle")
    private String nroCalle;

    private String depto;
    private String piso;
    private String cp;

    @OneToOne
    @JoinColumn(name = "id_pasajero", referencedColumnName = "id")
    private Pasajero pasajero;



    @ManyToOne(optional = false)
    @JoinColumn(name = "id_localidad", referencedColumnName = "id")
    private Localidad localidad;

    @OneToOne
    @JoinColumn(name = "id_responsable_pago")
    private ResponsableDePago responsableDePago;

    @OneToOne
    @JoinColumn(name = "id_banco")
    private Banco banco;

    /**************************************************/
    /**                CONSTRUCTORES                 **/
    /**************************************************/

    public Direccion() {

    }

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNroCalle() {
        return nroCalle;
    }

    public void setNroCalle(String nroCalle) {
        this.nroCalle = nroCalle;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public ResponsableDePago getResponsableDePago() {
        return responsableDePago;
    }

    public void setResponsableDePago(ResponsableDePago responsableDePago) {
        this.responsableDePago = responsableDePago;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    /**************************************************/
    /**                 OTROS MÉTODOS                **/
    /**************************************************/

    @Override
    public String toString() {
        return calle
                + " "
                + nroCalle
                + ", piso: "
                + piso
                + ", depto: "
                + depto
                + ", localidad: "
                + localidad.getNombre()
                + ".";
    }
}

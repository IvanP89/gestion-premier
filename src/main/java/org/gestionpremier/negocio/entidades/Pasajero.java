package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Pasajero {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private String apellido;

    @Column(name = "tipo_doc")
    @Enumerated(EnumType.STRING)
    private TipoDoc tipoDoc;

    @Column(name = "nro_doc")
    private String nroDoc;

    private String telefono;
    private String email;

    @Column(name = "fecha_nac")
    private LocalDate fechaNacimiento;

    private String nacionalidad;
    private String cuit;
    private String ocupacion;

    @Column(name = "pos_iva")
    @Enumerated(EnumType.STRING)
    private PosicionIva posicionIva;

    @OneToOne(mappedBy = "pasajero", cascade = CascadeType.ALL)
    private Direccion direccion;

    /**************************************************/
    /**                CONSTRUCTORES                 **/
    /**************************************************/

    public Pasajero() {

    }

    public Pasajero(String nombre,
                    String apellido,
                    TipoDoc tipoDoc,
                    String nroDoc,
                    String telefono,
                    String email,
                    LocalDate fechaNacimiento,
                    String nacionalidad,
                    String cuit,
                    String ocupacion,
                    PosicionIva posicionIva,
                    Direccion direccion) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.cuit = cuit;
        this.ocupacion = ocupacion;
        this.posicionIva = posicionIva;
        this.direccion = direccion;

    }

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public PosicionIva getPosicionIva() {
        return posicionIva;
    }

    public void setPosicionIva(PosicionIva posicionIva) {
        this.posicionIva = posicionIva;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {

        /*
         * ??Por qu?? el set de direcci??n es distinto?
         *
         * Para implementar la persistencia de hibernate:
         *
         * Si direcci??n es null, entonces el pasajero no ten??a direcci??n persistida en la base de datos, por lo que
         * todo normal, se setea como el resto y listo, despu??s cuando el objeto pasajero se guarda en la base de datos,
         * se guarda el objeto direcci??n con una nueva entrada en su tabla correspondiente.
         *
         * Ahora, si el objeto persona ya ten??a una direcci??n desde antes y se le setea una nueva con una
         * simple asignaci??n, entonces cuando persona se guarde, se va a actualizar su campo id_direcci??n y SE VA A
         * GUARDAR LA DIRECCI??N NUEVA COMO UNA NUEVA ENTRADA EN LA TABLA, CON PERSONA AHORA APUNTANDO A ESA, MIENTRAS
         * Q LA DIRECCI??N VIEJA NO SE VA A BORRAR Y VA QUEDAR AH?? OCUPANDO ESPACIO Y NUNCA SIENDO USADA DE NUEVO. No s??
         * si se supone q en este caso Hibernate tiene q ocuparse de borrar el objeto viejo, pero si es as??, prob?? de mil
         * maneras pero no pude lograr q eso pase.
         *
         * Por otro lado, si el objeto persona ya ten??a una direcci??n desde antes, pero en vez de setearle una direcci??n
         * nueva con una asignaci??n, copio en la direcci??n vieja los atributos de la direcci??n nueva y vuelvo a guardar
         * la persona con el objeto direcci??n viejo con atributos actualizados, ah?? Hibernate hace la actualizaci??n de
         * la direcci??n solo.
         *
         * */

        if (this.direccion == null) {

            this.direccion = direccion;
            this.direccion.setPasajero(this);

        } else {

            this.direccion.setCalle(direccion.getCalle());
            this.direccion.setNroCalle(direccion.getNroCalle());
            this.direccion.setDepto(direccion.getDepto());
            this.direccion.setPiso(direccion.getPiso());
            this.direccion.setLocalidad(direccion.getLocalidad());

        }

    }

    /**************************************************/
    /**                 OTROS M??TODOS                **/
    /**************************************************/

    @Override
    public String toString() {
        return "Pasajero{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tipoDoc=" + tipoDoc +
                ", nroDoc='" + nroDoc + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", cuit='" + cuit + '\'' +
                ", ocupacion='" + ocupacion + '\'' +
                ", posicionIva=" + posicionIva +
                ", direccion=" + direccion +
                '}';
    }
}

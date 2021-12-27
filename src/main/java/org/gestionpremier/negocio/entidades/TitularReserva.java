package org.gestionpremier.negocio.entidades;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "titular_reserva")
@Entity
public class TitularReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "apellido", nullable = false, length = 35)
    private String apellido;

    @Column(name = "nombre", nullable = false, length = 35)
    private String nombre;

    @Column(name = "telefono", nullable = false, length = 25)
    private String telefono;

    @OneToMany(mappedBy = "titular", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Reserva> reservas;

    public TitularReserva() {

        reservas = new ArrayList<>();

    }

    public void addReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void eliminarReserva(long id) {
        reservas.removeIf(reserva -> (reserva.getId() == id));
    }

    public void eliminarReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
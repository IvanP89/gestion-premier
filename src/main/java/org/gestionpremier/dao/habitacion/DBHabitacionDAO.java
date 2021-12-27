package org.gestionpremier.dao.habitacion;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Habitacion;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Habitacion que trabaja con base de datos y Hibernate.
 */

public class DBHabitacionDAO extends HibernateDAO implements HabitacionDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Habitacion> getHabitaciones() {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Habitacion");

        List<Habitacion> habitacionesLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (habitacionesLeidas == null) {
            habitacionesLeidas = new ArrayList<>();
        }

        return habitacionesLeidas;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Habitacion> getHabitacion(String nroHabitacion) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Habitacion where nro=:nro_habitacion");
        query.setParameter("nro_habitacion", nroHabitacion);

        List<Habitacion> habitacionesLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (habitacionesLeidas == null) {
            habitacionesLeidas = new ArrayList<>();
        }

        return habitacionesLeidas;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Habitacion getHabitacion(long idHabitacion) {

        iniciarSesionHibernate();

        Habitacion habitacionLeida = sesionHibernate.get(Habitacion.class, idHabitacion);

        cerrarSesionHibernate();

        return habitacionLeida;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long actualizarHabitacion(Habitacion habitacion) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.update(habitacion);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return habitacion.getId();

    }

}

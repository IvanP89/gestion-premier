package org.gestionpremier.dao.reserva;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Reserva;
import org.gestionpremier.negocio.entidades.TitularReserva;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de las clases entidad Reserva y TitularReserva que trabaja con base de datos y Hibernate.
 */

public class DBReservaDAO extends HibernateDAO implements ReservaDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertarTitular(TitularReserva titularReserva) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.save(titularReserva);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return titularReserva.getId();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reserva> getReservas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Reserva where fecha_desde BETWEEN :f_desde AND :f_hasta OR fecha_hasta BETWEEN :f_desde AND :f_hasta");
        query.setParameter("f_desde", fechaDesde);
        query.setParameter("f_hasta", fechaHasta);

        List<Reserva> reservasLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (reservasLeidas == null) {
            reservasLeidas = new ArrayList<>();
        }

        return reservasLeidas;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reserva getReserva(long idReserva) {

        iniciarSesionHibernate();

        Reserva reservaLeida = sesionHibernate.get(Reserva.class, idReserva);

        cerrarSesionHibernate();

        return reservaLeida;
    }

}

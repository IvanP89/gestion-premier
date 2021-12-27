package org.gestionpremier.dao.estadia;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Estadia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.Query;

/**
 * Data Access Object de la clase entidad Estadia que trabaja con base de datos y Hibernate.
 */

public class DBEstadiaDAO extends HibernateDAO implements EstadiaDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Estadia getUltimaEstadiaImpagaYaEmpezada(long idHabitacion) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Estadia where id_habitacion=:id AND (deuda!=:deudaCero OR importe=:importeCero) AND fecha_desde<=:fechaActual");
        query.setParameter("id", idHabitacion);
        query.setParameter("deudaCero", 0f);
        query.setParameter("importeCero", 0f);
        query.setParameter("fechaActual", LocalDateTime.now());

        List<Estadia> estadias = query.getResultList();

        cerrarSesionHibernate();

        try {

            return estadias.get(0);

        } catch (NullPointerException | IndexOutOfBoundsException e) {

            return null;

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Estadia> getEstadias(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Estadia where (fecha_desde BETWEEN :f_desde AND :f_hasta) OR (fecha_hasta BETWEEN :f_desde AND :f_hasta) OR (fecha_hasta > :f_hasta AND fecha_desde < :f_desde)");
        query.setParameter("f_desde", fechaDesde);
        query.setParameter("f_hasta", fechaHasta);

        List<Estadia> estadiasLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (estadiasLeidas == null) {
            estadiasLeidas = new ArrayList<>();
        }

        return estadiasLeidas;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Estadia getEstadia(long idEstadia) {

        iniciarSesionHibernate();

        Estadia estadiaLeia = sesionHibernate.get(Estadia.class, idEstadia);

        cerrarSesionHibernate();

        return estadiaLeia;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long actualizarEstadia(Estadia estadia) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.update(estadia);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return estadia.getId();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertarEstadia(Estadia estadia) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.save(estadia);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return estadia.getId();

    }

}

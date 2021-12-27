package org.gestionpremier.dao.consumo;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Consumo;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Consumo que trabaja con base de datos y Hibernate.
 */

public class DBConsumoDAO extends HibernateDAO implements ConsumoDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Consumo getConsumo(long idConsumo) {

        iniciarSesionHibernate();

        Consumo consumo=sesionHibernate.get(Consumo.class, idConsumo);

        cerrarSesionHibernate();

        return consumo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Consumo> getConsumos(long idEstadia) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Consumo where id_estadia=:id_est");
        query.setParameter("id_est", idEstadia);

        List<Consumo> consumosLeidos = query.getResultList();

        cerrarSesionHibernate();

        if (consumosLeidos == null) {
            return new ArrayList<>();
        }

        return consumosLeidos;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Consumo> getConsumosImpagos(long idEstadia) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Consumo where id_estadia=:id_est and cantidad_facturada!=cantidad");
        query.setParameter("id_est", idEstadia);

        List<Consumo> consumosLeidos = query.getResultList();

        cerrarSesionHibernate();

        if (consumosLeidos == null) {
            return new ArrayList<>();
        }

        return consumosLeidos;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long actualizarConsumo(Consumo consumo) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.update(consumo);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return consumo.getId();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertarConsumo(Consumo consumo) {

        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.save(consumo);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return consumo.getId();

    }

}

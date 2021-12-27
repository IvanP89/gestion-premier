package org.gestionpremier.dao.responsable;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.ResponsableDePago;
import org.hibernate.query.Query;

import java.util.*;

/**
 * Data Access Object de la clase entidad ResponsableDePago que trabaja con base de datos y Hibernate.
 */

public class DBResponsablePagoDAO extends HibernateDAO implements ResponsablePagoDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponsableDePago getResponsable(long idResponsable) {

        iniciarSesionHibernate();

        ResponsableDePago responsableLeido = sesionHibernate.get(ResponsableDePago.class, idResponsable);

        cerrarSesionHibernate();

        return responsableLeido;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResponsableDePago> getResponsables(String cuit) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from ResponsableDePago where cuit=:cuit_buscado");
        query.setParameter("cuit_buscado", cuit);

        List<ResponsableDePago> responsablesLeidos = query.getResultList();

        cerrarSesionHibernate();

        if (responsablesLeidos == null) {
            responsablesLeidos = new ArrayList<>();
        }

        return responsablesLeidos;

    }

}

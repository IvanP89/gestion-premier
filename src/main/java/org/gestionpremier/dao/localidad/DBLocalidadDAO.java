package org.gestionpremier.dao.localidad;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Localidad;
import org.gestionpremier.negocio.entidades.Provincia;

import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Localidad que trabaja con base de datos y Hibernate.
 */

public class DBLocalidadDAO extends HibernateDAO implements LocalidadDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Localidad getLocalidad(long idLocalidad) {

        iniciarSesionHibernate();

        Localidad localidadLeida = sesionHibernate.get(Localidad.class, idLocalidad);

        cerrarSesionHibernate();

        return localidadLeida;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Localidad> getLocalidades(Provincia provincia) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Localidad where id_provincia=:id");
        query.setParameter("id", provincia.getId());

        List<Localidad> localidadesLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (localidadesLeidas == null) {
            localidadesLeidas = new ArrayList<>();
        }

        return localidadesLeidas;

    }

}
package org.gestionpremier.dao.provincia;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Pais;
import org.gestionpremier.negocio.entidades.Provincia;

import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Provincia que trabaja con base de datos y Hibernate.
 */

public class DBProvinciaDAO extends HibernateDAO implements ProvinciaDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Provincia> getProvincias(Pais pais) {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Provincia where id_pais=:id");
        query.setParameter("id", pais.getId());

        List<Provincia> provinciasLeidas = query.getResultList();

        cerrarSesionHibernate();

        if (provinciasLeidas == null) {
            provinciasLeidas = new ArrayList<>();
        }

        return provinciasLeidas;

    }

}

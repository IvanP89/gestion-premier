package org.gestionpremier.dao.pais;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Pais;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Pais que trabaja con base de datos y Hibernate.
 */

public class DBPaisDAO extends HibernateDAO implements PaisDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pais> getPaises() {

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery("from Pais");

        List<Pais> paisesLeidos = query.getResultList();

        cerrarSesionHibernate();

        if (paisesLeidos == null) {
            paisesLeidos = new ArrayList<>();
        }

        return paisesLeidos;

    }

}

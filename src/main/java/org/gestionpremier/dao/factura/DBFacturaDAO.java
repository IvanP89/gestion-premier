package org.gestionpremier.dao.factura;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Factura;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object de la clase entidad Factura que trabaja con base de datos y Hibernate.
 */

public class DBFacturaDAO extends HibernateDAO implements FacturaDAO{

    /**
     * {@inheritDoc}
     */
    @Override
    public Factura getFactura(long idFactura) {

        iniciarSesionHibernate();

        Factura factura=sesionHibernate.get(Factura.class, idFactura);

        cerrarSesionHibernate();

        return factura;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Factura> getFacturas(long estadiaId) {
        iniciarSesionHibernate();

        Query query= sesionHibernate.createQuery("from factura where id_estadia=:id");
        query.setParameter("id", estadiaId);

        List<Factura> listaFacturas=query.getResultList();

        cerrarSesionHibernate();

        return listaFacturas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertarFactura(Factura factura) {
        iniciarSesionHibernate();

        sesionHibernate.beginTransaction();
        sesionHibernate.saveOrUpdate(factura);
        sesionHibernate.getTransaction().commit();

        cerrarSesionHibernate();

        return factura.getNro();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long actualizarFactura(Factura factura) {
        return insertarFactura(factura);
    }

}

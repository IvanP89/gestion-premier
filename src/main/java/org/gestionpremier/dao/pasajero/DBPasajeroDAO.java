package org.gestionpremier.dao.pasajero;

import org.gestionpremier.dao.HibernateDAO;
import org.gestionpremier.negocio.entidades.Pasajero;
import org.gestionpremier.utilidades.CriterioOrdenPasajeros;
import org.gestionpremier.utilidades.ListaPaginada;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object de la clase entidad Pasajero que trabaja con base de datos y Hibernate.
 */

public class DBPasajeroDAO extends HibernateDAO implements PasajeroDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Pasajero getPasajero(long pasajeroId) {

        iniciarSesionHibernate();

        Pasajero pasajeroLeido = sesionHibernate.get(Pasajero.class, pasajeroId);

        cerrarSesionHibernate();

        return pasajeroLeido;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pasajero> getPasajeros(String nombre, String apellido, String tipoDocumento, String nroDocumento) {

        boolean setParameterApellido = false;
        boolean setParameterNombre = false;
        boolean setParameterTipoDoc = false;
        boolean setParameterNroDoc = false;

        //*********  CONSTRUYENDO LA CONSULTA DE ACUERDO A LOS ARGUMENTOS QUE RECIBIÓ EL MÉTODO ******************

        StringBuilder subConsulta = new StringBuilder();
        subConsulta.append("from Pasajero where");

        if (nombre != "") {

            subConsulta.append(" nombre like :nom");
            setParameterNombre = true;

        }

        if (apellido != "") {

            subConsulta.append(" and apellido like :ape");
            setParameterApellido = true;

        }

        if (tipoDocumento != "") {

            subConsulta.append(" and tipo_doc=:tdoc");
            setParameterTipoDoc = true;

        }

        if (nroDocumento != "") {

            subConsulta.append(" and nro_doc=:ndoc");
            setParameterNroDoc = true;

        }

        String consulta = subConsulta.toString().replaceAll("where and", "where");

        //****************************** FIN DE CONSTRUCCIÓN DE LA CONSULTA *****************************************

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery(consulta);

        if (setParameterNombre) {
            query.setParameter("nom", nombre + "%");
        }
        if (setParameterApellido) {
            query.setParameter("ape", apellido + "%");
        }
        if (setParameterTipoDoc) {
            query.setParameter("tdoc", tipoDocumento.toString());
        }
        if (setParameterNroDoc) {
            query.setParameter("ndoc", nroDocumento);
        }

        List<Pasajero> pasajerosLeidos = query.getResultList();

        cerrarSesionHibernate();

        if (pasajerosLeidos == null) {
            pasajerosLeidos = new ArrayList<>();
        }

        return pasajerosLeidos;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListaPaginada<Pasajero> getPasajerosPaginado(String apellido, String nombre, String tipoDocumento, String nroDocumento, int paginaAMostrar, int tamanyoPagina, CriterioOrdenPasajeros ordenamiento) {

        boolean setParameterApellido = false;
        boolean setParameterNombre = false;
        boolean setParameterTipoDoc = false;
        boolean setParameterNroDoc = false;

        //*********  CONSTRUYENDO LA CONSULTA DE ACUERDO A LOS ARGUMENTOS QUE RECIBIÓ EL MÉTODO ******************

        StringBuilder subConsulta = new StringBuilder();
        subConsulta.append("from Pasajero where");

        if (nombre != "") {

            subConsulta.append(" nombre like :nom");
            setParameterNombre = true;

        }

        if (apellido != "") {

            subConsulta.append(" and apellido like :ape");
            setParameterApellido = true;

        }

        if (tipoDocumento != "") {

            subConsulta.append(" and tipo_doc=:tdoc");
            setParameterTipoDoc = true;

        }

        if (nroDocumento != "") {

            subConsulta.append(" and nro_doc=:ndoc");
            setParameterNroDoc = true;

        }

        String consulta;

        if (subConsulta.toString().equals("from Pasajero where")) {

            consulta = "from Pasajero";

        } else {

            consulta  = subConsulta.toString().replaceAll("where and", "where");

        }

        String consultaQueCuenta = "Select count (id) " + consulta;

        switch (ordenamiento) {
            case APELLIDO_ZA:
                consulta = consulta + " order by apellido desc";
                break;
            case NRO_DOC_1_9:
                consulta = consulta + " order by nro_doc";
                break;
            case NRO_DOC_9_1:
                consulta = consulta + " order by nro_doc desc";
                break;
            default:
                consulta = consulta + " order by apellido";

        }

        iniciarSesionHibernate();

        Query query = sesionHibernate.createQuery(consulta);
        Query queryQueCuenta = sesionHibernate.createQuery(consultaQueCuenta);

        if (setParameterNombre) {
            query.setParameter("nom", nombre + "%");
            queryQueCuenta.setParameter("nom", nombre + "%");
        }
        if (setParameterApellido) {
            query.setParameter("ape", apellido + "%");
            queryQueCuenta.setParameter("ape", apellido + "%");
        }
        if (setParameterTipoDoc) {
            query.setParameter("tdoc", tipoDocumento.toString());
            queryQueCuenta.setParameter("tdoc", tipoDocumento.toString());
        }
        if (setParameterNroDoc) {
            query.setParameter("ndoc", nroDocumento);
            queryQueCuenta.setParameter("ndoc", nroDocumento);
        }

        //****************************** FIN DE CONSTRUCCIÓN DE LA CONSULTA *****************************************

        long cantResultados = (long) queryQueCuenta.uniqueResult();
        int cantPaginas = (int) (cantResultados / tamanyoPagina) + 1;

        if (paginaAMostrar >= cantPaginas) {
            return new ListaPaginada<>();
        }

        if (paginaAMostrar < 0) {
            paginaAMostrar = cantPaginas - 1;
        }

        int posPrimerElemento = (paginaAMostrar) * tamanyoPagina;

        query.setFirstResult(posPrimerElemento);
        query.setMaxResults(tamanyoPagina);

        ListaPaginada<Pasajero> pasajerosLeidos = new ListaPaginada<>();

        pasajerosLeidos.setLista( query.getResultList() );
        pasajerosLeidos.setCantElementosPorPagina(tamanyoPagina);
        pasajerosLeidos.setCantPaginas(cantPaginas);
        pasajerosLeidos.setPaginaActual(paginaAMostrar);

        cerrarSesionHibernate();

        return pasajerosLeidos;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long actualizarPasajero(Pasajero pasajero) {
        return insertarPasajero(pasajero);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long insertarPasajero(Pasajero pasajero) {

        iniciarSesionHibernate();
        sesionHibernate.beginTransaction();
        sesionHibernate.saveOrUpdate(pasajero);
        sesionHibernate.getTransaction().commit();
        cerrarSesionHibernate();

        return pasajero.getId();
    }

}

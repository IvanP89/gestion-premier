package org.gestionpremier.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Clase abstracta que define los métodos para manipular una sesión de Hibernate.
 * De esta clase deberían heredar todos los DAOs que trabajen con Hibernate. Su propósito es simplificar la integración
 * entre Hibernate y los DAOs y evitar la escritura excesiva de código repetido.
 * Logra que el uso de la sesión de Hibernate por parte de los DAOs se reduzca a llamar a iniciarSesion() al principio
 * de un método y cerrarSesionHibernate() al finalizar el mismo, quedando a disposición entre medio de forma automática
 * el objeto de sesión de Hibernate para ser utilizado sin ninguna otra intervención previa o posterior.
 *
 * @see HibernateSessionFactory
 */

public abstract class HibernateDAO {

    /**
     * Objeto de sesión de Hibernate a través del cual se realizan todas las operaciones del ORM.
     */
    protected Session sesionHibernate = null;

    /**
     * Instancia el objeto de sesión de Hibernate.
     *
     * @throws HibernateException   si hay un problema con la conexión con la base de datos, o si la estructura de la
     *                              base de datos no es acorde al mapeado especificado en las clases de entidad.
     */
    protected void iniciarSesionHibernate() {

        try {
            sesionHibernate = HibernateSessionFactory.getInstancia().openSession();
        } catch (NullPointerException e){
            throw new HibernateException("");
        }
    }

    /**
     * Cierra la sesión de Hibernate.
     */
    protected void cerrarSesionHibernate() {

        sesionHibernate.close();

    }

}
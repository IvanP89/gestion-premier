package org.gestionpremier.dao;

/**
 * Clase que se encarga de cerrar las conexiones con los sistemas de persistencia de datos.
 * Debería ser utilizada antes de cerrar el programa.
 */

public abstract  class Cleaner {

    /**
     * Cierra las conexiones abiertas por las clases que trabajan con los sistemas de persistencia.
     */
    public static void cerrarConexiones() {

        cerrarConexionesDB();
    }

    /**
     * Cierra las conexiones utilizadas por Hibernate.
     */
    private static void cerrarConexionesDB() {

        HibernateSessionFactory.cerrar();

    }

}

package org.gestionpremier.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Instancia el Factory del objeto de sesión de Hibernate.
 * <p>
 * Hibernate se usa fundamentalmente mediante los métodos de un objeto de la clase Session, que se instancia a través
 * de un objeto de la clase SessionFactory.
 * <p>
 * Los objetos Session son descartables, se instancian, se usan para una operación, se cierran y de descartan.
 * <p>
 * Sin embargo el objeto SessionFactory es uno solo, se mantiene durante todo el tiempo q esté abierto el programa y
 * antes de cerrarse el mismo hay que llamar a su método close() para cerrar sus conexiones/finalizar sus operaciones.
 * Además, es usado por todos los DAOs y tiene que poder ser referenciado globalmente.
 * <p>
 * Por esto mismo se implementa como un Singleton con esta clase.
 *
 * @see HibernateDAO
 */

public class HibernateSessionFactory {

    /**
     * Única instancia de la Factory del objeto de sesión de Hibernate.
     */
    private static SessionFactory hibernateSessionFactory = null;

    /**
     * Constructor por defecto, privado por la implementación del patrón singleton.
     */
    private HibernateSessionFactory() {

    }

    /**
     * Obtiene la única instancia de la Factory del objeto de sesión de Hibernate.
     * Método implementado por la utilización del patrón singleton.
     *
     * @return  una instancia del factory del objeto de sesión de Hibernate.
     */
    public static SessionFactory getInstancia() {

        if (hibernateSessionFactory == null) {

            final StandardServiceRegistry registro = new StandardServiceRegistryBuilder()
                    .configure() // Setea las configuraciones desde el archivo hibernate.cfg.xml
                    .build();

            try {
                hibernateSessionFactory = new MetadataSources(registro).buildMetadata().buildSessionFactory();
            } catch (Exception ex) {
                StandardServiceRegistryBuilder.destroy(registro);
                System.out.println("Falló el build del nuevo de HibernateSessionFactory: " + ex);
            }

        }

        return hibernateSessionFactory;

    }

    /**
     * Cierra las conexiones del Factory del objeto de sesión de Hibernate.
     */
    public static void cerrar() {

        if (hibernateSessionFactory != null) {

            hibernateSessionFactory.close();

        }

    }

}

package org.gestionpremier.utilidades;

/**
 * CriterioOrdenPasajeros es un enum usado por el DAO de la entidad Pasajero.
 * En los métodos que retornan una lista de pasajeros, este enum puede ser recibido como argumento y usado dentro
 * de un switch para agregar un orden a la Query de consulta a la base de datos.
 */

public enum CriterioOrdenPasajeros {

    APELLIDO_AZ,
    APELLIDO_ZA,
    NRO_DOC_1_9,
    NRO_DOC_9_1

}

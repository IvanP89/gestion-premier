package org.gestionpremier.negocio.entidades;

import javax.persistence.Entity;

@Entity
public class UsuarioAdmin extends Usuario {

    public UsuarioAdmin() {

        setEsAdmin(true);

    }

}
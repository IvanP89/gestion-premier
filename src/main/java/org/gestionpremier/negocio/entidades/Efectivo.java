package org.gestionpremier.negocio.entidades;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Table(name = "efectivo")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Efectivo extends MedioDePago {



}
package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.TipoHabitacion;
import java.util.List;

public class EstadoHabitacionDTO {

    /**************************************************/
    /**                ATRIBUTOS                     **/
    /**************************************************/

    private Long idHab;
    private String nroHab;
    private TipoHabitacion tipoHabitacion;
    private List<EstadoHabitacionDiaDTO> estadosHabitacionXDia;

    /**************************************************/
    /**              GETTERS Y SETTERS               **/
    /**************************************************/

    public Long getIdHab() {
        return idHab;
    }

    public void setIdHab(Long idHab) {
        this.idHab = idHab;
    }

    public String getNroHab() {
        return nroHab;
    }

    public void setNroHab(String nroHab) {
        this.nroHab = nroHab;
    }

    public List<EstadoHabitacionDiaDTO> getEstadosHabitacionXDia() {
        return estadosHabitacionXDia;
    }

    public void setEstadosHabitacionXDia(List<EstadoHabitacionDiaDTO> estadosHabitacionXDia) {
        this.estadosHabitacionXDia = estadosHabitacionXDia;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }
}
package org.gestionpremier.negocio.gestores;

import org.gestionpremier.dao.habitacion.HabitacionDAO;
import org.gestionpremier.dao.habitacion.HabitacionFactoryDAO;
import org.gestionpremier.dao.reserva.ReservaDAO;
import org.gestionpremier.dto.*;
import org.gestionpremier.excepciones.ObjetoNoEncontradoException;
import org.gestionpremier.excepciones.RangoDeFechaErroneoException;
import org.gestionpremier.negocio.entidades.Reserva;
import org.gestionpremier.dao.reserva.ReservaFactoryDAO;
import org.gestionpremier.dto.*;
import org.gestionpremier.negocio.entidades.EstadoHabitacion;
import org.gestionpremier.negocio.entidades.Habitacion;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GestorHabitacion se encarga de la lógica de negocio que involucra a las clases de entidad que representan
 * a las habitaciones del hotel, las reservas de estas habitaciones y las personas titulares de estas reservas.
 * Dichas entidades son: Habitacion, Reserva y TitularReserva.
 * <p>
 * Se asume que las Habitaciones ya existen en la base de datos, no son creadas o borradas por el programa, sólo se
 * actualizan.
 * <p>
 * Clase Singleton.
 */

public class GestorHabitacion {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Instancia única del gestor según el patrón Singleton.
     */
    private static GestorHabitacion instancia;

    //******************************************  CONSTRUCTORES  ****************************************************

    /**
     * Constructor por defecto. Privado por implementarse el patrón Singleton.
     */
    private GestorHabitacion() {}

    /**
     * Método para obtener la única instancia de la clase por implementación del patrón Singleton.
     *
     * @return la única instancia de la clase GestorHabitacion.
     */
    public static GestorHabitacion obtenerInstancia() {

        if (instancia == null) {
            instancia = new GestorHabitacion();
        }

        return instancia;

    }

    //***********************************************  DAOs  ********************************************************

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de la clase Habitacion.
     *
     * @return  una instancia del DAO de Habitacion.
     */
    public HabitacionDAO getHabitacionDAO(){

        HabitacionFactoryDAO factoryHabitacion = HabitacionFactoryDAO.getFactoryDAO(HabitacionFactoryDAO.POSTGRESQL);
        HabitacionDAO habitacionDAO = factoryHabitacion.getHabitacionDAO();

        return habitacionDAO;

    }

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de las clases Reserva y TitularReserva.
     *
     * @return  una instancia del DAO de Reserva y TitularReserva.
     */
    public ReservaDAO getReservaDAO(){

        ReservaFactoryDAO factoryReserva = ReservaFactoryDAO.getFactoryDAO(ReservaFactoryDAO.POSTGRESQL);
        ReservaDAO reservaDAO = factoryReserva.getReservaDAO();

        return reservaDAO;

    }

    //*********************************************  GETTERS  *******************************************************

    /**
     * Genera una lista de objetos que representan el estado de todas las habitaciones en un período de tiempo determinado.
     * Cada objeto de la lista corresponde a una única habitación. Dentro de cada uno de estos objetos se encuentran los
     * estados de esa habitación para un período determinado de tiempo.
     *
     * @param fechaDesde    la fecha desde la cual se desean conocer los estados de las habitaciones.
     * @param fechaHasta    la fecha hasta la cual se desean conocer los estados de las habitaciones.
     * @return              una lista con los estados de todas las habitaciones en un período de tiempo. Cada objeto de
     *                      esta lista corresponde a una única habitación y dentro de cada uno hay otra lista con los
     *                      objetos que representan los estados de esa habitación para cada uno de los días del
     *                      rango de tiempo especificado. Si no se encuentran habitaciones, retorna una lista vacía.
     * @throws RangoDeFechaErroneoException si las fechas de inicio o fin no son válidas.
     * @see    EstadoHabitacionDiaDTO
     */
    public List<EstadoHabitacionDTO> getEstadoHabitaciones(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws RangoDeFechaErroneoException {

        GestorEstadia gestorEstadia = GestorEstadia.obtenerInstancia();

        if(!this.validarRangoFechas(fechaDesde, fechaHasta)){
            throw new RangoDeFechaErroneoException("Las fechas ingresadas no son válidas.");
        }

        List<Habitacion> habitaciones = this.getAllHabitaciones();

        List<Reserva> reservas = this.getReservas(fechaDesde, fechaHasta);

        List<EstadiaDTO> estadiasDTO;
        try {

            estadiasDTO = gestorEstadia.getEstadias(fechaDesde, fechaHasta);

        } catch (ObjetoNoEncontradoException e) {
            estadiasDTO = new ArrayList<>();
        }

        return this.generarEstadoHabitacionesDTO(reservas, estadiasDTO, habitaciones, fechaDesde, fechaHasta);

    }

    /**
     * Busca una Habitacion en la base de datos, la instancia y retorna su DTO.
     *
     * @param idHabitacion      el id de la Habitación buscada.
     * @return                  un DTO con los datos correspondientes a la Habitación buscada.
     * @throws ObjetoNoEncontradoException  si la búsqueda en la base de datos de la Habitacion con el id especificado
     *                                      no arroja ningún resultado.
     */
    public HabitacionDTO getHabitacion(Long idHabitacion) throws ObjetoNoEncontradoException {

        HabitacionDAO habitacionDAO = getHabitacionDAO();

        Habitacion habitacion = habitacionDAO.getHabitacion(idHabitacion);

        if (habitacion == null) {
            throw new ObjetoNoEncontradoException("La habitación con el id " + idHabitacion + " no se encuentra en la base de datos.");
        }

        return generarHabitacionDTO(habitacion);

    }

    /**
     * Busca una Habitacion en la base de datos, la instancia y retorna su DTO.
     *
     * @param numero    el número de la habitación buscada (el número NO es el id).
     * @return          un DTO con los datos correspondientes a la Habitación buscada.
     * @throws ObjetoNoEncontradoException  si la búsqueda en la base de datos de la Habitacion con el nro especificado
     *                                      no arroja ningún resultado.
     */
    public HabitacionDTO getHabitacion(String numero) throws ObjetoNoEncontradoException {

        HabitacionDAO habitacionDAO = getHabitacionDAO();

        Habitacion habitacion = habitacionDAO.getHabitacion(numero).stream().findFirst().orElse(null);

        if (habitacion == null) {
            throw new ObjetoNoEncontradoException("La habitación con el numero " + numero + " no se encuentra en la base de datos.");
        }

        return generarHabitacionDTO(habitacion);

    }

    /**
     * Busca una Habitacion en la base de datos, la instancia y retorna un objeto entidad.
     *
     * @param idHabitacion      el id de la Habitación buscada.
     * @return                  la instancia de la clase entidad de la Habitacion buscada.
     * @throws ObjetoNoEncontradoException  si la búsqueda en la base de datos de la Habitacion con el id especificado
     *                                      no arroja ningún resultado.
     */
    protected Habitacion getHabitacionEntidad(Long idHabitacion) throws ObjetoNoEncontradoException {

        HabitacionDAO habitacionDAO = getHabitacionDAO();

        Habitacion habitacion = habitacionDAO.getHabitacion(idHabitacion);

        if (habitacion == null) {
            throw new ObjetoNoEncontradoException("La habitación con el id " + idHabitacion + " no se encuentra en la base de datos.");
        }

        return habitacion;

    }

    /**
     * Levanta todas las habitaciones de la base de datos y retorna las instancias de sus entidades.
     *
     * @return      una lista con las entidades de todas las Habitaciones. Una lista vacía si no encuentra ninguna
     *              Habitación.
     */
    public List<Habitacion> getAllHabitaciones(){

        HabitacionDAO habitacionDAO = this.getHabitacionDAO();

        List<Habitacion> listaResultado = habitacionDAO.getHabitaciones();

        return listaResultado;

    }

    /**
     * Busca las reservas correspondientes a un período de tiempo para todas las habitaciones.
     * El criterio de búsqueda es la existencia de una superposición en cualquier punto del rango de fechas recibido
     * y el rango de fechas de las reservas.
     *
     * @param fechaDesde    la fecha desde la cual se desean conocer las reservas de las habitaciones.
     * @param fechaHasta    la fecha hasta la cual se desean conocer las reservas de las habitaciones.
     * @return              una lista con las reservas para todas las habitaciones encontradas para el período de
     *                      tiempo especificado o una lista vacía si no se encontró ninguna reserva para ese período.
     */
    public List<Reserva> getReservas(LocalDateTime fechaDesde, LocalDateTime fechaHasta){

        ReservaDAO reservaDAO = getReservaDAO();

        List<Reserva> reservas = reservaDAO.getReservas(fechaDesde, fechaHasta);

        return reservas;

    }

    /**
     * Busca una reserva en la base de datos y retorna su DTO.
     *
     * @param idReserva     el id de la reserva buscada.
     * @return              el DTO con los datos de la reserva buscada.
     * @throws ObjetoNoEncontradoException  si la búsqueda en la base de datos de la Reserva con el id especificado
     *                                      no arroja ningún resultado.
     */
    public ReservaDTO getReserva(long idReserva) throws ObjetoNoEncontradoException {

        Reserva reservaEntidad = this.getReservaEntidad(idReserva);

        ReservaDTO reservaDTO = new ReservaDTO();
        EstadoHabitacionDTO estadoHabitacionDTO = new EstadoHabitacionDTO();
        TitularReservaDTO titularReservaDTO = new TitularReservaDTO();

        estadoHabitacionDTO.setIdHab(reservaEntidad.getHabitacion().getId());
        estadoHabitacionDTO.setNroHab(reservaEntidad.getHabitacion().getNro());
        estadoHabitacionDTO.setTipoHabitacion(reservaEntidad.getHabitacion().getTipo());
        estadoHabitacionDTO.setEstadosHabitacionXDia(null);

        titularReservaDTO.setApellido(reservaEntidad.getTitular().getApellido());
        titularReservaDTO.setNombre(reservaEntidad.getTitular().getNombre());
        titularReservaDTO.setTelefono(reservaEntidad.getTitular().getTelefono());

        reservaDTO.setFechaDesde(reservaEntidad.getFechaDesde());
        reservaDTO.setFechaHasta(reservaEntidad.getFechaHasta());
        reservaDTO.setHabitacion(estadoHabitacionDTO);
        reservaDTO.setTitular(titularReservaDTO);

        return reservaDTO;

    }

    /**
     * Busca una reserva en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param idReserva     el id de la reserva buscada.
     * @return              el objeto entidad correspondiente a la reserva buscada.
     * @throws ObjetoNoEncontradoException  si la búsqueda en la base de datos de la Reserva con el id especificado
     *                                      no arroja ningún resultado.
     */
    protected Reserva getReservaEntidad(long idReserva) throws ObjetoNoEncontradoException {

        ReservaDAO reservaDAO = getReservaDAO();

        Reserva reserva = reservaDAO.getReserva(idReserva);

        if (reserva == null){

            throw new ObjetoNoEncontradoException("La reserva con el id " + idReserva + " no se encuentra en la base de datos.");

        }

        return reserva;

    }

    //************************************  TRANSFORMACIÓN A/DESDE DTOs  *********************************************

    /**
     * Genera el DTO correspondiente a una reserva a partir del objeto entidad de la misma.
     *
     * @param reservaEntidad    el objeto entidad del cual se desea generar el DTO.
     * @return                  el DTO del objeto entidad.
     */
    private ReservaDTO generarReservaDTO(Reserva reservaEntidad){

        ReservaDTO reservaDTO = new ReservaDTO();
        EstadoHabitacionDTO estadoHabitacionDTO = new EstadoHabitacionDTO();
        TitularReservaDTO titularReservaDTO = new TitularReservaDTO();

        estadoHabitacionDTO.setIdHab(reservaEntidad.getHabitacion().getId());
        estadoHabitacionDTO.setNroHab(reservaEntidad.getHabitacion().getNro());
        estadoHabitacionDTO.setTipoHabitacion(reservaEntidad.getHabitacion().getTipo());
        estadoHabitacionDTO.setEstadosHabitacionXDia(null);

        titularReservaDTO.setApellido(reservaEntidad.getTitular().getApellido());
        titularReservaDTO.setNombre(reservaEntidad.getTitular().getNombre());
        titularReservaDTO.setTelefono(reservaEntidad.getTitular().getTelefono());

        reservaDTO.setFechaDesde(reservaEntidad.getFechaDesde());
        reservaDTO.setFechaHasta(reservaEntidad.getFechaHasta());
        reservaDTO.setHabitacion(estadoHabitacionDTO);
        reservaDTO.setTitular(titularReservaDTO);

        return reservaDTO;
    }

    /**
     * Genera una lista con los estados de las habitaciones recibidas durante el período de tiempo indicado.
     * Cada objeto de la lista corresponde a una única habitación. Dentro de cada uno de estos objetos se encuentran los
     * estados de esa habitación para un período determinado de tiempo. Estos estados se generan a partir de las
     * estadías y las reservas que se recibieron como argumento.
     *
     * @param reservas      las reservas existentes para el período de tiempo indicado.
     * @param estadias      las estadías existentes para el período de tiempo indicado.
     * @param habitaciones  las habitaciones para las cuales se desea conocer sus estados.
     * @param fechaDesde    la fecha desde la cual se desea conocer los estados de las habitaciones.
     * @param fechaHasta    la fecha hasta la cual se desea conocer los estados de las habitaciones.
     * @return              una lista con los estados de las habitaciones en el período de tiempo indicado. Cada objeto
     *                      de esta lista corresponde a una única habitación y dentro de cada uno hay otra lista con los
     *                      objetos que representan los estados de esa habitación para cada uno de los días del
     *                      rango de tiempo especificado.
     */
    public List<EstadoHabitacionDTO> generarEstadoHabitacionesDTO(List<Reserva> reservas, List<EstadiaDTO> estadias, List<Habitacion> habitaciones, LocalDateTime fechaDesde, LocalDateTime fechaHasta){

        List<EstadoHabitacionDTO> habitacionesDTO = new ArrayList<>();

        for (Habitacion h: habitaciones) {

            EstadoHabitacionDTO estadoHabitacionDTO = new EstadoHabitacionDTO();

            estadoHabitacionDTO.setNroHab(h.getNro());
            estadoHabitacionDTO.setIdHab(h.getId());
            estadoHabitacionDTO.setEstadosHabitacionXDia(this.generarEstadoDiarioHabitacion(reservas, estadias, h.getId(), fechaDesde, fechaHasta));
            estadoHabitacionDTO.setTipoHabitacion(h.getTipo());

            habitacionesDTO.add(estadoHabitacionDTO);
        }

        return habitacionesDTO;

    }

    /**
     * Genera una lista con los estados de una habitación en particular durante el período de tiempo indicado.
     * Cada objeto de la lista corresponde a cada uno de los días en el período indicado.
     *
     * @param reservas      las reservas existentes para el período de tiempo indicado. Estas reservas pueden
     *                      corresponder a todas las habitaciones o a la habitación en particular con la que se está
     *                      trabajando.
     * @param estadias      las estadías existentes para el período de tiempo indicado. Estas estadías pueden
     *                      corresponder a todas las habitaciones o a la habitación en particular con la que se está
     *                      trabajando.
     * @param idHabitacion  el id de la habitación para la cual desean conocerse los estados.
     * @param fechaDesde    la fecha desde la cual se desea conocer los estados de la Habitacion.
     * @param fechaHasta    la fecha hasta la cual se desea conocer los estados de la Habitacion.
     * @return              una lista con los estados de la Habitacion para el período de tiempo indicado. Los objetos
     *                      de esta lista corresponden a cada uno de los días dentro del período de tiempo indicado y
     *                      contienen los estados de la Habitacion en esos días en particular. El largo de la lista
     *                      siempre será igual a la cantidad de días en el período de tiempo indicado.
     * @see     EstadoHabitacionDTO
     */
    public List<EstadoHabitacionDiaDTO> generarEstadoDiarioHabitacion(List<Reserva> reservas, List<EstadiaDTO> estadias, Long idHabitacion, LocalDateTime fechaDesde, LocalDateTime fechaHasta){

        List<EstadoHabitacionDiaDTO> estadosHabitacionPorDia = new ArrayList<>();
        List<Reserva> reservasDeEstaHabitacion = new ArrayList<>();
        List<EstadiaDTO> estadiasDeEstaHabitacion = new ArrayList<>();

        Duration duracion = Duration.between(fechaDesde, fechaHasta);
        Long diferencia = Math.abs(duracion.toDays()) + 1;

        for(Reserva r: reservas){

            if(r.getHabitacion().getId() == idHabitacion){
                reservasDeEstaHabitacion.add(r);
            }
        }

        for(EstadiaDTO e: estadias){

            if(e.getHabitacionDTO().getId() == idHabitacion){
                estadiasDeEstaHabitacion.add(e);
            }
        }

        for (int i=0; i<diferencia; i++){

            EstadoHabitacionDiaDTO estadoHabitacionDiaDTO = new EstadoHabitacionDiaDTO();

            LocalDateTime fechaDeAnalisis = fechaDesde.plusDays(i);

            estadoHabitacionDiaDTO.setIdHabitacion(idHabitacion);
            estadoHabitacionDiaDTO.setFecha(fechaDeAnalisis);
            estadoHabitacionDiaDTO.setEstado(EstadoHabitacion.DESOCUPADA);
            estadoHabitacionDiaDTO.setIdReserva(-1);
            estadoHabitacionDiaDTO.setIdEstadia(-1);

            for(Reserva r: reservasDeEstaHabitacion){

                if((r.getFechaDesde().isBefore(fechaDeAnalisis) || r.getFechaDesde().isEqual(fechaDeAnalisis)) &&
                        (r.getFechaHasta().isAfter(fechaDeAnalisis) || r.getFechaHasta().isEqual(fechaDeAnalisis))){

                    if (r.getEsMantenimiento()){
                        estadoHabitacionDiaDTO.setEstado(EstadoHabitacion.EN_MANTENIMIENTO);
                    }
                    else{
                        estadoHabitacionDiaDTO.setEstado(EstadoHabitacion.RESERVADA);
                        estadoHabitacionDiaDTO.setIdReserva(r.getId());
                    }
                }
            }

            for(EstadiaDTO e: estadiasDeEstaHabitacion){

                if((e.getFechaDesde().isBefore(fechaDeAnalisis) || e.getFechaDesde().isEqual(fechaDeAnalisis)) &&
                        (e.getFechaHasta().isAfter(fechaDeAnalisis) || e.getFechaHasta().isEqual(fechaDeAnalisis))){

                    estadoHabitacionDiaDTO.setEstado(EstadoHabitacion.OCUPADA);
                    estadoHabitacionDiaDTO.setIdEstadia(e.getId());

                }
            }

            estadosHabitacionPorDia.add(estadoHabitacionDiaDTO);
        }

        return estadosHabitacionPorDia;
    }

    /**
     * Genera el DTO correspondiente a la entidad de una Habitacion.
     *
     * @param unaHabitacion     el objeto entidad para el cual se desea generar el DTO.
     * @return                  el DTO con los datos del objeto entidad.
     */
    public HabitacionDTO generarHabitacionDTO(Habitacion unaHabitacion) {

        if (unaHabitacion == null) {
            return null;
        }

        HabitacionDTO unaHabitacionDTO = new HabitacionDTO();

        unaHabitacionDTO.setId(unaHabitacion.getId());
        unaHabitacionDTO.setNro(unaHabitacion.getNro());
        unaHabitacionDTO.setTipo(unaHabitacion.getTipo());
        unaHabitacionDTO.setEstado(unaHabitacion.getEstado());
        unaHabitacionDTO.setFechaEstado(unaHabitacion.getFechaEstado());

        return unaHabitacionDTO;

    }

    //****************************************  VALIDACIONES  ********************************************************

    /**
     * Valida las fechas inicial y final de un período de tiempo.
     *
     * @param fechaDesde    la fecha inicial del rango.
     * @param fechaHasta    la fecha final del rango.
     * @return              <code>false</code> si la fecha inicial es posterior a la fecha final, <code>true</code>
     *                      en el caso contrario.
     */
    public Boolean validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta){

        if (fechaDesde.isBefore(fechaHasta) || fechaDesde.isEqual(fechaHasta)){
            return true;
        }
        else{
            return false;
        }
    }

}

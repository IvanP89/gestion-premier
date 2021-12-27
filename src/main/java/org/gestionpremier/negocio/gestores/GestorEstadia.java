package org.gestionpremier.negocio.gestores;

import org.gestionpremier.dao.consumo.ConsumoDAO;
import org.gestionpremier.dao.consumo.ConsumoFactoryDAO;
import org.gestionpremier.dao.estadia.EstadiaDAO;
import org.gestionpremier.dao.estadia.EstadiaFactoryDAO;
import org.gestionpremier.dto.ConsumoDTO;
import org.gestionpremier.dto.EstadiaDTO;
import org.gestionpremier.dto.HabitacionDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.excepciones.IdErroneoException;
import org.gestionpremier.excepciones.ObjetoNoEncontradoException;
import org.gestionpremier.excepciones.RangoDeFechaErroneoException;
import org.gestionpremier.negocio.entidades.*;
import org.gestionpremier.negocio.entidades.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * GestorEstadia se encarga de la lógica de negocio que involucra a las clases de entidad que representan
 * a las estadías del hotel y los consumos de dichas estadías.
 * Dichas entidades son: Estadia y Consumo.
 * <p>
 * Además, actualiza los estados de los objetos Habitacion relacionados a las estadías.
 * <p>
 * Clase Singleton.
 */

public class GestorEstadia {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Instancia única del gestor según el patrón Singleton.
     */
    private static GestorEstadia instancia;

    //******************************************  CONSTRUCTORES  ****************************************************

    /**
     * Constructor por defecto. Privado por implementarse el patrón Singleton.
     */
    private GestorEstadia() {}

    /**
     * Obtiene la única instancia de la clase por implementación del patrón Singleton.
     *
     * @return la única instancia de la clase GestorEstadia.
     */
    public static GestorEstadia obtenerInstancia() {

        if (instancia == null) {
            instancia = new GestorEstadia();
        }

        return instancia;

    }

    //***********************************************  DAOs  ********************************************************

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de la clase Estadia.
     *
     * @return  una instancia del DAO de Estadia.
     */
    private EstadiaDAO getEstadiaDAO() {

        EstadiaFactoryDAO factoryEstadia = EstadiaFactoryDAO.getFactoryDAO(EstadiaFactoryDAO.POSTGRESQL);

        EstadiaDAO estadiaDAO = factoryEstadia.getEstadiaDAO();

        return estadiaDAO;

    }

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de la clase Consumo.
     *
     * @return  una instancia del DAO de Consumo.
     */
    private ConsumoDAO getConsumoDAO() {

        ConsumoFactoryDAO factoryDAO = ConsumoFactoryDAO.getFactoryDAO(ConsumoFactoryDAO.POSTGRESQL);

        ConsumoDAO consumoDAO = factoryDAO.getConsumoDAO();

        return consumoDAO;

    }

    //****************************************  INSERTS / UPDATES  **************************************************

    /**
     * Realiza el checkout de una estadía.
     * Setea la fecha y hora de salida, genera el consumo del hospedaje, calcula el costo total, desocupa la habitación
     * y actualiza la estadía y la habitación en la base de datos.
     *
     * @param idEstadia     la estadía a finalizar.
     * @param tiempo        la hora de checkout.
     * @throws IdErroneoException   si el id recibido como argumento no es válido.
     * @throws ObjetoNoEncontradoException  si no se encuentra una estadía con el id especificado en la base de datos.
     */
    public void finalizarEstadia(long idEstadia, LocalTime tiempo) throws IdErroneoException, ObjetoNoEncontradoException {

        if (!validarId(idEstadia)) {
            throw new IdErroneoException("El id de la estadía es incorrecto.");
        }

        EstadiaDAO estadiaDAO = this.getEstadiaDAO();

        Estadia unaEstadia = estadiaDAO.getEstadia(idEstadia);

        if (unaEstadia == null) {
            throw new ObjetoNoEncontradoException("La estadía buscada no se encuentra en la base de datos.");
        }

        if(unaEstadia.getHabitacion().getEstado()== EstadoHabitacion.OCUPADA) {

            calcularEstadia(unaEstadia, tiempo);

            unaEstadia.setConsumos(getConsumosEstadiaEntidad(unaEstadia.getId()));

            setearTotalEstadia(unaEstadia);

            unaEstadia.getHabitacion().setEstado(EstadoHabitacion.DESOCUPADA);
            unaEstadia.getHabitacion().setFechaEstado(LocalDateTime.now());
            unaEstadia.setFechaHasta(LocalDateTime.of(LocalDate.now(), tiempo));

            estadiaDAO.actualizarEstadia(unaEstadia);

        }

    }

    /**
     * Calcula el costo de hospedaje y genera el consumo del mismo.
     * El costo del hospedaje, con sus correspondientes recargos si aplicaran, se calcula y se setea a la estadía. Luego
     * se genera el consumo correspondiente al hospedaje y se guarda en la base de datos.
     *
     * @param unaEstadia    la estadia a calcular el hospedaje.
     * @param tiempo        la hora de checkout.
     * @return              el importe total del hospedaje
     */
    private float calcularEstadia(Estadia unaEstadia, LocalTime tiempo) {

        float precioPorNoche = getPrecioPorNoche(unaEstadia.getHabitacion().getTipo());

        int cantDias = calcularCantidadDias(unaEstadia.getFechaDesde(), LocalDateTime.of(LocalDate.now(), tiempo));

        String detalle = "Estadia " + unaEstadia.getHabitacion().getTipo().toString() + " por " + cantDias + " días";

        if(!(unaEstadia.getFechaDesde().toLocalDate().isEqual(LocalDate.now()))) {
            if (tiempo.getHour() < 11) {

                unaEstadia.setImporte(precioPorNoche * (float) cantDias);

            } else if (tiempo.getHour() <= 18) {

                unaEstadia.setImporte(precioPorNoche * ((float) cantDias + 0.5f));
                detalle = detalle + ", + recargo por extensión.";

            } else {

                unaEstadia.setImporte(precioPorNoche * ((float) cantDias + 1.0f));
                detalle = detalle + ", + día de recargo.";

            }
        } else {

            unaEstadia.setImporte(precioPorNoche);

        }
        generarConsumoEstadia(detalle, unaEstadia);

        return unaEstadia.getImporte();

    }

    /**
     * Crea el objeto consumo correspondiente al hospedaje y lo guarda.
     *
     * @param detalle   el detalle del consumo a generar.
     * @param estadia   la estadía para la cual se va a crear el consumo correspondiente al hospedaje.
     * @return          el id del consumo de la estadía creado.
     */
    private long generarConsumoEstadia(String detalle, Estadia estadia) {

        Consumo consumoEstadia = new Consumo();

        consumoEstadia.setEstadia(estadia);
        consumoEstadia.setImporteUnitario(estadia.getImporte());
        consumoEstadia.setCantidad(1);
        consumoEstadia.setDetalle(detalle);
        consumoEstadia.setFecha(estadia.getFechaHasta());
        consumoEstadia.setTipo(TipoServicio.ESTADIA);
        consumoEstadia.setCantidadFacturada(0);

        ConsumoDAO consumoDAO = this.getConsumoDAO();

        return consumoDAO.insertarConsumo(consumoEstadia);

    }

    /**
     * Inserta una nueva Estadia en la base de datos.
     *
     * @param estadiaDTO    el DTO con los datos de la nueva Estadia.
     * @return              el id generado para la nueva Estadia, o <code>-1</code> si no se pudo insertar el nuevo
     *                      objeto.
     */
    public long crearEstadia(EstadiaDTO estadiaDTO) {

        if (validarEstadia(estadiaDTO)) {

            Estadia estadia = this.generarEstadia(estadiaDTO);

            if (estadia == null) {
                return -1;
            }

            estadiaDTO.setId(this.guardarEstadia(estadia));

            return estadiaDTO.getId();

        } else {

            return -1;

        }

    }

    /**
     * Inserta una nueva Estadia en la base de datos.
     *
     * @param estadia   el nuevo objeto entidad a insertar en la base de datos.
     * @return          el id generado para la nueva Estadia, o <code>-1</code> si no se pudo insertar el nuevo
     *                  objeto.
     */
    private long guardarEstadia(Estadia estadia) {

        EstadiaDAO estadiaDAO = this.getEstadiaDAO();

        return estadiaDAO.insertarEstadia(estadia);

    }

    /**
     * Actualiza una Estadia que ya existe en la base de datos.
     *
     * @param estadiaDTO                        el DTO con los datos de la Estadía a actualizar.
     * @throws IdErroneoException               si el argumento id del DTO no es válido.
     * @throws ObjetoNoEncontradoException      si no existe en la base de datos un objeto Estadía correspondiente al
     *                                          DTO.
     * @throws DatosErroneosPasajeroException   si hay un error en al menos uno de los campos del Pasajero asociado a la estadía.
     */
    public void actualizarEstadia( EstadiaDTO estadiaDTO ) throws IdErroneoException, ObjetoNoEncontradoException, DatosErroneosPasajeroException {

        GestorHabitacion gestorHabitacion = GestorHabitacion.obtenerInstancia();
        Habitacion habitacion = gestorHabitacion.getHabitacionEntidad(estadiaDTO.getHabitacionDTO().getId());

        GestorPersona gestorPersona = GestorPersona.obtenerInstancia();
        Pasajero pasajeroResponsable = gestorPersona.getPasajeroEntidad(estadiaDTO.getPasajeroResponsable().getId());

        List<Pasajero> pasajeros = new ArrayList<>();

        for(PasajeroDTO p: estadiaDTO.getPasajeros()){

            Pasajero pasajero = gestorPersona.getPasajeroEntidad(p.getId());
            pasajeros.add(pasajero);

        }

        Estadia estadiaEntidad = this.getEstadiaEntidad(estadiaDTO.getId());

        estadiaEntidad.setFechaDesde(estadiaDTO.getFechaDesde());
        estadiaEntidad.setFechaHasta(estadiaDTO.getFechaHasta());
        estadiaEntidad.setImporte(estadiaDTO.getImporte());
        estadiaEntidad.setDeuda(estadiaDTO.getDeuda());
        estadiaEntidad.setHabitacion(habitacion);
        estadiaEntidad.setResponsable(pasajeroResponsable);
        estadiaEntidad.setPasajeros(pasajeros);

        EstadiaDAO estadiaDAO = this.getEstadiaDAO();

        estadiaDAO.actualizarEstadia(estadiaEntidad);
    }

    /**
     * Actualiza una Estadia que ya existe en la base de datos.
     *
     * @param estadia   el objeto entidad a actualizar.
     */
    public void actualizarEstadia( Estadia estadia ) {

        EstadiaDAO estadiaDAO = this.getEstadiaDAO();

        estadiaDAO.actualizarEstadia(estadia);

    }

    /**
     * Reduce la deuda de una estadía como contraparte a la creación de una nueva Factura para la misma.
     * La "deuda" de una estadía se constituye por los consumos sin facturar. Luego de crear una nueva factura, este
     * método resta el total de los consumos facturados a la deuda de la estadía. Finalmente estos cambios
     * se guardan en la base de datos.
     *
     * @param idEstadia     el id de la estadía para la cual fue generada una factura.
     * @param monto         el monto facturado en la factura a partir de la cual se desea reducir la deuda.
     */
    public void reducirDeuda(long idEstadia, float monto) {

        try {

            Estadia estadia = this.getEstadiaEntidad(idEstadia);

            if (estadia != null) {

                estadia.setDeuda(estadia.getDeuda() - monto);

                if (estadia.getDeuda() < 1f) {
                    estadia.setDeuda(0);
                }

                this.actualizarEstadia(estadia);

            }


        } catch (IdErroneoException e) {}

    }

    //*********************************************  GETTERS  *******************************************************

    /**
     * Busca en la base de datos las Estadias cuyas fechas se superpongan con el rango de fechas especificado.
     *
     * @param fechaDesde                    la fecha inicial del rango en el que buscar las Estadias.
     * @param fechaHasta                    la fecha final del rangoa en el que buscar las Estadias.
     * @return                              una lista con los DTOs de las Estadías correspondientes al rango buscado.
     *                                      El criterio es que las fechas de las estadías se superpongan en cualquier
     *                                      punto con el rango buscado.
     * @throws RangoDeFechaErroneoException si las fechas recibidas no son válidas.
     * @throws ObjetoNoEncontradoException  si la búsqueda no arroja ningún resultado.
     */
    public List<EstadiaDTO> getEstadias(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws RangoDeFechaErroneoException, ObjetoNoEncontradoException {

        if (!validarRangoFechas(fechaDesde, fechaHasta)) {
            throw new RangoDeFechaErroneoException("Las fechas ingresadas no son válidas.");
        }

        EstadiaDAO estadiaDAO = this.getEstadiaDAO();

        List<Estadia> estadiasLeidas = estadiaDAO.getEstadias(fechaDesde, fechaHasta);

        if (estadiasLeidas.isEmpty()) {
            throw new ObjetoNoEncontradoException("No se han encontrado estadías en el rango de fechas especificado.");
        }

        List<EstadiaDTO> listaEstadiaDTO = new ArrayList<>();

        for (Estadia e:
                estadiasLeidas) {

            listaEstadiaDTO.add( generarEstadiaDTO(e) );

        }

        return listaEstadiaDTO;

    }

    /**
     * Busca una Estadia en la base de datos y retorna la instancia con la entidad correspondiente.
     *
     * @param idEstadia             el id de la Estadia a buscar.
     * @return                      la instancia del objeto entidad correspondiente a la Estadia buscada, o null si la
     *                              búsqueda no arroja resultados.
     * @throws IdErroneoException   si el id no es válido.
     */
    protected Estadia getEstadiaEntidad(Long idEstadia) throws IdErroneoException {

        if(this.validarId(idEstadia)) {
            EstadiaDAO estadiaDAO = this.getEstadiaDAO();

            Estadia estadia = estadiaDAO.getEstadia(idEstadia);

            return estadia;
        } else throw new IdErroneoException();

    }

    /**
     * Busca en la base de datos todos los Consumos asociados a una Estadia.
     *
     * @param idEstadia             el id de la Estadia de la cual se desea obtener los consumos.
     * @return                      una lista con los DTOs correspondientes a los consumos de la Estadia buscada. Si la
     *                              Estadia no tiene consumos, retorna una lista vacía.
     * @throws IdErroneoException   si el id no es válido.
     */
    public List<ConsumoDTO> getConsumosEstadia(long idEstadia) throws IdErroneoException {

        if(idEstadia>=0) {
            ConsumoDAO consumoDAO = ConsumoFactoryDAO.getFactoryDAO(ConsumoFactoryDAO.POSTGRESQL).getConsumoDAO();

            return generarConsumosDTO(consumoDAO.getConsumos(idEstadia));

        } else throw new IdErroneoException();

    }

    /**
     * Busca en la base de datos todos los Consumos asociados a una Estadia.
     *
     * @param idEstadia             el id de la estadía de la cual se desean obtener los consumos.
     * @return                      una lista con los consumos correspondientes a la estadía, o una lista vacía si la
     *                              búsqueda no arroja resultados.
     * @throws IdErroneoException   si el id no es válido.
     */
    protected List<Consumo> getConsumosEstadiaEntidad(long idEstadia) throws IdErroneoException {

        if(idEstadia>=0) {
            ConsumoDAO consumoDAO = ConsumoFactoryDAO.getFactoryDAO(ConsumoFactoryDAO.POSTGRESQL).getConsumoDAO();

            return consumoDAO.getConsumos(idEstadia);

        } else {
            throw new IdErroneoException("El id de la estadía de la cual se desean obtener los consumos no es válido.\n\nEn: GestorEstadia.getConsumosEstadiaEntidad");
        }

    }

    /**
     * Busca en la base de datos los Consumos asociados a una Estadia que no están facturados.
     *
     * @param idEstadia             el id de la Estadia de la cual se desea obtener los consumos.
     * @return                      una lista con los DTOs correspondientes a los consumos de la Estadia buscada que
     *                              no están pagos. Si laEstadia no tiene consumos, retorna una lista vacía.
     * @throws IdErroneoException   si el id no es válido.
     */
    public List<ConsumoDTO> getConsumosImpagosEstadia(long idEstadia) throws IdErroneoException {

        if(idEstadia>=0) {
            ConsumoDAO consumoDAO = ConsumoFactoryDAO.getFactoryDAO(ConsumoFactoryDAO.POSTGRESQL).getConsumoDAO();

            return generarConsumosDTO(consumoDAO.getConsumosImpagos(idEstadia));

        } else {
            throw new IdErroneoException();
        }

    }

    /**
     * Busca una Estadia en la base de datos y retorna su DTO.
     * @param idEstadia     el id de la Estadía a buscar.
     * @return              el DTO con los datos de la Estadia buscada, o null si la búsqueda no arroja resultados.
     */
    public EstadiaDTO getEstadia(long idEstadia){

        EstadiaDAO estadiaDAO = EstadiaFactoryDAO.getFactoryDAO(EstadiaFactoryDAO.POSTGRESQL).getEstadiaDAO();

        if(idEstadia>=0)
            return generarEstadiaDTO(estadiaDAO.getEstadia(idEstadia));
        else return null;

    }

    /**
     * Busca la Estadia más reciente de una Habitación que tenga Consumos sin facturar.
     *
     * @param idHabitacion  el id de la Habitacion para la cual se desea buscar la Estadia.
     * @return              la Estadia más reciente que tenga Consumos sin facturar asociada a la Habitacion. El
     *                      criterio para identificar una Estadia "que tenga Consumos sin facturar" es que
     *                      Estadia.deuda sea distinto de cero (hay consumos sin facturar) OR que Estadia.importe sea
     *                      igual a cero (la Estadia no está finalizada y entonces el importe no está seteado).
     *                      Si la búsqueda no arroja resultados, retorna <code>null</code>.
     */
    public EstadiaDTO getUltimaEstadiaImpagaYaEmpezada(long idHabitacion) {

        EstadiaDAO estadiaDAO = EstadiaFactoryDAO.getFactoryDAO(EstadiaFactoryDAO.POSTGRESQL).getEstadiaDAO();

        Estadia estadia = estadiaDAO.getUltimaEstadiaImpagaYaEmpezada(idHabitacion);

        if(estadia!=null)
            return this.generarEstadiaDTO(estadia);
        else return null;

    }

    //************************************  TRANSFORMACIÓN A/DESDE DTOs  *********************************************

    /**
     * Genera el DTO correspondiente a una entidad Estadia.
     *
     * @param unaEstadia    el objeto entidad a partir del cual generar el DTO.
     * @return              el DTO con los datos de la Estadia.
     */
    private EstadiaDTO generarEstadiaDTO(Estadia unaEstadia) {

        if (unaEstadia != null) {

            EstadiaDTO unaEstadiaDTO = new EstadiaDTO();

            unaEstadiaDTO.setId(unaEstadia.getId());
            unaEstadiaDTO.setFechaDesde(unaEstadia.getFechaDesde());
            unaEstadiaDTO.setFechaHasta(unaEstadia.getFechaHasta());
            unaEstadiaDTO.setImporte(unaEstadia.getImporte());
            unaEstadiaDTO.setDeuda(unaEstadia.getDeuda());

            GestorHabitacion gestorHabitacion = GestorHabitacion.obtenerInstancia();
            HabitacionDTO unaHabitacionDTO = gestorHabitacion.generarHabitacionDTO(unaEstadia.getHabitacion());

            unaEstadiaDTO.setHabitacionDTO(unaHabitacionDTO);

            GestorPersona gestorPersona = GestorPersona.obtenerInstancia();
            PasajeroDTO pasajeroDTO = gestorPersona.generarPasajeroDTO(unaEstadia.getResponsable());

            unaEstadiaDTO.setPasajeroResponsable(pasajeroDTO);

            List<PasajeroDTO> pasajerosDTO = new ArrayList<>();
            for (Pasajero p: unaEstadia.getPasajeros()){
                pasajerosDTO.add(gestorPersona.generarPasajeroDTO(p));
            }

            unaEstadiaDTO.setPasajeros(pasajerosDTO);

            return unaEstadiaDTO;

        }

        return null;

    }

    /**
     * Instancia un objeto entidad Estadia a partir de los datos recibidos por el DTO.
     *
     * @param estadiaDTO    el DTO con los datos a partir de los cuales se instancia la entidad Estadia.
     * @return              una instancia de la entidad Estadia con los datos recibidos.
     */
    private Estadia generarEstadia(EstadiaDTO estadiaDTO) {

        Estadia estadia = new Estadia();

        estadia.setFechaDesde(estadiaDTO.getFechaDesde());
        estadia.setFechaHasta(estadiaDTO.getFechaHasta());

        try {
            estadia.setHabitacion(GestorHabitacion.obtenerInstancia().getHabitacionEntidad(estadiaDTO.getHabitacionDTO().getId()));
            estadia.getHabitacion().setEstado(EstadoHabitacion.OCUPADA);
        } catch (ObjetoNoEncontradoException e) {
            return null;
        }

        Pasajero pasajeroResponsable;

        try {
            pasajeroResponsable = GestorPersona.obtenerInstancia().getPasajeroEntidad(estadiaDTO.getPasajeroResponsable().getId());
        } catch (DatosErroneosPasajeroException e) {
            return null;
        }

        if (pasajeroResponsable == null) {
            return null;
        }

        estadia.setResponsable(pasajeroResponsable);

        List<Pasajero> pasajeros = GestorPersona.obtenerInstancia().getPasajerosEntidad(estadiaDTO.getPasajeros());

        if (pasajeros.isEmpty()) {
            return null;
        }

        estadia.setPasajeros(pasajeros);

        return estadia;

    }

    /**
     * Genera una lista de DTOs a partir de una lista de objetos entidad Consumo.
     *
     * @param list  una lista de objetos entidad.
     * @return      una lista de los DTOs correspondientes a las entidades recibidas.
     */
    private List<ConsumoDTO> generarConsumosDTO(List<Consumo> list){

        List<ConsumoDTO> listaRet;

        listaRet = list.stream().map(this::generarConsumoDTO).toList();

        return listaRet;
    }

    /**
     * Genera el DTO correspondiente a un objeto de entidad Consumo.
     *
     * @param c     el objeto de entidad para el cual se desea generar el DTO.
     * @return      un DTO con los datos del objeto entidad.
     */
    private ConsumoDTO generarConsumoDTO(Consumo c){

        ConsumoDTO ret = new ConsumoDTO();

        ret.setEstadia(generarEstadiaDTO(c.getEstadia()));
        ret.setFecha(c.getFecha());
        ret.setTipo(c.getTipo());
        ret.setCantidad(c.getCantidad());
        ret.setDetalle(c.getDetalle());
        ret.setCantidadFacturada(c.getCantidadFacturada());
        ret.setId(c.getId());
        ret.setImporteUnitario(c.getImporteUnitario());

        return ret;
    }

    //****************************************  VALIDACIONES  ********************************************************

    /**
     * Verifica si un Pasajero figura como ocupante de una Habitacion para alguna Estadia en el período especificado.
     *
     * @param idPasajero    el id del Pasajero buscado.
     * @param fechaDesde    la fecha de inicio del período de tiempo en el que buscar las Estadias.
     * @param fechaHasta    la fecha de finalización del período de tiempo en el que buscar las Estadias.
     * @return              <code>true</code> si el Pasajero ya figura como ocupante en al menos una de las Estadias
     *                      que se encuentran en el período de tiempo especificado, <code>false</code> en el caso
     *                      contrario.
     */
    public Boolean ocupanteCargadoEnOtraHabitacion(Long idPasajero, LocalDateTime fechaDesde, LocalDateTime fechaHasta){

        List<EstadiaDTO> estadiasDTO = null;
        try {
            estadiasDTO = this.getEstadias(fechaDesde, fechaHasta);
        } catch (RangoDeFechaErroneoException e) {
            // No debería pasar nunca porque uso este método con fechas que fueron previamente validadas
            System.out.println("Rango de fechas incorrecto");
        } catch (ObjetoNoEncontradoException e) {
            return false;
        }

        for (EstadiaDTO e: estadiasDTO){
            for(PasajeroDTO p: e.getPasajeros()){
                if(p.getId() == idPasajero){return true;}
            }
        }
        return false;
    }

    /**
     * Validas las fechas de inicio y fin de un período de tiempo.
     *
     * @param fechaDesde    la fecha de inicio.
     * @param fechaHasta    la fecha de finalización.
     * @return              <code>true</code> si las fechas son válidas, <code>false</code> en el caso contrario.
     */
    private boolean validarRangoFechas(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

        try {

            return fechaDesde.isBefore(fechaHasta);

        } catch (NullPointerException e) {

            return false;

        }


    }

    /**
     * Valida el formato de un id.
     *
     * @param unId  el id a validar.
     * @return      <code>true</code> si el id es válido, <code>false</code> en el caso contrario.
     */
    private boolean validarId(long unId) {

        return unId>=0;

    }

    /**
     * Verifica los valores de los atributos del DTO de una Estadia para validar que sirva para crear una Entidad.
     *
     * @param estadiaDTO    el DTO con los datos de la Estadia.
     * @return              <code>true</code> si todos los atributos del DTO son válidos en relación a si son
     *                      suficientes y correctos para ser usados para instanciar y guardar un objeto entidad
     *                      de la clase Estadia, <code>false</code> si al menos un atributo no es válido.
     */
    private boolean validarEstadia(EstadiaDTO estadiaDTO) {

        if (estadiaDTO.getPasajeroResponsable() == null) {
            return false;
        }

        if (!this.validarRangoFechas(estadiaDTO.getFechaDesde(), estadiaDTO.getFechaHasta())) {
            return false;
        }

        if(!GestorPersona.obtenerInstancia().esMayorDeEdad(estadiaDTO.getPasajeroResponsable())){
            return false;
        }

        LocalDateTime ldtdesde=estadiaDTO.getFechaDesde();
        LocalDate fdesde=LocalDate.of(ldtdesde.getYear(), ldtdesde.getMonthValue(), ldtdesde.getDayOfMonth());

        if(!fdesde.isEqual(LocalDate.now())){
            //System.out.println("Entro por false :(");
            return false;
        }

        return !estadiaDTO.getPasajeros().isEmpty();

    }

    //**************************************  OTRAS OPERACIONES  *****************************************************

    /**
     * Calcula y setea el costo total de una estadía (sin IVA) y su deuda inicial.
     * Recorre todos los consumos de la estadía y los suma. Setea el resultado como el importe total de la estadía y
     * también como la deuda de la misma (la deuda la constituyen los consumos no facturados).
     *
     * @param unaEstadia    la estadía a la cual se le desea setear el total.
     */
    private void setearTotalEstadia(Estadia unaEstadia){

        List<Consumo> listaConsumos = unaEstadia.getConsumos();

        float total = (float) listaConsumos.stream().mapToDouble(c -> c.getImporteUnitario()*c.getCantidad()).sum();
        unaEstadia.setImporte(total);
        unaEstadia.setDeuda(total);

    }

    /**
     * Calcula el precio por noche de una Habitacion de acuerdo a su tipo.
     *
     * @param tipoHabitacion    el tipo de la Habitacion.
     * @return                  el costo por noche para una Habitación del tipo especificado. El costo es por Habitacion
     *                          sin importar cuantos pasajeros se hospeden en la misma.
     */
    private float getPrecioPorNoche(TipoHabitacion tipoHabitacion) {

        switch (tipoHabitacion) {

            case SUITE_DOBLE:
                return 12600.0f;

            case DOBLE_ESTANDAR:
                return 6240.0f;

            case DOBLE_SUPERIOR:
                return 7308.0f;

            case INDIVIDUAL_ESTANDAR:
                return 4200.0f;

            case SUPERIOR_FAMILY_PLAN:
                return 10500.0f;

            default:
                return -1.0f;

        }

    }

    /**
     * Calcula la cantidad de días que hay en un período de tiempo especificado por una fecha inicial y una final.
     *
     * @param fechaDesde    la fecha inicial del período.
     * @param fechaHasta    la fecha final del período.
     * @return              la cantidad de días del período.
     */
    private int calcularCantidadDias(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {

        int cantDias = Period.between(fechaDesde.toLocalDate(), fechaHasta.toLocalDate()).getDays();

        if (cantDias == 0) {
            return 1;
        }

        return Math.abs(cantDias);

    }

}

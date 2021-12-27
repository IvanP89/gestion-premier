package org.gestionpremier.negocio.gestores;

import org.gestionpremier.dao.pasajero.PasajeroDAO;
import org.gestionpremier.dao.pasajero.PasajeroFactoryDAO;
import org.gestionpremier.dao.responsable.ResponsablePagoDAO;
import org.gestionpremier.dto.BusquedaPasajerosDTO;
import org.gestionpremier.dto.DireccionDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.dto.ResponsableDePagoDTO;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.excepciones.DatosErroneosResponsablePagoException;
import org.gestionpremier.excepciones.ObjetoNoEncontradoException;
import org.gestionpremier.excepciones.PasajeroExisteException;
import org.gestionpremier.interfaces.VentanaGestionarPasajero;
import org.gestionpremier.negocio.entidades.*;
import org.gestionpremier.utilidades.CriterioOrdenPasajeros;
import org.gestionpremier.utilidades.ListaPaginada;
import org.gestionpremier.dao.responsable.ResponsablePagoFactoryDAO;
import org.gestionpremier.dto.*;
import org.gestionpremier.negocio.entidades.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * GestorPersona se encarga de la lógica de negocio que involucra a las clases de entidad que representan
 * personas físicas o jurídicas modeladas en el programa. Estas clases entidad son: Pasajero y ResponsableDePago.
 * <p>
 * Clase Singleton.
 */

public class GestorPersona {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Instancia única del gestor según el patrón Singleton.
     */
    private static GestorPersona instancia;

    //******************************************  CONSTRUCTORES  ****************************************************

    /**
     * Constructor por defecto. Privado por implementarse el patrón Singleton.
     */
    private GestorPersona() {

    }

    /**
     * Método para obtener la única instancia de la clase por implementación del patrón Singleton.
     *
     * @return la única instancia de la clase GestorPersona.
     */
    public static GestorPersona obtenerInstancia() {

        if (instancia == null) {
            instancia = new GestorPersona();
        }

        return instancia;

    }

    //***********************************************  DAOs  ********************************************************

    /**
     * Método que resuelve la lógica del patrón Factory Method para obtener el DAO de pasajero.
     *
     * @return  una instancia del DAO de Pasajero.
     */
    private PasajeroDAO getPasajeroDAO() {

        PasajeroFactoryDAO factoryPasajero = PasajeroFactoryDAO.getFactoryDAO(PasajeroFactoryDAO.POSTGRESQL);
        PasajeroDAO pasajeroDAO = factoryPasajero.getPasajeroDAO();

        return pasajeroDAO;

    }

    /**
     * Método que resuelve la lógica del patrón Factory Method para obtener el DAO de ResponsableDePago.
     *
     * @return  una instancia del DAO de ResponsableDePago.
     */
    private ResponsablePagoDAO getResponsableDAO() {

        ResponsablePagoFactoryDAO factoryResponsable = ResponsablePagoFactoryDAO.getFactoryDAO(ResponsablePagoFactoryDAO.POSTGRESQL);
        ResponsablePagoDAO responsableDAO = factoryResponsable.getResponsablePagoDAO();

        return responsableDAO;

    }

    //****************************************  INSERTS / UPDATES  **************************************************

    /**
     * Inserta un nuevo pasajero en la base de datos.
     *
     * @param pasajeroDTO               el DTO del pasajero a insertar en la base de datos.
     * @param comprobarExistencia       <code>true</code> si antes de insertar el nuevo pasajero se debe comprobar
     *                                  si ya existe un pasajero en la base de datos con el mismo tipo y número de
     *                                  documento, cuyo caso positivo lanza la excepción correspondiente y el nuevo
     *                                  pasajero no se inserta. <code>false</code> para indicar que se omita la
     *                                  comprobación anterior.
     * @return                          el id generado para el pasajero luego de insertarlo en la base de datos.
     * @throws PasajeroExisteException         si ya existe previamente un Pasajero en la base de datos con el mismo
     *                                          tipo y número de documento.
     * @throws DatosErroneosPasajeroException  si alguno de los campos de pasajeroDTO no es válido.
     */
    public long crearPasajero(PasajeroDTO pasajeroDTO, boolean comprobarExistencia) throws PasajeroExisteException, DatosErroneosPasajeroException {

        if (validarNuevoPasajero(pasajeroDTO, comprobarExistencia)) {

            Pasajero pasajero = generarPasajero(pasajeroDTO);

            PasajeroDAO dao = getPasajeroDAO();

            return dao.insertarPasajero(pasajero);

        } else {

            return -1;

        }

    }

    //*********************************************  GETTERS  *******************************************************

    /**
     * Busca mediante el DAO de Pasajero una lista de pasajeros que coincidan con el criterio de búsqueda ingresado
     * en VentanaGestionarPasajero. Retorna la totalidad de los resultados.
     *
     * @param pasajeroBuscado   un objeto que contiene los parámetros de búsqueda para los pasajeros.
     * @return                  una lista no paginada, con la totalidad de los resultados de la búsqueda.
     * @see                     VentanaGestionarPasajero
     */
    public List<PasajeroDTO> getPasajeros (BusquedaPasajerosDTO pasajeroBuscado) {

        PasajeroDAO pasajeroDAO = getPasajeroDAO();

        List<Pasajero> listaResultado = pasajeroDAO.getPasajeros(
                pasajeroBuscado.getApellido(),
                pasajeroBuscado.getNombre(),
                pasajeroBuscado.getTipoDoc(),
                pasajeroBuscado.getNroDoc());

        return crearListaPasajeroDTO(listaResultado);

    }

    /**
     * Busca mediante el DAO de Pasajero una lista de pasajeros que coincidan con el criterio de búsqueda ingresado
     * en VentanaGestionarPasajero. La búsqueda se realiza con paginación. Retorna una página del resultado.
     *
     * @param pasajeroBuscado   un objeto que contiene los parámetros de búsqueda para los pasajeros.
     * @param paginaAMostrar    el índice de la página a mostrar para la búsqueda paginada.
     * @param tamanyoPagina     la cantidad de elementos por página definidos en la query de búsqueda paginada.
     * @param ordenamiento      el criterio para el orden de la lista a traer, a ser especificado en la query a la base
     *                          de datos.
     * @return                  una lista no paginada, con la totalidad de los resultados de la búsqueda.
     * @throws DatosErroneosPasajeroException   si alguno de los campos de pasajeroBuscado no es válido.
     */
    public ListaPaginada<PasajeroDTO> getPasajerosPaginado (BusquedaPasajerosDTO pasajeroBuscado, int paginaAMostrar, int tamanyoPagina, CriterioOrdenPasajeros ordenamiento) throws DatosErroneosPasajeroException {

        if (validarBusquedaPasajero(pasajeroBuscado)) {

            PasajeroDAO pasajeroDAO = getPasajeroDAO();

            ListaPaginada<Pasajero> listaResultado = pasajeroDAO.getPasajerosPaginado(
                    pasajeroBuscado.getApellido(),
                    pasajeroBuscado.getNombre(),
                    pasajeroBuscado.getTipoDoc(),
                    pasajeroBuscado.getNroDoc(),
                    paginaAMostrar,
                    tamanyoPagina,
                    ordenamiento);

            ListaPaginada<PasajeroDTO> listaResultadoDTO = new ListaPaginada<>();
            listaResultadoDTO.setPaginaActual(listaResultado.getPaginaActual());
            listaResultadoDTO.setCantPaginas(listaResultado.getCantPaginas());
            listaResultadoDTO.setCantElementosPorPagina(listaResultado.getCantElementosPorPagina());
            listaResultadoDTO.setLista( crearListaPasajeroDTO(listaResultado.getLista()) );

            return listaResultadoDTO;

        }

        return new ListaPaginada<>();

    }

    /**
     * Busca un Pasajero en la base de datos, lo instancia y lo retorna.
     *
     * @param idPasajero    el valor del atributo id del Pasajero, correspondiente a la columna id de la tupla
     *                     con los datos del mismo en la base de datos.
     * @return              el pasajero buscado, o null si no fue encontrado.
     * @throws DatosErroneosPasajeroException   si el id no tiene un valor válido.
     */
    protected Pasajero getPasajeroEntidad(long idPasajero) throws DatosErroneosPasajeroException {

        if(idPasajero>=0) {

            PasajeroDAO pasajeroDAO = getPasajeroDAO();

            PasajeroDTO pasajeroDTO;

            Pasajero pasajero = pasajeroDAO.getPasajero(idPasajero);

            return pasajero;

        } else {
            throw new DatosErroneosPasajeroException();
        }

    }

    /**
     * Busca un Pasajero en la base de datos y retorna su DTO.
     *
     * @param pasajeroId    el valor del atributo id del Pasajero, correspondiente a la columna id de la tupla
     *                      con los datos del mismo en la base de datos.
     * @return              el DTO del pasajero buscado.
     * @throws DatosErroneosPasajeroException   si el id no es válido.
     * @throws ObjetoNoEncontradoException  si no se encuentra en la base de datos un Pasajero con el id especificado.
     */
    public PasajeroDTO getPasajero(long pasajeroId) throws DatosErroneosPasajeroException, ObjetoNoEncontradoException {

        if(pasajeroId>=0) {

            PasajeroDAO pasajeroDAO = getPasajeroDAO();

            PasajeroDTO pasajeroDTO;

            Pasajero pasajero = pasajeroDAO.getPasajero(pasajeroId);

            if (pasajero == null) {
                throw new ObjetoNoEncontradoException("No se encuentra el pasajero con id = " + pasajeroId);
            }

            pasajeroDTO = generarPasajeroDTO(pasajero);

            return pasajeroDTO;

        } else {
            throw new DatosErroneosPasajeroException();
        }

    }

    /**
     * Busca un responsable de pago en la base de datos y retorna su DTO.
     *
     * @param responsableId     el valor del atributo id del ResponsableDePago, correspondiente a la columna id de la
     *                          tupla con los datos del mismo en la base de datos.
     * @return                  el DTO del responsable de pago buscado.
     * @throws DatosErroneosResponsablePagoException si el id no es válido.
     * @throws ObjetoNoEncontradoException  si no se encuentra en la base de datos un ResponsableDePago con el id
     *                                      especificado.
     */
    public ResponsableDePagoDTO getResponsable(long responsableId) throws DatosErroneosResponsablePagoException, ObjetoNoEncontradoException {

        if(responsableId>=0){

                ResponsablePagoDAO responsableDAO = getResponsableDAO();

                ResponsableDePagoDTO responsableDTO=null;

                ResponsableDePago responsable = responsableDAO.getResponsable(responsableId);

                if(responsable == null) {
                    throw new ObjetoNoEncontradoException("No se encuentra el responsable de pago con id = " + responsableId);
                }

                responsableDTO = generarResponsableDTO(responsable);

                return responsableDTO;
        }
        else throw new DatosErroneosResponsablePagoException();

    }

    /**
     * Busca un responsable de pago en la base de datos y retorna su DTO.
     *
     * @param cuit      el cuit del responsable de pago.
     * @return          el DTO del responsable de pago buscado.
     * @throws DatosErroneosResponsablePagoException    si el cuit no es válido.
     * @throws ObjetoNoEncontradoException              si no se encuentra en la base de datos un ResponsableDePago con
     *                                                  el cuit indicado.
     */
    public ResponsableDePagoDTO getResponsable(String cuit) throws DatosErroneosResponsablePagoException, ObjetoNoEncontradoException {

        if(validarCUIT(cuit)){

            ResponsablePagoDAO responsableDAO = getResponsableDAO();

            ResponsableDePagoDTO responsableDTO=null;

            ResponsableDePago responsable = responsableDAO.getResponsables(cuit).stream().findAny().orElseThrow(ObjetoNoEncontradoException::new);

            responsableDTO=generarResponsableDTO(responsable);

            return responsableDTO;

        } else throw new DatosErroneosResponsablePagoException();

    }

    /**
     * Busca un responsable de pago en la base de datos y retorna una instancia de su clase entidad.
     *
     * @param idResponsable     el valor del atributo id del ResponsableDePago, correspondiente a la columna id de
     *                          la tupla con los datos del mismo en la base de datos.
     * @return                  el objeto entidad correspondiente al responsable de pago buscado, o <code>null</code>
     *                          si la búsqueda no arrojó resultadas.
     * @throws DatosErroneosResponsablePagoException    si el id no es válido.
     */
    protected ResponsableDePago getResponsableEntidad(long idResponsable) throws DatosErroneosResponsablePagoException {

        if(idResponsable>=0){

            ResponsablePagoDAO responsableDAO = getResponsableDAO();

            ResponsableDePago responsable = responsableDAO.getResponsable(idResponsable);

            return responsable;

        }
        else throw new DatosErroneosResponsablePagoException();

    }

    /**
     * Busca un responsable de pago en la base de datos y retorna una lista con los DTOs de los resultandos.
     *
     * @param cuit      el cuit del responsable de pago buscado.
     * @return          una lista con los DTOs correspondientes a los responsables de pago que se encontraron.
     */
    public List<ResponsableDePagoDTO> getResponsables(String cuit) {

        ResponsablePagoDAO responsableDAO = getResponsableDAO();

        List<ResponsableDePago> listaResultado = responsableDAO.getResponsables(cuit);

        return crearListaResponsableDTO(listaResultado);

    }

    //************************************  TRANSFORMACIÓN A/DESDE DTOs  *********************************************

    /**
     * Convierte una lista de PasajeroDTO en su correspondiente lista de Pasajero.
     * Recorre la lista de PasajeroDTO y va levantando sus correspondientes Pasajero de la base de datos, creando
     * una nueva lista con los mismos.
     * Protected por retornar objetos entidad de la lógica de negocio.
     *
     * @param pasajerosDTO  una lista de objetos PasajeroDTO con al menos el atributo id seteado.
     * @return              una lista con objetos de tipo Pasajero correspondientes a cada objeto
     *                      de tipo PasajeroDTO que se recibieron como argumento.
     */
    protected List<Pasajero> getPasajerosEntidad(List<PasajeroDTO> pasajerosDTO) {

        List<Pasajero> pasajerosLeidos = new ArrayList<>();

        for (PasajeroDTO p: pasajerosDTO) {

            try {
                pasajerosLeidos.add(this.getPasajeroEntidad(p.getId()));
            } catch (DatosErroneosPasajeroException e) {
                continue;
            }

        }

        return pasajerosLeidos;

    }

    /**
     * Convierte las entidades de una lista de Pasajero en sus correspondientes DTOs.
     *
     * @param pasajeros     una lista con entidades Pasajero.
     * @return              una lista con objetos DTO correspondientes a los pasajeros que se recibieron como argumento.
     */
    public List<PasajeroDTO> crearListaPasajeroDTO(List<Pasajero> pasajeros) {

        List<PasajeroDTO> listaPasajeroDTO = new ArrayList<>();

        for (Pasajero p: pasajeros) {

            listaPasajeroDTO.add(generarPasajeroDTO(p));

        }

        return listaPasajeroDTO;

    }

    /**
     * Convierte las entidades de una lista de ResponsableDePago en sus correspondientes DTOs.
     *
     * @param responsables      una lista con entidades ResponsableDePago.
     * @return                  una lista con objetos DTO correspondientes a los responsables de pago
     *                          que se recibieron como argumento.
     */
    public List<ResponsableDePagoDTO> crearListaResponsableDTO(List<ResponsableDePago> responsables) {

        List<ResponsableDePagoDTO> listaResponsableDTO = new ArrayList<>();

        for (ResponsableDePago r: responsables) {

            listaResponsableDTO.add(generarResponsableDTO(r));

        }

        return listaResponsableDTO;

    }

    /**
     * Genera el DTO de una entidad Pasajero.
     *
     * @param pasajero  un objeto entidad correspondiente a un Pasajero.
     * @return          el DTO del pasajero recibido como argumento.
     */
    public PasajeroDTO generarPasajeroDTO(Pasajero pasajero) {

        PasajeroDTO pasajeroDTO = new PasajeroDTO();

        pasajeroDTO.setId(pasajero.getId());
        pasajeroDTO.setNombre(pasajero.getNombre());
        pasajeroDTO.setApellido(pasajero.getApellido());
        pasajeroDTO.setNroDoc(pasajero.getNroDoc());
        pasajeroDTO.setTipoDoc(pasajero.getTipoDoc());
        pasajeroDTO.setTelefono(pasajero.getTelefono());
        pasajeroDTO.setEmail(pasajero.getEmail());
        pasajeroDTO.setFechaNacimiento(pasajero.getFechaNacimiento());
        pasajeroDTO.setNacionalidad(pasajero.getNacionalidad());
        pasajeroDTO.setCuit(pasajero.getCuit());
        pasajeroDTO.setOcupacion(pasajero.getOcupacion());
        pasajeroDTO.setPosicionIva(pasajero.getPosicionIva());
        pasajeroDTO.setDireccion(generarDireccionDTO(pasajero.getDireccion()));

        return pasajeroDTO;

    }

    /**
     * Genera el DTO de una entidad Direccion.
     *
     * @param direccion     la entidad Dirección cuyo DTO se desea generar.
     * @return              el DTO de la dirección.
     */
    public DireccionDTO generarDireccionDTO(Direccion direccion) {

        GestorGeografico gestorGeografico = GestorGeografico.obtenerInstancia();

        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setCalle(direccion.getCalle());
        direccionDTO.setNroCalle(direccion.getNroCalle());
        direccionDTO.setDepto(direccion.getDepto());
        direccionDTO.setPiso(direccion.getPiso());
        direccionDTO.setCp(direccion.getCp());
        direccionDTO.setLocalidad(gestorGeografico.generarLocalidadDTO(direccion.getLocalidad()));

        return direccionDTO;

    }

    /**
     * Genera el DTO correspondiente a una entidad ResponsableDePago.
     *
     * @param responsable   La entidad para lo cual se desea generar el DTO.
     * @return              El DTO correspondiente a la entidad que se recibió como argumento.
     */
    public ResponsableDePagoDTO generarResponsableDTO(ResponsableDePago responsable) {

        ResponsableDePagoDTO responsableDTO = new ResponsableDePagoDTO();

        responsableDTO.setId(responsable.getId());
        responsableDTO.setRazonSocial(responsable.getRazonSocial());
        responsableDTO.setTelefono(responsable.getTelefono());
        responsableDTO.setCuit(responsable.getCuit());
        responsableDTO.setDireccion(generarDireccionDTO(responsable.getDireccion()));

        return responsableDTO;

    }

    /**
     * Genera un objeto entidad de modelo de negocio a partir de un DTO.
     *
     * @param dto   el DTO de un pasajero.
     * @return      un objeto entidad del modelo de negocio correspondiente al DTO que se recibio como argumento.
     */
    private Pasajero generarPasajero(PasajeroDTO dto) {

        Pasajero pasajero = new Pasajero();

        pasajero.setNombre(dto.getNombre());
        pasajero.setApellido(dto.getApellido());
        pasajero.setTipoDoc(dto.getTipoDoc());
        pasajero.setNroDoc(dto.getNroDoc());
        pasajero.setTelefono(dto.getTelefono());
        pasajero.setEmail(dto.getEmail());
        pasajero.setFechaNacimiento(dto.getFechaNacimiento());
        pasajero.setNacionalidad(dto.getNacionalidad());
        pasajero.setCuit(dto.getCuit());
        pasajero.setOcupacion(dto.getOcupacion());
        pasajero.setPosicionIva(dto.getPosicionIva());

        Direccion direccion = crearDireccion(dto.getDireccion());

        pasajero.setDireccion(direccion);

        return pasajero;

    }

    /**
     * Genera un objeto entidad de modelo de negocio a partir de un DTO.
     *
     * @param dto   el DTO de una dirección.
     * @return      un objeto entidad correspondiente a una dirección.
     */
    private Direccion crearDireccion(DireccionDTO dto) {

        Direccion direccion = new Direccion();

        direccion.setCalle(dto.getCalle());
        direccion.setNroCalle(dto.getNroCalle());
        direccion.setPiso(dto.getPiso());
        direccion.setDepto(dto.getDepto());
        direccion.setCp(dto.getCp());

        GestorGeografico gestorGeografico = GestorGeografico.obtenerInstancia();

        Localidad localidad = gestorGeografico.getLocalidad(dto.getLocalidad().getId_localidad());

        direccion.setLocalidad(localidad);

        return direccion;

    }

    //****************************************  VALIDACIONES  ********************************************************

    /**
     * Verifica si la edad correspondiente del PasajeroDTO pasado por parámetro es mayor a 18 años.
     *
     * @param pasajeroDTO   La entidad para lo cual se desea verificar la edad.
     * @return              true or false según corresponda.
     */
    public Boolean esMayorDeEdad(PasajeroDTO pasajeroDTO){

        Long diferencia = Math.abs(ChronoUnit.YEARS.between(pasajeroDTO.getFechaNacimiento(), LocalDateTime.now()));
        if(diferencia>=18) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Realiza las validaciones correspondientes previas a la inserción de un nuevo pasajero en la base de datos.
     *
     * @param pasajeroDTO               el DTO del nuevo pasajero que se va a insertar en la base de datos.
     * @param comprobarExistencia       bandera que indica si también debe realizarse la validación de existencia.
     * @return                          <code>true</code> si todas las validaciones fueron positivas y el pasajero
     *                                  puede insertarse. <code>false</code> si al menos una validación fue negativa.
     * @throws  PasajeroExisteException         si ya existe previamente en la base de datos un Pasajero con el mismo
     *                                          tipo y número de documento que el nuevo pasajero.
     * @throws  DatosErroneosPasajeroException  si alguno de los campos de pasajeroDTO no es válido.
     */
    private boolean validarNuevoPasajero (PasajeroDTO pasajeroDTO, boolean comprobarExistencia) throws PasajeroExisteException, DatosErroneosPasajeroException {

        if (comprobarExistencia) {

            if (comprobarExistencia(pasajeroDTO)) {

                throw new PasajeroExisteException();

            }

        }

        return validarDatosNuevoPasajero(pasajeroDTO);

    }

    /**
     * Valida el formato de los campos que componen el criterio de búsqueda para uno o varios pasajeros.
     *
     * @param pasajeroBuscado   un DTO creado por la ventana de gestión de pasajeros con los criterios de búsqueda.
     * @return                  <code>true</code> si todos los campos son válidos, <code>false</code> si al menos un
     *                          campo no es válido.
     * @throws  DatosErroneosPasajeroException  si alguno de los campos de pasajeroBuscado no es válido.
     */
    private boolean validarBusquedaPasajero (BusquedaPasajerosDTO pasajeroBuscado) throws DatosErroneosPasajeroException {

        StringBuilder mensajeError = new StringBuilder();
        boolean hayErrores = false;

        mensajeError.append("Error en los campos de búsqueda:\n");

        if (!pasajeroBuscado.getApellido().matches("^$|^[A-ZÑÁÉÍÓÚ\s\\.]+$")) {

            mensajeError.append("\nEl apellido ingresado no tiene un formáto válido.");
            hayErrores = true;

        }
        if (pasajeroBuscado.getApellido().length()>50) {

            mensajeError.append("\nEl apellido no puede contener más de 50 caracteres.");
            hayErrores = true;

        }
        if (!pasajeroBuscado.getNombre().matches("^$|^[A-ZÑÁÉÍÓÚ\s\\.]+$")) {

            mensajeError.append("\nEl nombre ingresado no tiene un formáto válido.");
            hayErrores = true;

        }
        if (pasajeroBuscado.getNombre().length()>50) {

            mensajeError.append("\nEl nombre no puede contener más de 50 caracteres.");
            hayErrores = true;

        }
        if ((!pasajeroBuscado.getNroDoc().matches("^$|^[1-9][0-9]{7}$") && pasajeroBuscado.getTipoDoc().equals("DNI"))
                || (!pasajeroBuscado.getNroDoc().matches("^$|^[0-9]{1,8}$|^[1-9][0-9]{0,6}$") && ((pasajeroBuscado.getTipoDoc().equals("LE")) || (pasajeroBuscado.getTipoDoc().equals("LC"))))
                || (!pasajeroBuscado.getNroDoc().matches("^$|^[A-Z0-9]{6,9}$") && pasajeroBuscado.getTipoDoc().equals("PASAPORTE"))
                || (!pasajeroBuscado.getNroDoc().matches("^$|^[A-Z0-9]{1,15}$") && (pasajeroBuscado.getTipoDoc().equals("OTRO") || pasajeroBuscado.getTipoDoc().equals("")))) {

            mensajeError.append("\nEl número de documento ingresado no tiene un formato válido para el tipo especificado.");
            hayErrores = true;

        }

        if (hayErrores) {

            throw new DatosErroneosPasajeroException(mensajeError.toString());

        }

        return true;

    }

    /**
     * Comprueba si ya existe en la base de datos de pasajeros al menos una entrada con el mismo tipo y número de documento.
     *
     * @param pasajeroDTO   el DTO con los datos del pasajero cuya previa existencia se desea comprobar.
     * @return              <code>true</code> si ya existe un pasajero con el mismo tipo y número de documento,
     *                      <code>false</code> en el caso contrario.
     */
    private boolean comprobarExistencia (PasajeroDTO pasajeroDTO) {

        PasajeroDAO pasajeroDAO = getPasajeroDAO();

        List<Pasajero> listaResultado = pasajeroDAO.getPasajeros(
                "",
                "",
                pasajeroDTO.getTipoDoc().toString(),
                pasajeroDTO.getNroDoc());

        try {

            return !listaResultado.isEmpty();

        } catch (NullPointerException e) {

            return false;

        }

    }

    /**
     * Valida el formato de los datos de los atributos del DTO de un pasajero para que estén acorde a los criterios
     * para almacenarlos en la base de datos.
     *
     * @param pasajeroDTO   el DTO de un pasajero a validar.
     * @return              <code>true</code> si todos los atributos del pasajero son válidos, <code>false</code> si
     *                      al menos uno de los atributos no tiene un formato válido.
     * @throws  DatosErroneosPasajeroException  si alguno de los campos de pasajeroDTO no es válido.
     */
    private boolean validarDatosNuevoPasajero(PasajeroDTO pasajeroDTO) throws DatosErroneosPasajeroException {

        StringBuilder mensajeError = new StringBuilder();
        boolean hayErrores = false;

        mensajeError.append("Error en los datos del nuevo pasajero:\n");

        if (pasajeroDTO.getNombre().equals("")) {

            mensajeError.append("\nEl nombre del pasajero no puede estar en blanco.");
            hayErrores = true;

        } else {

            if (pasajeroDTO.getNombre().length()>50) {
                mensajeError.append("\nEl nombre del pasajero no puede tener más de 50 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getNombre().matches("^[A-ZÑÁÉÍÓÚ\s\\.]+$")) {
                mensajeError.append("\nEl nombre del pasajero no puede contener números o caracteres especiales.");
                hayErrores = true;
            }

        }
        if (pasajeroDTO.getApellido().equals("")) {

            mensajeError.append("\nEl apellido no puede estar en blanco.");
            hayErrores = true;

        } else {

            if (pasajeroDTO.getApellido().length()>50) {
                mensajeError.append("\nEl apellido no puede tener más de 50 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getApellido().matches("^[A-ZÑÁÉÍÓÚ\s\\.]+$")) {
                mensajeError.append("\nEl apellido no puede contener números o caracteres especiales.");
                hayErrores = true;
            }

        }
        if (pasajeroDTO.getNroDoc().equals("")) {

            mensajeError.append("\nEl número de documento no puede estar en blanco.");
            hayErrores = true;

        } else {

            if (pasajeroDTO.getNroDoc().length()>15) {
                mensajeError.append("\nEl número de documento no puede contener más de 15 caracteres.");
                hayErrores = true;
            }
            if ((!pasajeroDTO.getNroDoc().matches("^$|^[1-9][0-9]{7}$") && pasajeroDTO.getTipoDoc() == TipoDoc.DNI)
                    || (!pasajeroDTO.getNroDoc().matches("^$|^[0-9]{1,8}$|^[1-9][0-9]{0,6}$") && ((pasajeroDTO.getTipoDoc() == TipoDoc.LE) || (pasajeroDTO.getTipoDoc() == TipoDoc.LC)))
                    || (!pasajeroDTO.getNroDoc().matches("^$|^[a-zA-Z0-9]{6,9}$") && pasajeroDTO.getTipoDoc() == TipoDoc.PASAPORTE)
                    || (!pasajeroDTO.getNroDoc().matches("^$|^[a-zA-Z0-9]{1,15}$") && (pasajeroDTO.getTipoDoc() == TipoDoc.OTRO))) {

                mensajeError.append("\nEl número de documento no tiene un formato válido para el tipo especificado.");
                hayErrores = true;

            }

        }
        if (pasajeroDTO.getTelefono().length()>20) {
            mensajeError.append("\nEl número de teléfono no puede contener más de 20 caracteres.");
            hayErrores = true;
        }
        if (!pasajeroDTO.getTelefono().matches("^$|^[0-9\\-\\(\\)\s]+$")) {
            mensajeError.append("\nEl número de teléfono solamente puede contener número, guiones y/o paréntesis.");
            hayErrores = true;
        }
        if (pasajeroDTO.getEmail().length()>30) {
            mensajeError.append("\nLa dirección de correo electrónico no puede contener más de 30 caracteres.");
            hayErrores = true;
        }
        if (!pasajeroDTO.getEmail().matches("^$|^[A-ZÑ0-9\\_\\-\\.]+@[A-ZÑ0-9]+\\.[A-ZÑ0-9]+(\\.[A-ZÑ]+)*$")) {
            mensajeError.append("\nEl formato de la dirección de correo electrónico es incorrecto.");
            hayErrores = true;
        }
        if (pasajeroDTO.getFechaNacimiento() == null) {
            mensajeError.append("\nLa fecha de nacimiento no puede estar en blanco.");
            hayErrores = true;
        }
        if (pasajeroDTO.getNacionalidad().equals("")) {
            mensajeError.append("\nLa nacionalidad no puede estar en blanco.");
            hayErrores = true;
        } else {
            if (pasajeroDTO.getNacionalidad().length()>20) {
                mensajeError.append("\nLa nacionalidad no puede contener más de 20 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getNacionalidad().matches("^[A-ZÁÉÍÓÚÑ\s]+$")) {
                mensajeError.append("\nLa nacionalidad no puede contener caracteres especiales.");
                hayErrores = true;
            }
        }
        if (pasajeroDTO.getDireccion().getCalle().equals("")) {
            mensajeError.append("\nEl nombre de la calle no puede estar en blanco.");
            hayErrores = true;
        } else {
            if (pasajeroDTO.getDireccion().getCalle().length()>30) {
                mensajeError.append("\nEl nombre de la calle no puede tener más de 30 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getDireccion().getCalle().matches("^[A-Z0-9ÁÉÍÓÚÑ\s\\.]+$")) {
                mensajeError.append("\nEl nombre de la calle no puede contener caracteres especiales.");
                hayErrores = true;
            }
        }
        if (pasajeroDTO.getDireccion().getNroCalle().equals("")) {
            mensajeError.append("\nEl número de la calle no puede estar en blanco.");
            hayErrores = true;
        } else {
            if (pasajeroDTO.getDireccion().getNroCalle().length()>5) {
                mensajeError.append("\nEl número de la calle no puede contener más de 5 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getDireccion().getNroCalle().matches("^[A-ZÑ0-9\s]+$")) {
                mensajeError.append("\nEl número de la calle no puede contener caracteres especiales.");
                hayErrores = true;
            }
        }
        if (pasajeroDTO.getDireccion().getDepto().length()>5) {
            mensajeError.append("\nEl departamente no puede contener más de 5 caracteres.");
            hayErrores = true;
        }
        if (!pasajeroDTO.getDireccion().getDepto().matches("^$|^[A-ZÑ0-9\s]+$")) {
            mensajeError.append("\nEl departamente no puede contener caracteres especiales.");
            hayErrores = true;
        }
        if (pasajeroDTO.getDireccion().getPiso().length()>5) {
            mensajeError.append("\nEl piso no puede contener más de 3 caracteres.");
            hayErrores = true;
        }
        if (!pasajeroDTO.getDireccion().getPiso().matches("^$|^[A-ZÑ0-9\s]+$")) {
            mensajeError.append("\nEl piso no puede contener caracteres especiales.");
            hayErrores = true;
        }
        if (pasajeroDTO.getDireccion().getCp().equals("")) {
            mensajeError.append("\nEl código postal no puede estar en blanco.");
            hayErrores = true;
        } else {
            if (pasajeroDTO.getDireccion().getCp().length()>10) {
                mensajeError.append("\nEl código postal no puede contener más de 10 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getDireccion().getCp().matches("^[A-ZÑ0-9\s]+$")) {
                mensajeError.append("\nEl código postal solamente puede contener caracteres alfanuméricos.");
                hayErrores = true;
            }
        }
        if (pasajeroDTO.getCuit().equals("")) {
            if (!pasajeroDTO.getPosicionIva().equals(PosicionIva.CONSUMIDOR_FINAL)) {
                mensajeError.append("\nEl CUIT no puede estar en blando si la posición ante el IVA no es consumidor final.");
                hayErrores = true;
            }
        } else {
            if (pasajeroDTO.getCuit().length()>13) {
                mensajeError.append("\nEl CUIT no puede contener más de 13 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getCuit().matches("^$|(^[0-9]{2}-[0-9]{8}-[0-9]$)|(^[0-9]{11}$)")) {
                mensajeError.append("\nEl formato del CUIT es incorrecto. Formatos aceptados: 99-99999999-9 o 99999999999.");
                hayErrores = true;
            }
        }
        if (pasajeroDTO.getOcupacion().equals("")) {
            mensajeError.append("\nLa ocupación no puede estar en blanco.");
            hayErrores = true;
        } else {
            if (pasajeroDTO.getOcupacion().length()>30) {
                mensajeError.append("\nLa ocupación no puede contener más de 30 caracteres.");
                hayErrores = true;
            }
            if (!pasajeroDTO.getOcupacion().matches("[A-Z0-9ÑÁÉÍÓÚ\s]+")) {
                mensajeError.append("\nLa ocupación no puede contener caracteres especiales.");
                hayErrores = true;
            }
        }

        if (hayErrores) {
            throw new DatosErroneosPasajeroException(mensajeError.toString());
        }

        return true;

    }

    /**
     * Valida el formato de una cadena correspondiente a un cuit, que puede ser atributo de un objeto Pasajero o ResponsableDePago.
     *
     * @param cuit  cadena con un cuit cuyo formato se desea validar.
     * @return      <code>true</code> si el formato del cuit es válido, <code>false</code> en el caso contrario.
     */
    private boolean validarCUIT(String cuit){

        boolean ret = cuit.matches("^$|(^[0-9]{2}-[0-9]{8}-[0-9]$)|(^[0-9]{11}$)");

        return ret;

    }

}

package org.gestionpremier.negocio.gestores;

import org.gestionpremier.dao.consumo.ConsumoFactoryDAO;
import org.gestionpremier.dao.factura.FacturaFactoryDAO;
import org.gestionpremier.dao.consumo.ConsumoDAO;
import org.gestionpremier.dao.factura.FacturaDAO;
import org.gestionpremier.dto.FacturaDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.dto.RenglonFacturaDTO;
import org.gestionpremier.dto.ResponsableDePagoDTO;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.excepciones.DatosErroneosResponsablePagoException;
import org.gestionpremier.excepciones.IdErroneoException;
import org.gestionpremier.negocio.entidades.*;

import java.util.List;

/**
 * GestorCaja se encarga de la lógica de negocio que involucra a las clases de entidad que intervienen en las
 * operaciones relacionadas con dinero y movimientos contables.
 * Dichas entidades son: Factura, RenglonFactura, Pago, MedioDePago, NotaDeCredito, Moneda y  Banco.
 * <p>
 * Se asume que los datos de las entidades Banco y Moneda ya existen en la base de datos, este sistema no los crea,
 * modifica ni elimina, sólo los accede.
 * <p>
 * Clase Singleton.
 */

public class GestorCaja {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Instancia única del gestor según el patrón Singleton.
     */
    private static GestorCaja instancia;

    //******************************************  CONSTRUCTORES  ****************************************************

    /**
     * Constructor por defecto. Privado por implementarse el patrón Singleton.
     */
    private GestorCaja() {
    }

    /**
     * Obtiene la única instancia de la clase por implementación del patrón Singleton.
     *
     * @return la única instancia de la clase GestorCaja.
     */
    public static GestorCaja obtenerInstancia() {

        if (instancia == null) {
            instancia = new GestorCaja();
        }

        return instancia;

    }

    //***********************************************  DAOs  ********************************************************

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de la clase Consumo.
     *
     * @return  una instancia del DAO de Consumo.
     */
    private ConsumoDAO getConsumoDAO(){

        ConsumoFactoryDAO factoryConsumo = ConsumoFactoryDAO.getFactoryDAO(ConsumoFactoryDAO.POSTGRESQL);
        ConsumoDAO consumoDAO = factoryConsumo.getConsumoDAO();

        return consumoDAO;

    }

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de la clase Factura.
     *
     * @return  una instancia del DAO de Factura.
     */
    private FacturaDAO getFacturaDAO(){

        FacturaFactoryDAO factoryFactura = FacturaFactoryDAO.getFactoryDAO(FacturaFactoryDAO.POSTGRESQL);
        FacturaDAO facturaDAO = factoryFactura.getFacturaDAO();

        return facturaDAO;

    }

    //****************************************  INSERTS / UPDATES  **************************************************

    /**
     * Crea una nueva factura y la inserta en la base de datos.
     *
     * @param facturaDTO    el objeto con los datos de la nueva factura.
     * @return              el id generado para la factura creada.
     * @throws DatosErroneosResponsablePagoException    si el id del responsable de pago asociado a la factura no tiene
     *                                                  un formato válido.
     * @throws DatosErroneosPasajeroException           si el id del pasajero asociado a la factura no tiene un formato
     *                                                  válido.
     * @throws IdErroneoException                       si el id de la estadía asociada a la factura no tiene un
     *                                                  formato válido.
     */
    public Long insertarFactura(FacturaDTO facturaDTO) throws DatosErroneosResponsablePagoException, DatosErroneosPasajeroException, IdErroneoException {

        FacturaDAO dao = getFacturaDAO();

        Factura factura = crearFactura(facturaDTO);

        Long idFactura=dao.insertarFactura(factura);

        actualizarConsumos(factura);

        facturaDTO.setNro(idFactura);

        return idFactura;

    }

    /**
     * Actualiza el estado de los consumos que se vieron afectados por una factura.
     * Se actualiza el campo correspondiente a la cantidad facturada para cada consumo asociado al renglón de una
     * factura creada. Luego refleja estos cambios en la base de datos.
     *
     * @param factura   la factura cuyos consumos facturados se desean actualizar.
     */
    private void actualizarConsumos(Factura factura) {

        ConsumoDAO consumoDAO = getConsumoDAO();

        Consumo consumo;

        for (RenglonFactura r: factura.getRenglones()) {

            consumo = r.getConsumo();

            consumo.setCantidadFacturada( consumo.getCantidadFacturada() + r.getCantidad() );

            consumoDAO.actualizarConsumo(consumo);
        }

    }

    //*********************************************  GETTERS  *******************************************************

    /**
     * Busca un Consumo en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param idConsumo     el id del consumo buscado.
     * @return              una instacia del objeto entidad del consumo, o <code>null</code> si la búsqueda no
     *                      arrojó resultados.
     */
    private Consumo getConsumo(Long idConsumo){

        ConsumoDAO consumoDAO = getConsumoDAO();

        return consumoDAO.getConsumo(idConsumo);

    }

    /**
     * Busca un ResponsableDePago en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param responsable   el DTO con los datos del responsable de pago a buscar. Sólo se necesita que su atributo
     *                      id tenga datos válidos.
     * @return              una instancia del objeto entidad del ResponsableDePago, o <code>null</code> si la
     *                      búsqueda no arrojó resultados.
     * @throws DatosErroneosResponsablePagoException    si el atributo id del DTO que se recibió no tiene un formato
     *                                                  válido.
     * @see     GestorPersona
     */
    private ResponsableDePago getResponsable(ResponsableDePagoDTO responsable) throws DatosErroneosResponsablePagoException {

        if (responsable == null) {
            return null;
        }

        GestorPersona gestorPersona = GestorPersona.obtenerInstancia();

        return gestorPersona.getResponsableEntidad(responsable.getId());

    }

    /**
     * Busca una Estadia en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param idEstadia             el id de la Estadia buscada.
     * @return                      una instancia del objeto entidad de la Estadia, o <code>null</code> si la
     *                              búsqueda no arroja resultados.
     * @throws IdErroneoException   si el id recibido no tiene un formato válido.
     * @see    GestorEstadia
     */
    private Estadia getEstadia(Long idEstadia) throws IdErroneoException {

        GestorEstadia gestorEstadia = GestorEstadia.obtenerInstancia();

        return gestorEstadia.getEstadiaEntidad(idEstadia);

    }

    /**
     * Busca un Pasajero en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param pasajero      el DTO con los datos del pasajero a buscar. Sólo es necesario que su atributo id contenga
     *                      un valor con formato válido.
     * @return              una instancia del objeto entidad del Pasajero, o <code>null</code> si la búsqueda no
     *                      arrojó resultados.
     * @throws DatosErroneosPasajeroException   si el atributo id del DTO recibido no tiene un formato válido.
     * @see    GestorPersona
     */
    private Pasajero getPasajero(PasajeroDTO pasajero) throws DatosErroneosPasajeroException {

        if (pasajero == null) {
            return null;
        }

        GestorPersona gestorPersona = GestorPersona.obtenerInstancia();

        return gestorPersona.getPasajeroEntidad(pasajero.getId());

    }

    /**
     * Busca una Factura en la base de datos y retorna una instancia de su objeto entidad.
     *
     * @param idFactura             el id de la factura buscada.
     * @return                      una instancia del objeto entidad de la Factura, o <code>null</code> si la
     *                              búsqueda no arrojó resultados.
     * @throws IdErroneoException   si el id recibido no tiene un formato válido.
     */
    protected Factura getFacturaEntidad(Long idFactura) throws IdErroneoException {

        if(idFactura>=0) {

            FacturaDAO dao = getFacturaDAO();

            Factura factura = dao.getFactura(idFactura);

            return factura;
        }
        else throw new IdErroneoException();

    }

    //************************************  TRANSFORMACIÓN A/DESDE DTOs  *********************************************

    /**
     * Crea un objeto entidad de tipo Factura a partir de su correspondiente DTO.
     * La nueva factura no se guarda, sólo se retorna.
     *
     * @param dto       el objeto con los datos de la nueva factura.
     * @return          el objeto Factura generado a partir de los datos recibidos.
     * @throws DatosErroneosPasajeroException           si el atributo id del pasajero asociado a la factura no tiene
     *                                                  un formato válido.
     * @throws DatosErroneosResponsablePagoException    si el atributo id del responsable de pago asociado a la factura
     *                                                  no tiene un formato válido.
     * @throws IdErroneoException                       si el id de la estadía asociada a la factura no tiene un formato
     *                                                  válido.
     */
    private Factura crearFactura(FacturaDTO dto) throws DatosErroneosPasajeroException, DatosErroneosResponsablePagoException, IdErroneoException {

        Factura ret=new Factura();

//        ret.setNro(dto.getNro());
        ret.setClientePasajero(getPasajero(dto.getClientePasajero()));
        ret.setClienteResponsable(getResponsable(dto.getClienteResponsable()));
        ret.setEstadia(getEstadia(dto.getIdEstadia()));
        ret.setFecha(dto.getFecha());
        ret.setEstado(dto.getEstado());
        ret.setTipo(dto.getTipo());
        ret.setIva(dto.getIva());
        ret.setTotal(dto.getTotal());
        ret.setRenglones(crearRenglones(dto.getRenglones(), ret));

        return ret;
    }

    /**
     * Genera una lista de objetos entidad RenglonFactura a partir de una lista de sus correspondientes DTOs.
     * Los renglones generados no se guardan, sólo se retornan.
     *
     * @param listDTO   una lista de DTOs con los datos de cada renglón de una factura.
     * @param fact      la factura a la cual corresponden los renglones a crear.
     * @return          una lista de renglones en su forma de entidad generados a partir de sus DTOs.
     */
    private List<RenglonFactura> crearRenglones(List<RenglonFacturaDTO> listDTO, Factura fact){

        List<RenglonFactura> listaRenglon;
        listaRenglon = listDTO.stream().map(dto -> {
            return crearRenglonFactura(dto, fact);
        }).toList();

        return listaRenglon;
    }

    /**
     * Crea un objeto entidad RenglonFactura a partir de su correspondiente DTO.
     * El objeto creado no se guarda, sólo se retorna.
     *
     * @param dto       el objeto con los datos del renglón a crear.
     * @param fact      la factura para la cual se está generando el renglón.
     * @return          un renglón de factura de tipo entidad.
     */
    private RenglonFactura crearRenglonFactura(RenglonFacturaDTO dto, Factura fact){

        RenglonFactura renglon = new RenglonFactura();

        renglon.setNro(dto.getNro());
        renglon.setId(dto.getId());
        renglon.setCantidad(dto.getCantidad());
        renglon.setFactura(fact);
        renglon.setImporteUnitario(dto.getImporteUnitario());
        renglon.setNombreConsumo(dto.getNombreConsumo());
        renglon.setConsumo(getConsumo(dto.getConsumoId()));

        return renglon;
    }

}

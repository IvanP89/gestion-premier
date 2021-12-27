package org.gestionpremier.negocio.gestores;

import org.gestionpremier.dao.localidad.LocalidadDAO;
import org.gestionpremier.dao.localidad.LocalidadFactoryDAO;
import org.gestionpremier.dao.pais.PaisDAO;
import org.gestionpremier.dao.provincia.ProvinciaDAO;
import org.gestionpremier.dao.provincia.ProvinciaFactoryDAO;
import org.gestionpremier.dto.LocalidadDTO;
import org.gestionpremier.dto.PaisDTO;
import org.gestionpremier.dto.ProvinciaDTO;
import org.gestionpremier.negocio.entidades.Pais;
import org.gestionpremier.dao.pais.PaisFactoryDAO;
import org.gestionpremier.negocio.entidades.Localidad;
import org.gestionpremier.negocio.entidades.Provincia;

import java.util.ArrayList;
import java.util.List;

/**
 * GestorGeográfico se encarga de la lógica de negocio que involucra a las clases de entidad que representan
 * ubicaciones geográficas a gran escala que son utilizadas como partes de una dirección.
 * <p>
 * Estas clases entidad son: Pais, Provincia y Localidad.
 * <p>
 * Se asume que estas clases ya existen en la base de datos. Además, son inmodificables, por lo que el rol de este
 * gestor se reduce a la sola tarea de acceder a esos datos e instanciarlos.
 * <p>
 * Clase Singleton.
 */

public class GestorGeografico {

    //********************************************  ATRIBUTOS  ******************************************************

    /**
     * Instancia única del gestor según el patrón Singleton.
     */
    private static GestorGeografico instancia;

    //******************************************  CONSTRUCTORES  ****************************************************

    /**
     * Constructor por defecto. Privado por implementarse el patrón Singleton.
     */
    private GestorGeografico () {

    }

    /**
     * Obtiene la única instancia de la clase por implementación del patrón Singleton.
     *
     * @return la única instancia de la clase GestorGeografico.
     */
    public static GestorGeografico obtenerInstancia() {

        if (instancia == null) {
            instancia = new GestorGeografico();
        }

        return instancia;

    }

    //***********************************************  DAOs  ********************************************************

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de Localidad.
     *
     * @return  una instancia del DAO de Localidad.
     */
    private LocalidadDAO getLocalidadDAO() {

        LocalidadFactoryDAO factoryLocalidad = LocalidadFactoryDAO.getFactoryDAO(LocalidadFactoryDAO.POSTGRESQL);
        LocalidadDAO localidadDAO = factoryLocalidad.getLocalidadDAO();

        return localidadDAO;

    }

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de Pais.
     *
     * @return  una instancia del DAO de Pais.
     */
    private PaisDAO getPaisDAO() {

        PaisFactoryDAO factoryPais = PaisFactoryDAO.getFactoryDAO(PaisFactoryDAO.POSTGRESQL);
        PaisDAO paisDAO = factoryPais.getPaisDAO();

        return paisDAO;

    }

    /**
     * Resuelve la lógica del patrón Factory Method para obtener el DAO de Provincia.
     *
     * @return  una instancia del DAO de Provincia.
     */
    private ProvinciaDAO getProvinciaDAO() {

        ProvinciaFactoryDAO factoryProvincia = ProvinciaFactoryDAO.getFactoryDAO(ProvinciaFactoryDAO.POSTGRESQL);
        ProvinciaDAO provinciaDAO = factoryProvincia.getProvinciaDAO();

        return provinciaDAO;

    }

    //*********************************************  GETTERS  *******************************************************

    /**
     * Busca una localidad en la base de datos y retorna su instancia entidad.
     *
     * @param idLocalidad   el id de la localidad a buscar.
     * @return              la instancia del objeto entidad buscado, o null si no encuentra una Localidad con el id
     *                      usado como parámetro de búsqueda.
     */
    public Localidad getLocalidad (long idLocalidad) {

        LocalidadDAO localidadDAO = getLocalidadDAO();

        Localidad localidad = localidadDAO.getLocalidad(idLocalidad);

        return localidad;

    }

    /**
     * Busca todos los Paises existentes en la base de datos, los instancia y los retorna como DTOs.
     *
     * @return  una lista con los DTOs con los datos correspondientes a cada Pais existente en la base de datos.
     */
    public List<PaisDTO> getAllPaises() {

        PaisDAO dao = getPaisDAO();

        List<Pais> paises = dao.getPaises();

        List<PaisDTO> paisesDTO = new ArrayList<>();

        if (paises.isEmpty()) {
            return paisesDTO;
        }

        for (Pais p: paises) {

            paisesDTO.add(generarPaisDTO(p));

        }

        return paisesDTO;

    }

    /**
     * Busca en la base de datos las Provincias asociadas a un Pais, retorna los resultados como DTOs.
     *
     * @param paisDTO   el país del cual se desean obtener las Provincias.
     * @return          una lista de las Provincias existentes en la base de datos para el País buscado. Retorna una
     *                  lista vacía en el caso de que la búsqueda no arroje resultados.
     */
    public List<ProvinciaDTO> getAllProvinciasPais(PaisDTO paisDTO) {

        Pais pais = generarPais(paisDTO);

        ProvinciaDAO dao = getProvinciaDAO();

        List<Provincia> provincias = dao.getProvincias(pais);

        List<ProvinciaDTO> provinciasDTO = new ArrayList<>();

        if (provincias.isEmpty()) {
            return provinciasDTO;
        }

        for (Provincia p: provincias) {

            provinciasDTO.add(generarProvinciaDTO(p));

        }

        return provinciasDTO;

    }

    /**
     * Busca en la base de datos las Localidades asociadas a una Provincia, retorna los resultados como DTOs.
     *
     * @param provinciaDTO  la provincia de la cual se desea obtener las Localidades.
     * @return              una lista de las Localidades existentes en la base de datos para la Provincia buscada.
     *                      Retorna una lista vacía en el caso de que la búsqueda no arroje resultados.
     */
    public List<LocalidadDTO> getAllLocalidadesProvincia(ProvinciaDTO provinciaDTO) {

        Provincia provincia = generarProvincia(provinciaDTO);

        LocalidadDAO dao = getLocalidadDAO();

        List<Localidad> localidades = dao.getLocalidades(provincia);

        List<LocalidadDTO> localidadesDTO = new ArrayList<>();

        if (localidades == null) {
            return localidadesDTO;
        }

        for (Localidad l: localidades) {

            localidadesDTO.add(generarLocalidadDTO(l));

        }

        return localidadesDTO;

    }

    //************************************  TRANSFORMACIÓN A/DESDE DTOs  *********************************************

    /**
     * Genera el DTO correspondiente a un Pais.
     *
     * @param pais  el Pais para el que se desea generar un DTO.
     * @return      el DTO con los datos del Pais recibido.
     */
    public PaisDTO generarPaisDTO(Pais pais) {

        PaisDTO paisDTO = new PaisDTO();
        paisDTO.setNombre(pais.getNombre());
        paisDTO.setId_pais(pais.getId());

        return paisDTO;

    }

    /**
     * Genera un objeto entidad Pais a partir de su DTO correspondiente.
     *
     * @param paisDTO   el DTO con los datos para generar la entidad.
     * @return          un objeto entidad generado a partir de los datos recibidos.
     */
    private Pais generarPais(PaisDTO paisDTO) {

        Pais pais = new Pais();

        pais.setNombre(paisDTO.getNombre());
        pais.setId(paisDTO.getId_pais());

        return pais;

    }

    /**
     * Genera el DTO correspondiente a una Provincia.
     *
     * @param provincia     la Provincia para la cual se desea generar el DTO.
     * @return              el DTO con los datos de la Provincia recibida.
     */
    public ProvinciaDTO generarProvinciaDTO(Provincia provincia) {

        ProvinciaDTO provinciaDTO = new ProvinciaDTO();
        provinciaDTO.setId_provincia(provincia.getId());
        provinciaDTO.setNombre(provincia.getNombre());
        provinciaDTO.setPais(generarPaisDTO(provincia.getPais()));

        return provinciaDTO;

    }

    /**
     * Genera un objeto entidad Provincia a partir de su DTO correspondiente.
     *
     * @param provinciaDTO  el DTO con los datos para generar la entidad.
     * @return              un objeto entidad generado a partir de los datos recibidos.
     */
    private Provincia generarProvincia(ProvinciaDTO provinciaDTO) {

        Provincia provincia = new Provincia();

        provincia.setId(provinciaDTO.getId_provincia());
        provincia.setNombre(provinciaDTO.getNombre());

        try {

            provincia.setPais(generarPais(provinciaDTO.getPais()));

        } catch (NullPointerException e) {

            provincia.setPais(new Pais());

        }

        return provincia;

    }

    /**
     * Genera el DTO correspondiente a una Localidad.
     *
     * @param localidad     la Localidad para la cual se desea generar el DTO.
     * @return              el DTO con los datos de la Localidad recibida.
     */
    public LocalidadDTO generarLocalidadDTO(Localidad localidad) {

        LocalidadDTO localidadDTO = new LocalidadDTO();
        localidadDTO.setId_localidad(localidad.getId());
        localidadDTO.setNombre(localidad.getNombre());
        localidadDTO.setProvincia(generarProvinciaDTO(localidad.getProvincia()));

        return localidadDTO;

    }

}

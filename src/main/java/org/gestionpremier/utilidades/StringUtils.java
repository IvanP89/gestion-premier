package org.gestionpremier.utilidades;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;

public abstract class StringUtils {

    public static String todasLasPalabrasEmpezandoConMayusculas(String s) {

        String[] palabras = s.split("\\s");

        StringBuilder resultado = new StringBuilder();

        for (String p: palabras) {

            resultado.append(p.substring(0,1).toUpperCase());
            resultado.append(p.substring(1));
            resultado.append(" ");

        }

        return resultado.toString().trim();

    }

    public static LocalDate stringAFecha(String sFecha) {

        String[] segmentos = sFecha.split("/");

        LocalDate fecha = LocalDate.of(
                Integer.parseInt(segmentos[2]),
                Integer.parseInt(segmentos[1]),
                Integer.parseInt(segmentos[0])
        );

        return fecha;

    }

}

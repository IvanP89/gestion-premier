package org.gestionpremier;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.gestionpremier.interfaces.PantallaPrincipal;

public class Main {

    public static void main(String[] args) {

        FlatIntelliJLaf.setup();

        PantallaPrincipal programa = new PantallaPrincipal();

    }

}

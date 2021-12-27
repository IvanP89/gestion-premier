package org.gestionpremier.dto;

import org.gestionpremier.negocio.entidades.EstadoFactura;

import java.time.LocalDate;
import java.util.List;

public class FacturaDTO {

    private Long nro;
    private char tipo;
    private LocalDate fecha;
    private float total;
    private float iva;
    private EstadoFactura estado;
    private List<RenglonFacturaDTO> renglones;
    private Long idEstadia;
    private PasajeroDTO clientePasajero;
    private ResponsableDePagoDTO clienteResponsable;

    public Long getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Long idEstadia) {
        this.idEstadia = idEstadia;
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public List<RenglonFacturaDTO> getRenglones() {
        return renglones;
    }

    public void setRenglones(List<RenglonFacturaDTO> renglones) {
        this.renglones = renglones;
    }

    public PasajeroDTO getClientePasajero() {
        return clientePasajero;
    }

    public void setClientePasajero(PasajeroDTO clientePasajero) {
        this.clientePasajero = clientePasajero;
    }

    public ResponsableDePagoDTO getClienteResponsable() {
        return clienteResponsable;
    }

    public void setClienteResponsable(ResponsableDePagoDTO clienteResponsable) {
        this.clienteResponsable = clienteResponsable;
    }

    public float getSubtotalSinIva() {

        float subtotal = 0f;

        for (RenglonFacturaDTO r: renglones) {

            subtotal += r.getImporteUnitario() * (float) r.getCantidad();

        }

        return subtotal;

    }

    @Override
    public String toString() {

        final int LARGO_RENGLON = 129;
        final int ANCHO_COL_ITEM = 6;
        final int ANCHO_COL_DETALLE = 73;
        final int ANCHO_COL_IMPORTE_UNI = 18;
        final int ANCHO_COL_CANT = 10;
        final int ANCHO_COL_SUBTOTAL = 18;

        StringBuilder sb =  new StringBuilder();
        StringBuilder col =  new StringBuilder();

        for (int i = 0; i < LARGO_RENGLON; i++) {
            sb.append('-');
        }

        sb.append("\n\nFactura nro: ");
        sb.append(String.format("%010d", nro));
        sb.append("\n\nFecha: ");
        sb.append(fecha);
        sb.append("\n\nTipo de factura: ");
        sb.append(tipo);
        sb.append("\nCliente: ");
        if (clientePasajero != null) {

            sb.append(clientePasajero.getNombre() + " " + clientePasajero.getApellido());

            sb.append("\nCUIT: ");
            if (!clientePasajero.getCuit().equals("")) {
                sb.append(clientePasajero.getCuit());
            } else {
                sb.append("-");
            }

        } else {

            sb.append(clienteResponsable.getRazonSocial());

            sb.append("\nCUIT: ");
            sb.append(clienteResponsable.getCuit());

        }
        sb.append("\n\n");

        //*************************************************************************************************************

        for (int i = 0; i < LARGO_RENGLON; i++) {
            sb.append('-');
        }
        sb.append("\n");

        sb.append(" Item |");
        for (int i = 0; i < (ANCHO_COL_DETALLE - 7) / 2; i++) {
            col.append(" ");
        }
        col.append("Detalle");
        for (int i = 0; i < (ANCHO_COL_DETALLE - 7) / 2; i++) {
            col.append(" ");
        }
        sb.append(col.toString());
        sb.append("|");
        sb.append(" Importe unitario |");
        sb.append(" Cantidad |");
        sb.append(" Subtotal ");

        sb.append("\n");
        for (int i = 0; i < LARGO_RENGLON; i++) {
            sb.append('-');
        }
        sb.append("\n");

        //*************************************************************************************************************

        for (RenglonFacturaDTO renglon:
             renglones) {

            col.setLength(0);
            col.append("  ");
            col.append(String.format("%02d", renglon.getNro() + 1));
            col.append("  |");

            sb.append(col.toString());

            col.setLength(0);
            col.append("  ");
            col.append(renglon.getNombreConsumo());
            while(col.toString().length() < ANCHO_COL_DETALLE) {
                col.append(' ');
            }
            col.append("|");

            sb.append(col.toString());

            col.setLength(0);
            col.append("   $ ");
            if (tipo == 'A') {
                col.append(String.format("%.2f", renglon.getImporteUnitario()));
            } else {
                col.append(String.format("%.2f", renglon.getImporteUnitario() * (1f + iva)));
            }
            while(col.toString().length() < ANCHO_COL_IMPORTE_UNI) {
                col.append(' ');
            }
            col.append("|");

            sb.append(col.toString());

            col.setLength(0);
            col.append("    ");
            col.append(renglon.getCantidad());
            while(col.toString().length() < ANCHO_COL_CANT) {
                col.append(' ');
            }
            col.append("|");

            sb.append(col.toString());

            col.setLength(0);
            col.append(" $ ");
            if (tipo == 'A') {
                col.append(String.format("%.2f", (float) renglon.getCantidad() * renglon.getImporteUnitario()));
            } else {
                col.append(String.format("%.2f", (float) renglon.getCantidad() * renglon.getImporteUnitario() * (1f + iva)));
            }
            while(col.toString().length() < ANCHO_COL_SUBTOTAL) {
                col.append(' ');
            }

            sb.append(col.toString());

            sb.append("\n");

        }

        if (renglones.size() < 10) {

            int cantRestantes = 10 - renglones.size();

            for (int i = 0; i < cantRestantes; i++) {

                col.setLength(0);
                col.append("  ");
                col.append(String.format("%02d", renglones.get((renglones.size()-1)).getNro() + i + 2));
                col.append("  |");
                for (int j = 0; j < ANCHO_COL_DETALLE; j++) {
                    col.append(" ");
                }
                col.append("|");
                for (int j = 0; j < ANCHO_COL_IMPORTE_UNI; j++) {
                    col.append(" ");
                }
                col.append("|");
                for (int j = 0; j < ANCHO_COL_CANT; j++) {
                    col.append(" ");
                }
                col.append("|");

                sb.append(col.toString());
                sb.append("\n");

            }

        }

        //**********************************************************************************************************

        for (int i = 0; i < LARGO_RENGLON; i++) {
            sb.append('-');
        }
        sb.append("\n");

        col.setLength(0);
        while (col.toString().length() < LARGO_RENGLON - 42) {
            col.append(' ');
        }
        sb.append(col.toString());
        float subtotal = getSubtotalSinIva();
        if (tipo == 'B') {
            subtotal = subtotal * (1f + iva);
        }
        sb.append("  Subtotal: " + "$ " + String.format("%.2f", subtotal));

        sb.append("\n");

        col.setLength(0);
        while (col.toString().length() < LARGO_RENGLON - 42) {
            col.append(' ');
        }
        sb.append(col.toString());

        String totalIva = "     n/c";
        if (tipo == 'A') {
            totalIva = "     $ " + String.format("%.2f", getSubtotalSinIva() * iva);
        }

        sb.append("  IVA: " + totalIva);

        sb.append("\n");

        col.setLength(0);
        while (col.toString().length() < LARGO_RENGLON - 42) {
            col.append('#');
        }
        sb.append(col.toString());
        sb.append("  TOTAL: " + "   $ " + String.format("%.2f", this.total));

        sb.append("\n");

        for (int i = 0; i < LARGO_RENGLON; i++) {
            sb.append('-');
        }

        return sb.toString();

    }

}

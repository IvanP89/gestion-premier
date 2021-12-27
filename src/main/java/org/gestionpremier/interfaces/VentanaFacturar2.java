package org.gestionpremier.interfaces;

import org.gestionpremier.dto.FacturaDTO;
import org.gestionpremier.dto.PasajeroDTO;
import org.gestionpremier.dto.RenglonFacturaDTO;
import org.gestionpremier.dto.ResponsableDePagoDTO;
import org.gestionpremier.excepciones.DatosErroneosPasajeroException;
import org.gestionpremier.excepciones.DatosErroneosResponsablePagoException;
import org.gestionpremier.excepciones.IdErroneoException;
import org.gestionpremier.negocio.gestores.GestorCaja;
import org.gestionpremier.negocio.entidades.EstadoFactura;
import org.gestionpremier.negocio.gestores.GestorEstadia;
import org.hibernate.HibernateException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaFacturar2 extends JDialog {

    //***************************************************************************************************************
    //                                     ATRIBUTOS - ELEMENTOS GRÁFICOS
    //***************************************************************************************************************

    private JPanel panelPrincipal;
    private JTable tabla;
    private JCheckBox checkBoxPagarEstadia;
    private JTextField campoValorEstadia;
    private JLabel labelCuit;
    private JLabel labelRazonSocial;
    private JRadioButton radioBtnTipoA;
    private JRadioButton radioBtnTipoB;
    private JTextField campoIVA;
    private JLabel labelTotalAPagar;
    private JLabel labelSubtotal;
    private JLabel labelIVA;
    private JButton btnCancelar;
    private JButton btnAceptar;
    private JLabel labelIconoPdf;
    private JLabel labelTotalConsumos;
    private JLabel labelTotalConsumosSeleccionados;
    private JScrollPane panelTabla;

    //***************************************************************************************************************
    //                                           ATRIBUTOS - LÓGICA
    //***************************************************************************************************************

    private FacturaDTO facturaDTO;

    //***************************************************************************************************************
    //                                                CONSTRUCTORES
    //***************************************************************************************************************

    public VentanaFacturar2(Frame owner, boolean modal, FacturaDTO facturaDTO) {

        super(owner, modal);

        if (facturaDTO == null) {
            String mensajeError = "Ventana Facturar2 esperaba recibir un objeto FacturaDTO pero recibió null.";
            JOptionPane.showMessageDialog(null, mensajeError);
            throw new IllegalArgumentException(mensajeError);
        }

        this.facturaDTO = facturaDTO;
        this.facturaDTO.setTipo('A'); // Luego inicializarComponentes() lo cambia a B si no hay cuit.

        // DATOS DE PRUEBA RANDOMLOCALES
//        mockLlenarFacturaDTO();

        this.inicializarComponentes();
        this.agregarListeners();
        this.configurarVentana();

    }

    //***************************************************************************************************************
    //                                  MÉTODOS DE CREACIÓN LLAMADOS EN EL CONSTRUCTOR
    //***************************************************************************************************************

    private void inicializarComponentes() {

        this.cargarTabla();

        btnAceptar.setBackground(new Color(10, 132, 255));
        btnAceptar.setForeground(Color.WHITE);

        ButtonGroup grupoRadioBtnTipoFactura = new ButtonGroup();
        grupoRadioBtnTipoFactura.add(radioBtnTipoA);
        grupoRadioBtnTipoFactura.add(radioBtnTipoB);

        if (((ModeloTablaFacturar2) tabla.getModel()).hayEstadia()) {

            checkBoxPagarEstadia.setEnabled(true);
            campoValorEstadia.setText( "$" + String.format("%.2f", ((ModeloTablaFacturar2) tabla.getModel()).getValorEstadia()) );

        } else {

            checkBoxPagarEstadia.setEnabled(false);
            campoValorEstadia.setText("");

        }

        if (facturaDTO.getClientePasajero() != null) {
            if (facturaDTO.getClientePasajero().getCuit() == null || facturaDTO.getClientePasajero().getCuit().equals("")) {
                radioBtnTipoA.setEnabled(false);
                radioBtnTipoB.setSelected(true);
                facturaDTO.setTipo('B');
            }
        }

        this.cargarLabels();

    }

    private void agregarListeners() {

        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aceptar();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancelar();
            }
        });

        checkBoxPagarEstadia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (checkBoxPagarEstadia.isSelected()) {
                    ((ModeloTablaFacturar2) tabla.getModel()).pagarEstadiaSeleccionado(true);
                } else {
                    ((ModeloTablaFacturar2) tabla.getModel()).pagarEstadiaSeleccionado(false);
                }

                cargarLabels();

            }
        });

        tabla.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                cargarLabels();
            }
        });

        radioBtnTipoA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioBtnTipoA.isSelected()) {
                    facturaDTO.setTipo('A');
                }
                ((ModeloTablaFacturar2) tabla.getModel()).fireTableDataChanged();
                cargarLabels();
            }
        });

        radioBtnTipoB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radioBtnTipoB.isSelected()) {
                    facturaDTO.setTipo('B');
                }
                ((ModeloTablaFacturar2) tabla.getModel()).fireTableDataChanged();
                cargarLabels();
            }
        });

        /** Cerrar cuando se presiona la tecla ESC **/
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        this.getRootPane().registerKeyboardAction(e -> this.cancelar(), k, w);

    }

    private void configurarVentana() {

        setContentPane(panelPrincipal);
        setTitle("Facturación");
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga del ícono del programa en la barra superior.
        URL pathRecurso = this.getClass().getClassLoader().getResource("icons/hotel.png");
        this.setIconImage(new ImageIcon(pathRecurso).getImage());

        setVisible(true);

    }

    private void cargarLabels() {

        try {

            if (facturaDTO.getClientePasajero() == null) {

                labelCuit.setText("CUIT: " + facturaDTO.getClienteResponsable().getCuit());
                labelRazonSocial.setText(facturaDTO.getClienteResponsable().getRazonSocial());

            } else {

                if (facturaDTO.getClientePasajero().getCuit() != null) {
                    labelCuit.setText("CUIT: " + facturaDTO.getClientePasajero().getCuit());
                } else {
                    labelCuit.setText("CUIT: no posee");
                }
                labelRazonSocial.setText(facturaDTO.getClientePasajero().getNombre() + " " + facturaDTO.getClientePasajero().getApellido());

            }

        } catch (NullPointerException e) {

            JOptionPane.showMessageDialog(null, "Error\n\nVentanaFacturar2: la factura no tiene ningún cliente asociado.");
            cancelar();

        }

        if (radioBtnTipoA.isSelected()) {

            campoIVA.setText( (facturaDTO.getIva() * 100) + "%" );

            labelTotalConsumos.setText(String.format("%.2f", calcularTotalConsumosPorPAgar()));
            labelTotalConsumosSeleccionados.setText(String.format("%.2f", calcularTotalConsumosSeleccionados()));
            labelSubtotal.setText(String.format("%.2f", calcularTotalConsumosSeleccionados()));
            labelIVA.setText(String.format("%.2f", calcularIva()));

            if (checkBoxPagarEstadia.isEnabled()) {
                campoValorEstadia.setText( "$" + String.format("%.2f", ((ModeloTablaFacturar2) tabla.getModel()).getValorEstadia()) );
            }

        } else {

            campoIVA.setText("n/c");

            labelTotalConsumos.setText(String.format("%.2f", calcularTotalConsumosPorPAgar() * (1 + facturaDTO.getIva())));
            labelTotalConsumosSeleccionados.setText(String.format("%.2f", calcularTotalConsumosSeleccionados() * (1 + facturaDTO.getIva())));
            labelSubtotal.setText(String.format("%.2f", calcularTotalConsumosSeleccionados() * (1 + facturaDTO.getIva())));
            labelIVA.setText("n/c");

            if (checkBoxPagarEstadia.isEnabled()) {
                campoValorEstadia.setText( "$" + String.format("%.2f", ((ModeloTablaFacturar2) tabla.getModel()).getValorEstadia() * (1 + facturaDTO.getIva())) );
            }

        }


        labelTotalAPagar.setText(String.format("%.2f", calcularTotal()));


    }

    private void cargarTabla() {

        instanciarTabla();

        tabla.setModel(new ModeloTablaFacturar2(facturaDTO));

        configurarColumnaEditableTabla();

        // CENTRA EL TEXTO DE LAS CELDAS
        DefaultTableCellRenderer rendererAlineacionCentro = new DefaultTableCellRenderer();
        rendererAlineacionCentro.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < tabla.getColumnModel().getColumnCount(); i++) {

            tabla.getColumnModel().getColumn(i).setCellRenderer(rendererAlineacionCentro);

        }

        tabla.getColumnModel().getColumn(0).setPreferredWidth(250);

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(true);
        tabla.setGridColor(Color.GRAY);
        tabla.getTableHeader().setReorderingAllowed(false);

    }

    private void instanciarTabla() {

        tabla = new JTable() {

            /** SE SOBREESCRIBE ESTE MÉTODO PARA COLOREAR LAS FILAS DE FORMA ALTERNADA. **/
            public Component prepareRenderer(TableCellRenderer renderer, int fila, int columna) {

                Component c = super.prepareRenderer(renderer, fila, columna);

                if (!isRowSelected(fila)) {

                    c.setBackground(fila % 2 != 0 ? getBackground() : Color.LIGHT_GRAY);
                    ((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.GRAY));

                }

                return c;

            }

        };

        panelTabla.getViewport().add(tabla);

    }

    private void configurarColumnaEditableTabla() {

        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {

            @Override
            public boolean stopCellEditing() {

                int columna = tabla.getEditingColumn();
                int fila = tabla.getEditingRow();

                final int INDICE_COL_CANT_CONSUMOS_A_PAGAR = 3;
                final int INDICE_COL_CANT_CONSUMOS_POR_PAGAR = 2;

                if (columna == INDICE_COL_CANT_CONSUMOS_A_PAGAR) {


                    Object valor = this.getCellEditorValue();

                    int cantConsumosAPagar;

                    try {
                        cantConsumosAPagar = Integer.parseInt((String) valor);
                    } catch (NumberFormatException e) { // EL USUARIO INGRESÓ LETRAS O COSAS NO CASTEABLES A INT.
                        cantConsumosAPagar = -1;
                    }

                    int cantConsumosPorPagar = (int) tabla.getModel().getValueAt(fila, INDICE_COL_CANT_CONSUMOS_POR_PAGAR);

                    if ( cantConsumosAPagar <= cantConsumosPorPagar && cantConsumosAPagar >= 0) {

                        return super.stopCellEditing();

                    } else {

                        JOptionPane.showMessageDialog(null, "Valor no permitido. Por favor ingrese un número entero, cuya cantidad no exceda la cantidad de consumos pendientes de pago o sea menor a cero.");
                        return false;

                    }

                } else {

                    return super.stopCellEditing();

                }

            }

            @Override
            public JTextField getComponent() {
                return (JTextField) super.getComponent();
            }

        };

        tabla.setDefaultEditor(Object.class, editor);

    }

    //***************************************************************************************************************
    //                                      MÉTODOS ACCIONADOS CON LOS BOTONES
    //***************************************************************************************************************

    private void aceptar() {

        if (validarCampos()) {

            aplicarCambiosFactura();

            try {

                Long nroFactura = GestorCaja.obtenerInstancia().insertarFactura(facturaDTO);

                facturaDTO.setNro(nroFactura);

                GestorEstadia.obtenerInstancia().reducirDeuda(facturaDTO.getIdEstadia(), facturaDTO.getTotal() / (1f + facturaDTO.getIva()));

                this.mostrarFacturaImpresa();

                this.cancelar();

            } catch (DatosErroneosPasajeroException e1) {

                JOptionPane.showMessageDialog(null, "Error al crear la factura: Los datos del pasajero responsable asociado no son correctos.");

            } catch (DatosErroneosResponsablePagoException e2) {

                JOptionPane.showMessageDialog(null, "Error al crear la factura: Los datos del responsable de pago asociado no son correctos.");

            } catch (IdErroneoException e3) {

                JOptionPane.showMessageDialog(null, "Error al crear la factura: Los datos de la estadía asociada no son correctos.");

            } catch (HibernateException e4) {

                JOptionPane.showMessageDialog(null, "Se ha producido un error al intentar acceder a la base de datos para insertar la factura.\n\n" + e4.getMessage());

            }

        }

    }

    private void cancelar() {

        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

    }

    //***************************************************************************************************************
    //                                           VALIDACIÓN DE CAMPOS
    //***************************************************************************************************************

    private boolean validarCampos() {

        try {

            if (
                    !(facturaDTO != null
                            && facturaDTO.getRenglones().size() > 0
                            && ((ModeloTablaFacturar2) tabla.getModel()).getCantConsumosAPagar().size() > 0
                            && facturaDTO.getRenglones().size() == ((ModeloTablaFacturar2) tabla.getModel()).getCantConsumosAPagar().size()
                            && (facturaDTO.getClientePasajero() != null || facturaDTO.getClienteResponsable() != null)
                            && facturaDTO.getIdEstadia() >= 0)
                ) {

                JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: los campos de la factura no son válidos. (En VentanaFacturar2.aceptar)");
                return false;

            }

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: los campos de la factura no son válidos. (En VentanaFacturar2.aceptar)");
            return false;

        }

        List<Integer> consumosAFacturar = ((ModeloTablaFacturar2) tabla.getModel()).getCantConsumosAPagar();
        boolean hayConsumosAFacturar = false;

        for (Integer c: consumosAFacturar) {

            if (c > 0) {
                hayConsumosAFacturar = true;
                break;
            }

        }

        if (!hayConsumosAFacturar) {

            JOptionPane.showMessageDialog(null, "Debe especificar una cantidad a facturar para al menos un consumo.");
            return false;

        }

        return true;

    }

    //***************************************************************************************************************
    //                                           OTROS MÉTODOS
    //***************************************************************************************************************

    private float calcularTotalConsumosPorPAgar() {

        float total = 0;

        for (RenglonFacturaDTO r: facturaDTO.getRenglones()) {

            total += r.getCantidad() * r.getImporteUnitario();

        }

        return total;

    }

    private float calcularTotalConsumosSeleccionados() {

        ModeloTablaFacturar2 modeloTabla = ((ModeloTablaFacturar2) tabla.getModel());

        float total = 0;

        for (int i = 0; i < facturaDTO.getRenglones().size(); i++) {

            total += facturaDTO.getRenglones().get(i).getImporteUnitario() * modeloTabla.getCantConsumosAPagar().get(i);

        }

        return total;

    }

    private float calcularIva() {

        return calcularTotalConsumosSeleccionados() * facturaDTO.getIva();

    }

    private float calcularTotal() {

        return calcularTotalConsumosSeleccionados() + calcularIva();

    }

    /**
     * Actualiza la FacturaDTO con los cambios que seleccionó el usuario.
     */
    private void aplicarCambiosFactura() {

        this.generarListaRenglones();

        facturaDTO.setTotal( calcularTotal() );

        if (radioBtnTipoA.isSelected()) {
            facturaDTO.setTipo('A');
        } else {
            facturaDTO.setTipo('B');
        }

        facturaDTO.setEstado(EstadoFactura.IMPAGA);
        facturaDTO.setFecha(LocalDate.now());

    }

    /**
     * Actualiza la cantidad a pagar por consumo en los renglones de la factura.
     *
     * Por cada renglón de la factura, reemplaza la cantidad total de consumos por facturar por la cantidad a facturar
     * que haya seleccionado el usuario. Si el usuario seleccionó una cantidad igual a cero para un consumo, el
     * renglón correspondiente a ese consumo se elimina de la factura.
     */
    private void generarListaRenglones() {

        ModeloTablaFacturar2 modeloTabla = (ModeloTablaFacturar2) tabla.getModel();

        int iteradorRenglon = 0;
        int cantConsumosAPagar;
        RenglonFacturaDTO renglonActual;

        while (iteradorRenglon < facturaDTO.getRenglones().size()) {

            renglonActual = facturaDTO.getRenglones().get(iteradorRenglon);
            cantConsumosAPagar = modeloTabla.getCantConsumosAPagar().get(iteradorRenglon);

            if (cantConsumosAPagar == 0) {

                facturaDTO.getRenglones().remove(renglonActual);
                modeloTabla.getCantConsumosAPagar().remove(iteradorRenglon);

                if (iteradorRenglon == 0 && modeloTabla.hayEstadia()) {

                    modeloTabla.setHayEstadia(false);

                }

                // NÓTESE, iteradorRenglon NO SE INCREMENTA SI ENTRÓ A ESTA PARTE DEL IF.

            } else {

                facturaDTO.getRenglones().get(iteradorRenglon).setCantidad(cantConsumosAPagar);
                facturaDTO.getRenglones().get(iteradorRenglon).setNro(iteradorRenglon);

                iteradorRenglon++;

            }

        }

    }

    private void mostrarFacturaImpresa() {

        String mensajeFormateado = "<html>" + facturaDTO.toString().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;") + "</html>";
        JLabel resLabel = new JLabel(mensajeFormateado);
        resLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resLabel);
        scrollPane.setPreferredSize( new Dimension( 1050, 640 ) );
        JOptionPane.showMessageDialog( this, scrollPane, "Se ha creado la factura de forma exitosa con número = " + facturaDTO.getNro(), JOptionPane.PLAIN_MESSAGE );

//        JOptionPane.showMessageDialog(this, "Se ha creado la factura de forma exitosa con id = " + id + "." + "\n\n" + facturaDTO);

    }

    //***************************************************************************************************************
    //                                           METODOS DE PRUEBA A BORRAR
    //***************************************************************************************************************

    private void mockLlenarFacturaDTO() {

        if (facturaDTO == null) {
            facturaDTO = new FacturaDTO();
        }
        if (facturaDTO.getRenglones() == null) {
            facturaDTO.setRenglones(new ArrayList<>());
        }

        facturaDTO.setNro(123456l);
        facturaDTO.setEstado(EstadoFactura.IMPAGA);
        facturaDTO.setFecha(LocalDate.now());
        facturaDTO.setIdEstadia(123456l);
        facturaDTO.setIva(0.21f);

        ResponsableDePagoDTO responsableDePagoDTO = new ResponsableDePagoDTO();
        responsableDePagoDTO.setCuit("20-35555555-3");
        responsableDePagoDTO.setId(12354987l);
        responsableDePagoDTO.setRazonSocial("GRUPO 10C SRL");

        PasajeroDTO pasajeroDTO = new PasajeroDTO();
        pasajeroDTO.setNombre("JUAN");
        pasajeroDTO.setApellido("PEREZ");
        pasajeroDTO.setId(123l);

        facturaDTO.setClienteResponsable(responsableDePagoDTO);
//        facturaDTO.setClientePasajero(pasajeroDTO);

        // CON ESTADIA
        facturaDTO.getRenglones().add(new RenglonFacturaDTO());
        facturaDTO.getRenglones().get(0).setId(0l);
        facturaDTO.getRenglones().get(0).setFacturaDTO(facturaDTO);
        facturaDTO.getRenglones().get(0).setNro(0);
        facturaDTO.getRenglones().get(0).setNombreConsumo("Estadía Individual Estándar x 5 días");
        facturaDTO.getRenglones().get(0).setImporteUnitario(21000f);
        facturaDTO.getRenglones().get(0).setCantidad(1);


        final int CANT_RENGLONES_DE_PRUEBA = 5;

        for (int i = 1; i < CANT_RENGLONES_DE_PRUEBA; i++) {

            facturaDTO.getRenglones().add(new RenglonFacturaDTO());
            facturaDTO.getRenglones().get(i).setId((long)i);
            facturaDTO.getRenglones().get(i).setFacturaDTO(facturaDTO);
            facturaDTO.getRenglones().get(i).setNro(i);
            facturaDTO.getRenglones().get(i).setNombreConsumo("Producto " + i);
            facturaDTO.getRenglones().get(i).setImporteUnitario(((float) (i + 1)) * 2.75f);
            facturaDTO.getRenglones().get(i).setCantidad((i+1)*10);

        }

    }


}

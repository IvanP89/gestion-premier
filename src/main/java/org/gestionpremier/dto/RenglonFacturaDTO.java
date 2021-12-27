package org.gestionpremier.dto;


public class RenglonFacturaDTO {

    private Long id;
    private int nro;
    private String nombreConsumo;
    private float importeUnitario;
    private int cantidad;
    private Long consumoId;
    private FacturaDTO facturaDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public String getNombreConsumo() {
        return nombreConsumo;
    }

    public void setNombreConsumo(String nombreConsumo) {
        this.nombreConsumo = nombreConsumo;
    }

    public float getImporteUnitario() {
        return importeUnitario;
    }

    public void setImporteUnitario(float importeUnitario) {
        this.importeUnitario = importeUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getConsumoId() {
        return consumoId;
    }

    public void setConsumoId(Long consumoId) {
        this.consumoId = consumoId;
    }

    public FacturaDTO getFacturaDTO() {
        return facturaDTO;
    }

    public void setFacturaDTO(FacturaDTO facturaDTO) {
        this.facturaDTO = facturaDTO;
    }
}

package ar.edu.unju.fi.util;

/**
 * Enum que representa los posibles estados de pago de una orden.
 */
public enum EstadoPago {
    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    CANCELADO("Cancelado");
    
    private final String descripcion;
    
    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
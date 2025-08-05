package ar.edu.unju.fi.entity.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos para métodos de pago en el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodData {
    private String name;
    private int count;
}
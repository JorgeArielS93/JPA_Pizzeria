package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para datos de métodos de pago en el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDataDTO {
    private String name;
    private int count;
}
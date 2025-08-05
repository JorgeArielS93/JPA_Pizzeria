package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para datos de ventas de pizzas en el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaSalesDataDTO {
    private int id;
    private String name;
    private int quantity;
    private double revenue;
}
package ar.edu.unju.fi.entity.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de ventas de pizzas para el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaSalesData {
    private int id;
    private String name;
    private int quantity;
    private double revenue;
}
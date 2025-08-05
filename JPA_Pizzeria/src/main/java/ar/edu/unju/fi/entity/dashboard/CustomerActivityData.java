package ar.edu.unju.fi.entity.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de actividad de clientes para el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerActivityData {
    private int id;
    private String name;
    private String email;
    private int orderCount;
    private double totalSpent;
}
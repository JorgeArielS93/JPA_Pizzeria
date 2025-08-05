package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para datos de actividad de clientes en el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerActivityDataDTO {
    private int id;
    private String name;
    private String email;
    private int orderCount;
    private double totalSpent;
}
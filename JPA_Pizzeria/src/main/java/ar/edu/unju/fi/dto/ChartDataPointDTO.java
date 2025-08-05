package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para datos de puntos en gráficos del dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataPointDTO {
    private String label;
    private double value;
}
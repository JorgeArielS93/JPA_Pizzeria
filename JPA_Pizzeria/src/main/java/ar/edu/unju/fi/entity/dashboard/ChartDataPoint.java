package ar.edu.unju.fi.entity.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos para puntos en gráficos del dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataPoint {
    private String label;
    private double value;
}
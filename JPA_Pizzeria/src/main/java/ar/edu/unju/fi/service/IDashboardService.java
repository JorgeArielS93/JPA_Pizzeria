package ar.edu.unju.fi.service;

import java.time.LocalDate;
import java.util.List;

import ar.edu.unju.fi.controller.AdminController.ChartDataPoint;
import ar.edu.unju.fi.controller.AdminController.CustomerActivityData;
import ar.edu.unju.fi.controller.AdminController.PaymentMethodData;
import ar.edu.unju.fi.controller.AdminController.PizzaSalesData;
import ar.edu.unju.fi.dto.OrderDTO;

/**
 * Interfaz para el servicio de estadísticas y métricas del dashboard administrativo
 */
public interface IDashboardService {
    
    /**
     * Calcula la fecha de inicio según el período seleccionado
     * @param period Periodo (day, week, month)
     * @return Fecha de inicio para filtrar
     */
    LocalDate calculateStartDate(String period);
    
    /**
     * Filtra las órdenes por fecha
     * @param orders Lista de órdenes
     * @param startDate Fecha de inicio
     * @return Lista filtrada
     */
    List<OrderDTO> filterOrdersByDate(List<OrderDTO> orders, LocalDate startDate);
    
    /**
     * Calcula el total de pedidos en un período
     * @param orders Lista de órdenes
     * @return Número total de pedidos
     */
    int calculateTotalOrders(List<OrderDTO> orders);
    
    /**
     * Calcula los ingresos totales en un período
     * @param orders Lista de órdenes
     * @return Ingresos totales
     */
    double calculateTotalRevenue(List<OrderDTO> orders);
    
    /**
     * Calcula el número de clientes activos en un período
     * @param orders Lista de órdenes
     * @return Número de clientes activos
     */
    long calculateActiveCustomers(List<OrderDTO> orders);
    
    /**
     * Calcula el valor promedio de pedido en un período
     * @param orders Lista de órdenes
     * @return Valor promedio
     */
    double calculateAverageOrderValue(List<OrderDTO> orders);
    
    /**
     * Calcula las pizzas más vendidas en un período
     * @param orders Lista de órdenes
     * @param limit Límite de resultados (top N)
     * @return Lista de pizzas más vendidas
     */
    List<PizzaSalesData> calculateTopPizzas(List<OrderDTO> orders, int limit);
    
    /**
     * Calcula los clientes más frecuentes en un período
     * @param orders Lista de órdenes
     * @param limit Límite de resultados (top N)
     * @return Lista de clientes más frecuentes
     */
    List<CustomerActivityData> calculateTopCustomers(List<OrderDTO> orders, int limit);
    
    /**
     * Genera datos para el gráfico de ventas
     * @param orders Lista de órdenes
     * @param period Periodo (day, week, month)
     * @return Datos para el gráfico
     */
    List<ChartDataPoint> generateSalesChartData(List<OrderDTO> orders, String period);
    
    /**
     * Genera datos para el gráfico de métodos de pago
     * @param orders Lista de órdenes
     * @return Datos para el gráfico
     */
    List<PaymentMethodData> generatePaymentMethodsData(List<OrderDTO> orders);
    
}
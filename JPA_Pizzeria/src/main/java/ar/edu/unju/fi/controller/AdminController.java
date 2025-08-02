package ar.edu.unju.fi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.service.IDashboardService;
import ar.edu.unju.fi.service.IOrderService;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para la sección de administración
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminController {
    
    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private IDashboardService dashboardService;
    
    /**
     * Muestra el panel de administración con estadísticas y métricas
     * @param period Periodo de tiempo para las estadísticas (day, week, month)
     * @param model El modelo para pasar datos a la vista
     * @return La vista del panel de administración
     */
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name = "period", defaultValue = "day") String period, Model model) {
        log.info("Accediendo al panel de administración con período: {}", period);
        
        // Obtener todas las órdenes
        List<OrderDTO> allOrders = orderService.getAllOrders();
        
        // Filtrar por período
        LocalDate startDate = dashboardService.calculateStartDate(period);
        List<OrderDTO> filteredOrders = dashboardService.filterOrdersByDate(allOrders, startDate);
        
        // Calcular estadísticas generales
        model.addAttribute("totalOrders", dashboardService.calculateTotalOrders(filteredOrders));
        model.addAttribute("totalRevenue", dashboardService.calculateTotalRevenue(filteredOrders));
        model.addAttribute("activeCustomers", dashboardService.calculateActiveCustomers(filteredOrders));
        model.addAttribute("averageOrderValue", dashboardService.calculateAverageOrderValue(filteredOrders));
        
        // Calcular pizzas más vendidas y clientes más frecuentes
        model.addAttribute("topPizzas", dashboardService.calculateTopPizzas(filteredOrders, 5));
        model.addAttribute("topCustomers", dashboardService.calculateTopCustomers(filteredOrders, 5));
        
        // Generar datos para gráficos
        model.addAttribute("salesData", dashboardService.generateSalesChartData(allOrders, period));
        model.addAttribute("paymentMethods", dashboardService.generatePaymentMethodsData(filteredOrders));
        
        // Agregar el período actual al modelo para marcar el botón correcto
        model.addAttribute("currentPeriod", period);
        
        return "admin/dashboard";
    }
    
    /**
     * Clase interna para datos de ventas de pizzas
     */
    public static class PizzaSalesData {
        public int id;
        public String name;
        public int quantity;
        public double revenue;
        
        // Getters para Thymeleaf
        public int getId() { return id; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getRevenue() { return revenue; }
    }
    
    /**
     * Clase interna para datos de actividad de clientes
     */
    public static class CustomerActivityData {
        public int id;
        public String name;
        public String email;
        public int orderCount;
        public double totalSpent;
        
        // Getters para Thymeleaf
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getOrderCount() { return orderCount; }
        public double getTotalSpent() { return totalSpent; }
    }
    
    /**
     * Clase interna para datos de gráficos
     */
    public static class ChartDataPoint {
        private String label;
        private double value;
        
        public ChartDataPoint(String label, double value) {
            this.label = label;
            this.value = value;
        }
        
        // Getters para Thymeleaf y serialización JSON
        public String getLabel() { return label; }
        public double getValue() { return value; }
    }
    
    /**
     * Clase interna para datos de métodos de pago
     */
    public static class PaymentMethodData {
        private String name;
        private int count;
        
        public PaymentMethodData(String name, int count) {
            this.name = name;
            this.count = count;
        }
        
        // Getters para Thymeleaf y serialización JSON
        public String getName() { return name; }
        public int getCount() { return count; }
    }
}
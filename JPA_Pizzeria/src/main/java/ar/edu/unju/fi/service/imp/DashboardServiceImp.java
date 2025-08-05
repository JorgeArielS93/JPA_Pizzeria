package ar.edu.unju.fi.service.imp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.ChartDataPointDTO;
import ar.edu.unju.fi.dto.CustomerActivityDataDTO;
import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.dto.PaymentMethodDataDTO;
import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.dto.PizzaSalesDataDTO;
import ar.edu.unju.fi.service.IDashboardService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de estadísticas y métricas para el dashboard
 */
@Service
@Slf4j
public class DashboardServiceImp implements IDashboardService {

    @Override
    public LocalDate calculateStartDate(String period) {
        LocalDate today = LocalDate.now();
        
        switch (period) {
            case "week":
                return today.minusDays(7);
            case "month":
                return today.minusMonths(1);
            case "day":
            default:
                return today;
        }
    }

    @Override
    public List<OrderDTO> filterOrdersByDate(List<OrderDTO> orders, LocalDate startDate) {
        return orders.stream()
                .filter(order -> order.getDate() != null && 
                                !order.getDate().isBefore(startDate))
                .collect(Collectors.toList());
    }

    @Override
    public int calculateTotalOrders(List<OrderDTO> orders) {
        return orders.size();
    }

    @Override
    public double calculateTotalRevenue(List<OrderDTO> orders) {
        return orders.stream()
                .mapToDouble(OrderDTO::getTotal)
                .sum();
    }

    @Override
    public long calculateActiveCustomers(List<OrderDTO> orders) {
        return orders.stream()
                .filter(order -> order.getCustomer() != null)
                .map(order -> order.getCustomer().getIdCustomer())
                .distinct()
                .count();
    }

    @Override
    public double calculateAverageOrderValue(List<OrderDTO> orders) {
        int totalOrders = orders.size();
        double totalRevenue = calculateTotalRevenue(orders);
        return totalOrders > 0 ? totalRevenue / totalOrders : 0;
    }

    @Override
    public List<PizzaSalesDataDTO> calculateTopPizzas(List<OrderDTO> orders, int limit) {
        // Mapa para rastrear ventas por pizza
        Map<Integer, PizzaSalesDataDTO> pizzaSalesMap = new HashMap<>();
        
        // Procesar todos los pedidos y sus ítems
        orders.forEach(order -> {
            if (order.getItems() != null) {
                order.getItems().forEach(item -> {
                    if (item.getPizza() != null && item.getIdPizza() != null) {
                        int pizzaId = item.getIdPizza();
                        PizzaDTO pizza = item.getPizza();
                        double itemRevenue = item.getPrice() * item.getQuantity();
                        
                        // Actualizar o crear datos de ventas para esta pizza
                        if (pizzaSalesMap.containsKey(pizzaId)) {
                            PizzaSalesDataDTO data = pizzaSalesMap.get(pizzaId);
                            data.setQuantity(data.getQuantity() + item.getQuantity());
                            data.setRevenue(data.getRevenue() + itemRevenue);
                        } else {
                            PizzaSalesDataDTO data = new PizzaSalesDataDTO();
                            data.setId(pizzaId);
                            data.setName(pizza.getName());
                            data.setQuantity(item.getQuantity());
                            data.setRevenue(itemRevenue);
                            pizzaSalesMap.put(pizzaId, data);
                        }
                    }
                });
            }
        });
        
        // Convertir a lista y ordenar por cantidad vendida (descendente)
        return pizzaSalesMap.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getQuantity(), p1.getQuantity()))
                .limit(limit)  // Limitar a las N pizzas más vendidas
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerActivityDataDTO> calculateTopCustomers(List<OrderDTO> orders, int limit) {
        // Mapa para rastrear actividad por cliente
        Map<Integer, CustomerActivityDataDTO> customerActivityMap = new HashMap<>();
        
        // Procesar todos los pedidos
        orders.forEach(order -> {
            if (order.getCustomer() != null && order.getCustomer().getIdCustomer() != null) {
                int customerId = order.getCustomer().getIdCustomer();
                CustomerDTO customer = order.getCustomer();
                double orderTotal = order.getTotal();
                
                // Actualizar o crear datos de actividad para este cliente
                if (customerActivityMap.containsKey(customerId)) {
                    CustomerActivityDataDTO data = customerActivityMap.get(customerId);
                    data.setOrderCount(data.getOrderCount() + 1);
                    data.setTotalSpent(data.getTotalSpent() + orderTotal);
                } else {
                    CustomerActivityDataDTO data = new CustomerActivityDataDTO();
                    data.setId(customerId);
                    data.setName(customer.getName());
                    data.setEmail(customer.getEmail());
                    data.setOrderCount(1);
                    data.setTotalSpent(orderTotal);
                    customerActivityMap.put(customerId, data);
                }
            }
        });
        
        // Convertir a lista y ordenar por número de pedidos (descendente)
        return customerActivityMap.values().stream()
                .sorted((c1, c2) -> Integer.compare(c2.getOrderCount(), c1.getOrderCount()))
                .limit(limit)  // Limitar a los N clientes más frecuentes
                .collect(Collectors.toList());
    }

    @Override
    public List<ChartDataPointDTO> generateSalesChartData(List<OrderDTO> orders, String period) {
        List<ChartDataPointDTO> salesData = new ArrayList<>();
        DateTimeFormatter formatter;
        
        // Configurar según el período
        switch (period) {
            case "month":
                // Datos por semana para el último mes
                formatter = DateTimeFormatter.ofPattern("'Semana' w");
                // Aquí iría la lógica para agrupar por semana
                
                // Simulación básica de datos para las últimas 4 semanas
                LocalDate today = LocalDate.now();
                for (int i = 4; i >= 0; i--) {
                    LocalDate weekStart = today.minusWeeks(i);
                    String label = weekStart.format(formatter);
                    
                    // En un sistema real, calcularíamos las ventas semanales
                    // Aquí usamos datos aleatorios para simulación
                    double weeklySales = Math.random() * 5000 + 1000;
                    
                    salesData.add(new ChartDataPointDTO(label, weeklySales));
                }
                break;
                
            case "week":
                // Datos por día para la última semana
                formatter = DateTimeFormatter.ofPattern("EEE");
                
                // Simulación de datos para los últimos 7 días
                LocalDate todayForWeek = LocalDate.now();
                for (int i = 6; i >= 0; i--) {
                    LocalDate date = todayForWeek.minusDays(i);
                    String label = date.format(formatter);
                    
                    // Calcular ventas para este día
                    final LocalDate currentDate = date;
                    double dailySales = orders.stream()
                            .filter(order -> order.getDate() != null && order.getDate().equals(currentDate))
                            .mapToDouble(OrderDTO::getTotal)
                            .sum();
                    
                    salesData.add(new ChartDataPointDTO(label, dailySales));
                }
                break;
                
            case "day":
            default:
                // Datos por hora para el día actual
                formatter = DateTimeFormatter.ofPattern("HH:mm");
                
                // Simulación de datos para las últimas horas
                for (int i = 0; i < 12; i += 2) {
                    String hourLabel = String.format("%02d:00", i + 8); // 8am a 8pm
                    
                    // En un sistema real, calcularíamos las ventas por hora
                    // Aquí usamos datos aleatorios para simulación
                    double hourlySales = Math.random() * 1000;
                    
                    salesData.add(new ChartDataPointDTO(hourLabel, hourlySales));
                }
                break;
        }
        
        return salesData;
    }

    @Override
    public List<PaymentMethodDataDTO> generatePaymentMethodsData(List<OrderDTO> orders) {
        // Mapa para contar órdenes por método de pago
        Map<String, Integer> methodCounts = new HashMap<>();
        methodCounts.put("Efectivo", 0);
        methodCounts.put("Transferencia", 0);
        methodCounts.put("MercadoPago", 0);
        
        // Procesar órdenes
        orders.forEach(order -> {
            String method = order.getMethod();
            if (method != null) {
                String methodName;
                switch (method) {
                    case "E":
                        methodName = "Efectivo";
                        break;
                    case "T":
                        methodName = "Transferencia";
                        break;
                    case "M":
                        methodName = "MercadoPago";
                        break;
                    default:
                        methodName = "Otro";
                        break;
                }
                
                // Incrementar contador para este método
                methodCounts.put(methodName, methodCounts.getOrDefault(methodName, 0) + 1);
            }
        });
        
        // Convertir a lista de objetos para el gráfico
        return methodCounts.entrySet().stream()
                .map(entry -> new PaymentMethodDataDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
package ar.edu.unju.fi.service;

import java.time.LocalDate;
import java.util.List;

import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.util.EstadoPago;

public interface IOrderService {
	void saveOrder(OrderDTO order);

	OrderDTO getOrderById(Integer id);

	List<OrderDTO> getAllOrders();

	void deleteOrder(Integer id);
	
	// Nuevos métodos para el estado de pago
	void actualizarEstadoPago(Integer idOrder, EstadoPago estadoPago);
	List<OrderDTO> findByEstadoPago(EstadoPago estadoPago);
	
	// Métodos para filtrado
	List<OrderDTO> findByStatus(String status);
	List<OrderDTO> findByMethod(String method);
	List<OrderDTO> findByDate(LocalDate date);
	List<OrderDTO> findByDateBetween(LocalDate startDate, LocalDate endDate);
	List<OrderDTO> findByCustomerNameContaining(String customerName);
	List<OrderDTO> findByCustomerDniContaining(String customerDni);
	List<OrderDTO> findByTotalGreaterThanEqual(Double minTotal);
	List<OrderDTO> findByTotalLessThanEqual(Double maxTotal);
	List<OrderDTO> findByTotalContaining(String totalText);
	List<OrderDTO> findByAnyField(String searchTerm);
	
	/**
	 * Método para filtrar pedidos según un campo específico
	 * @param searchTerm El término a buscar
	 * @param field El campo en el que buscar (id, status, method, customer, date, total)
	 * @return Lista de pedidos filtrados
	 */
	List<OrderDTO> filterOrders(String searchTerm, String field);
}
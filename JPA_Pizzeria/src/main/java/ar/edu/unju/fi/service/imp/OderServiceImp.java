package ar.edu.unju.fi.service.imp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.entity.OrderEntity;
import ar.edu.unju.fi.entity.OrderItemEntity;
import ar.edu.unju.fi.mapper.OrderMapDTO;
import ar.edu.unju.fi.repository.OrderRepository;
import ar.edu.unju.fi.service.IOrderService;
import ar.edu.unju.fi.service.IPizzaService;
import ar.edu.unju.fi.util.EstadoPago;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OderServiceImp implements IOrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderMapDTO orderMapDTO;
	@Autowired
	private IPizzaService pizzaService;
	
	@Override
	@Transactional
	public void saveOrder(OrderDTO order) {
		// Asegurar que toda orden nueva tenga estado PENDIENTE
		if (order.getEstadoPago() == null) {
			order.setEstadoPago(EstadoPago.PENDIENTE);
		}
		
		// Primero, guardamos la orden para obtener su ID
		OrderEntity orderEntity = orderMapDTO.toEntity(order);
		
		// Guardamos una copia de los items para manejarlos después
		List<OrderItemEntity> itemsToSave = new ArrayList<>();
		if (orderEntity.getItems() != null) {
			itemsToSave.addAll(orderEntity.getItems());
			// Temporalmente quitamos los items para guardar primero la orden
			orderEntity.setItems(new ArrayList<>());
		}
		
		// Guardamos la orden para obtener su ID
		OrderEntity savedOrder = orderRepository.save(orderEntity);
		log.info("INFO - Orden base guardada con ID: {}", savedOrder.getIdOrder());
		
		// Ahora que tenemos el ID de la orden, lo asignamos a cada item
		if (!itemsToSave.isEmpty()) {
			int itemCounter = 1;
			for (OrderItemEntity item : itemsToSave) {
				// Asignar el ID de la orden al item
				item.setIdOrder(savedOrder.getIdOrder());
				// Asignar un ID de item secuencial
				item.setIdItem(itemCounter++);
				// Establecer la referencia a la orden
				item.setOrder(savedOrder);
			}
			
			// Restaurar los items en la orden
			savedOrder.setItems(itemsToSave);
			
			// Guardar la orden nuevamente para persistir los items
			savedOrder = orderRepository.save(savedOrder);
			log.info("INFO - Orden guardada completamente con {} items, ID: {}", 
				itemsToSave.size(), savedOrder.getIdOrder());
		}
	}

	@Override
	public OrderDTO getOrderById(Integer id) {
		log.info("INFO - Buscando pedido con id: {}", id);
		OrderDTO orderDTO = orderRepository.findById(id).map(orderMapDTO::toDto).orElse(null);
		if (orderDTO != null) {
			// Ensure all pizza data is loaded for order items
			for (OrderItemDTO item : orderDTO.getItems()) {
				if (item.getIdPizza() != null) {
					PizzaDTO pizzaDTO = pizzaService.findById(item.getIdPizza());
					item.setPizza(pizzaDTO);
				}
			}
		}
		return orderDTO;
	}

	@Override
	public List<OrderDTO> getAllOrders() {
		log.info("INFO - Buscando todos los pedidos en la base de datos");
		List<OrderDTO> orders = orderMapDTO.listOrderToListOrderDTO(orderRepository.findAll());
		
		// Ensure pizza data is properly loaded for all order items
		for (OrderDTO order : orders) {
			if (order.getItems() != null) {
				for (OrderItemDTO item : order.getItems()) {
					if (item.getPizza() == null && item.getIdPizza() != null) {
						PizzaDTO pizzaDTO = pizzaService.findById(item.getIdPizza());
						item.setPizza(pizzaDTO);
						log.info("INFO - Pizza cargada para item: {}", pizzaDTO != null ? pizzaDTO.getName() : "no encontrada");
					}
				}
			}
		}
		
		// Invertir el orden de la lista para mostrar primero las órdenes más nuevas
		java.util.Collections.reverse(orders);
		log.info("INFO - Lista de pedidos invertida para mostrar primero los más recientes");
		
		return orders;
	}

	@Override
	public void deleteOrder(Integer id) {
		log.info("INFO - Eliminando pedido con id: {}", id);
		orderRepository.deleteById(id);
	}
	
	// Implementación de los nuevos métodos para filtrado
	
	@Override
	public List<OrderDTO> findByStatus(String status) {
		// Para mantener compatibilidad, intentamos mapear el string al enum
		try {
			EstadoPago estadoPago = EstadoPago.valueOf(status.toUpperCase());
			return findByEstadoPago(estadoPago);
		} catch (Exception e) {
			log.warn("WARN - Estado de pago no reconocido: {}", status);
			return new ArrayList<>();
		}
	}

	@Override
	public List<OrderDTO> findByMethod(String method) {
		log.info("Buscando pedidos con método de pago: {}", method);
		List<OrderEntity> entities = orderRepository.findByMethod(method);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByDate(LocalDate date) {
		log.info("Buscando pedidos con fecha: {}", date);
		List<OrderEntity> entities = orderRepository.findByDate(date);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByDateBetween(LocalDate startDate, LocalDate endDate) {
		log.info("Buscando pedidos entre fechas {} y {}", startDate, endDate);
		List<OrderEntity> entities = orderRepository.findByDateBetween(startDate, endDate);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByCustomerNameContaining(String customerName) {
		log.info("Buscando pedidos con nombre de cliente que contiene: {}", customerName);
		List<OrderEntity> entities = orderRepository.findByCustomerNameContaining(customerName);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByCustomerDniContaining(String customerDni) {
		log.info("Buscando pedidos con DNI de cliente que contiene: {}", customerDni);
		List<OrderEntity> entities = orderRepository.findByCustomerDniContaining(customerDni);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByTotalGreaterThanEqual(Double minTotal) {
		log.info("Buscando pedidos con total mayor o igual a: {}", minTotal);
		List<OrderEntity> entities = orderRepository.findByTotalGreaterThanEqual(minTotal);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByTotalLessThanEqual(Double maxTotal) {
		log.info("Buscando pedidos con total menor o igual a: {}", maxTotal);
		List<OrderEntity> entities = orderRepository.findByTotalLessThanEqual(maxTotal);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByTotalContaining(String totalText) {
		log.info("Buscando pedidos con total que contiene: {}", totalText);
		List<OrderEntity> entities = orderRepository.findByTotalContaining(totalText);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> findByAnyField(String searchTerm) {
		log.info("Buscando pedidos con término en cualquier campo: {}", searchTerm);
		List<OrderEntity> entities = orderRepository.findByAnyField(searchTerm);
		return orderMapDTO.listOrderToListOrderDTO(entities);
	}

	@Override
	public List<OrderDTO> filterOrders(String searchTerm, String field) {
		log.info("Filtrando pedidos por campo: {} con término: {}", field, searchTerm);
		
		if (searchTerm == null || searchTerm.trim().isEmpty()) {
			return getAllOrders();
		}
		
		switch (field) {
			case "status":
				return findByStatus(searchTerm);
			case "method":
				return findByMethod(searchTerm);
			case "customer":
				// Intentamos buscar por nombre o DNI del cliente
				List<OrderDTO> byName = findByCustomerNameContaining(searchTerm);
				if (!byName.isEmpty()) {
					return byName;
				}
				return findByCustomerDniContaining(searchTerm);
			case "date":
				try {
					// Primero intentar parsear como fecha en formato DD/MM/YYYY
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					LocalDate date = LocalDate.parse(searchTerm, formatter);
					log.info("INFO - Fecha interpretada correctamente en formato DD/MM/YYYY: {}", date);
					return findByDate(date);
				} catch (DateTimeParseException e1) {
					log.warn("WARN - No se pudo parsear la fecha en formato DD/MM/YYYY: {}", searchTerm);
					try {
						// Si falla, intentar con el formato ISO estándar YYYY-MM-DD
						LocalDate date = LocalDate.parse(searchTerm);
						log.info("INFO - Fecha interpretada correctamente en formato ISO: {}", date);
						return findByDate(date);
					} catch (DateTimeParseException e2) {
						// Si no se puede parsear en ningún formato, devolvemos una lista vacía
						log.warn("WARN - No se pudo parsear la fecha en ningún formato conocido: {}", searchTerm);
						return new ArrayList<>();
					}
				}
			case "total":
				try {
					// Intentar parsear como número para búsquedas exactas de total
					Double total = Double.parseDouble(searchTerm);
					return findByTotalContaining(searchTerm);
				} catch (NumberFormatException e) {
					// Si no se puede parsear, buscar coincidencias parciales en texto
					return findByTotalContaining(searchTerm);
				}
			case "all":
			default:
				return findByAnyField(searchTerm);
		}
	}

	@Override
	@Transactional
	public void actualizarEstadoPago(Integer idOrder, EstadoPago estadoPago) {
		log.info("INFO - Actualizando estado de pago de la orden {} a {}", idOrder, estadoPago);
		Optional<OrderEntity> orderOpt = orderRepository.findById(idOrder);
		if (orderOpt.isPresent()) {
			OrderEntity order = orderOpt.get();
			order.setEstadoPago(estadoPago);
			orderRepository.save(order);
			log.info("INFO - Estado de pago actualizado correctamente");
		} else {
			log.error("ERROR - No se encontró la orden con ID: {}", idOrder);
		}
	}

	@Override
	public List<OrderDTO> findByEstadoPago(EstadoPago estadoPago) {
		log.info("INFO - Buscando órdenes con estado de pago: {}", estadoPago);
		List<OrderEntity> orders = orderRepository.findByEstadoPago(estadoPago);
		return orderMapDTO.listOrderToListOrderDTO(orders);
	}
}
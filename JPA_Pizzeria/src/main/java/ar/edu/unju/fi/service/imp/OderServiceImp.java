package ar.edu.unju.fi.service.imp;

import java.util.ArrayList;
import java.util.List;

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
		
		return orders;
	}

	@Override
	public void deleteOrder(Integer id) {
		log.info("INFO - Eliminando pedido con id: {}", id);
		orderRepository.deleteById(id);
	}
}
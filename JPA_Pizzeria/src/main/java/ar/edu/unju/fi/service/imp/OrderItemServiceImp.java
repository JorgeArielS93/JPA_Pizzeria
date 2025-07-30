package ar.edu.unju.fi.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.entity.OrderItemEntity;
import ar.edu.unju.fi.entity.OrderItemId;
import ar.edu.unju.fi.mapper.OrderItemMapDTO;
import ar.edu.unju.fi.repository.OrderItemRepository;
import ar.edu.unju.fi.service.IOrderItemService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderItemServiceImp implements IOrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderItemMapDTO orderItemMapDTO;
	
	@Override
	public void saveOrderItem(OrderItemDTO orderItem) {
		OrderItemEntity orderItemEntity = orderItemMapDTO.toEntity(orderItem);
		OrderItemEntity savedEntity = orderItemRepository.save(orderItemEntity);
		log.info("OrderItem guardado correctamente con ID: {}", savedEntity.getIdItem());
	}

	@Override
	public OrderItemDTO getOrderItemById(OrderItemId id) {
		log.info("Buscando OrderItem con ID: {}", id);
		return orderItemRepository.findById(id)
				.map(orderItemMapDTO::toDto)
				.orElse(null);
	}

	@Override
	public List<OrderItemDTO> getAllOrderItems() {
		log.info("Buscando todos los OrderItems en la base de datos");
		List<OrderItemDTO> orderItems = orderItemMapDTO.listOrderItemToListOrderItemDTO(orderItemRepository.findAll());
		return orderItems;
	}

	@Override
	public void deleteOrderItem(OrderItemId id) {
		log.info("Eliminando OrderItem con ID: {}", id);
		orderItemRepository.deleteById(id);
	}
}
package ar.edu.unju.fi.service;

import java.util.List;

import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.entity.OrderItemId;

public interface IOrderItemService {
	void saveOrderItem(OrderItemDTO orderItem);

	OrderItemDTO getOrderItemById(OrderItemId id);

	List<OrderItemDTO> getAllOrderItems();

	void deleteOrderItem(OrderItemId id);
}

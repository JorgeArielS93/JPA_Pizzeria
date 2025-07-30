package ar.edu.unju.fi.service;

import java.util.List;

import ar.edu.unju.fi.dto.OrderDTO;

public interface IOrderService {
	void saveOrder(OrderDTO order);

	OrderDTO getOrderById(Integer id);

	List<OrderDTO> getAllOrders();

	void deleteOrder(Integer id);
}

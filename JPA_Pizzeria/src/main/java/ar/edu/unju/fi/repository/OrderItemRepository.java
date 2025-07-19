package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.unju.fi.entity.OrderItemEntity;
import ar.edu.unju.fi.entity.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId>{

}

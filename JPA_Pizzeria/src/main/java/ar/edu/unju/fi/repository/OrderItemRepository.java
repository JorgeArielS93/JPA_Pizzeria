package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.OrderItemEntity;
import ar.edu.unju.fi.entity.OrderItemId;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId>{
}

package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.unju.fi.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{

}

package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.unju.fi.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String>{

}

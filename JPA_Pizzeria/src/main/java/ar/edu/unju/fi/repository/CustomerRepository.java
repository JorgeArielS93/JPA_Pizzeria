package ar.edu.unju.fi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.CustomerEntity;
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>{
    boolean existsByDni(String dni);
    CustomerEntity findByDni(String dni);
}
package ar.edu.unju.fi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.CustomerEntity;
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>{
    boolean existsByDni(String dni);
    CustomerEntity findByDni(String dni);
    
    // Métodos de búsqueda para filtrado
    List<CustomerEntity> findByDniContaining(String dni);
    List<CustomerEntity> findByNameContainingIgnoreCase(String name);
    List<CustomerEntity> findByAddressContainingIgnoreCase(String address);
    List<CustomerEntity> findByEmailContainingIgnoreCase(String email);
    List<CustomerEntity> findByPhoneNumberContaining(String phoneNumber);
    
    // Método de búsqueda general en múltiples campos
    @Query("SELECT c FROM CustomerEntity c WHERE " +
           "LOWER(c.dni) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CustomerEntity> findByAnyField(@Param("searchTerm") String searchTerm);
}
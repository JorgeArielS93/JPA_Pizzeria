package ar.edu.unju.fi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.OrderEntity;
import ar.edu.unju.fi.util.EstadoPago;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
    
    // Métodos de búsqueda para filtrado
    // Reemplazar el método findByStatus con consulta JPQL para hacer la conversión
    @Query("SELECT o FROM OrderEntity o WHERE CAST(o.estadoPago AS string) LIKE %:status%")
    List<OrderEntity> findByStatus(@Param("status") String status);
    
    List<OrderEntity> findByMethod(String method);
    List<OrderEntity> findByDate(LocalDate date);
    List<OrderEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Búsqueda por estado de pago (nuevo)
    List<OrderEntity> findByEstadoPago(EstadoPago estadoPago);
    
    // Búsqueda por cliente
    @Query("SELECT o FROM OrderEntity o WHERE o.customer.name LIKE %:customerName%")
    List<OrderEntity> findByCustomerNameContaining(@Param("customerName") String customerName);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.customer.dni LIKE %:customerDni%")
    List<OrderEntity> findByCustomerDniContaining(@Param("customerDni") String customerDni);
    
    // Búsqueda por total
    List<OrderEntity> findByTotalGreaterThanEqual(Double minTotal);
    List<OrderEntity> findByTotalLessThanEqual(Double maxTotal);
    
    // Método para buscar por texto en el total
    @Query("SELECT o FROM OrderEntity o WHERE CAST(o.total AS string) LIKE %:totalText%")
    List<OrderEntity> findByTotalContaining(@Param("totalText") String totalText);
    
    // Búsqueda por cualquier campo
    @Query("SELECT o FROM OrderEntity o WHERE " +
           "CAST(o.idOrder AS string) LIKE %:searchTerm% OR " +
           "o.method LIKE %:searchTerm% OR " +
           "CAST(o.estadoPago AS string) LIKE %:searchTerm% OR " +
           "o.customer.name LIKE %:searchTerm% OR " +
           "o.customer.dni LIKE %:searchTerm% OR " +
           "CAST(o.total AS string) LIKE %:searchTerm%")
    List<OrderEntity> findByAnyField(@Param("searchTerm") String searchTerm);
}
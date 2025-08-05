package ar.edu.unju.fi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.PizzaEntity;
@Repository
public interface PizzaRepository extends JpaRepository<PizzaEntity, Integer> {
    
    // Métodos de búsqueda para filtrado
    List<PizzaEntity> findByNameContainingIgnoreCase(String name);
    List<PizzaEntity> findByDescriptionContainingIgnoreCase(String description);
    List<PizzaEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    List<PizzaEntity> findByPriceLessThanEqual(Double maxPrice);
    List<PizzaEntity> findByPriceGreaterThanEqual(Double minPrice);
    
    // Método de búsqueda general en múltiples campos
    @Query("SELECT p FROM PizzaEntity p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PizzaEntity> findByAnyField(@Param("searchTerm") String searchTerm);
    
    // Búsqueda por precio como texto (para cuando el usuario introduce un precio en el campo de búsqueda)
    @Query("SELECT p FROM PizzaEntity p WHERE " +
           "CAST(p.price AS string) LIKE CONCAT('%', :priceText, '%')")
    List<PizzaEntity> findByPriceContaining(@Param("priceText") String priceText);
}
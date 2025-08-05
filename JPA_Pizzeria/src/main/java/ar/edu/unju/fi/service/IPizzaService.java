package ar.edu.unju.fi.service;
import java.util.List;
import ar.edu.unju.fi.dto.PizzaDTO; // Usamos DTO en la interfaz
public interface IPizzaService {
    List<PizzaDTO> findAll();
    PizzaDTO findById(Integer id); 
    void save(PizzaDTO pizzaDTO);    
    void deleteById(Integer id);
    
    // Nuevos métodos para filtrado
    List<PizzaDTO> findByNameContaining(String name);
    List<PizzaDTO> findByDescriptionContaining(String description);
    List<PizzaDTO> findByPriceContaining(String priceText);
    List<PizzaDTO> findByPriceBetween(Double minPrice, Double maxPrice);
    List<PizzaDTO> findByPriceLessThanEqual(Double maxPrice);
    List<PizzaDTO> findByPriceGreaterThanEqual(Double minPrice);
    
    /**
     * Método para buscar pizzas que coincidan con el término de búsqueda en cualquier campo
     * @param searchTerm El término a buscar
     * @return Lista de pizzas que coincidan con la búsqueda
     */
    List<PizzaDTO> findByAnyField(String searchTerm);
    
    /**
     * Método para filtrar pizzas según un campo específico
     * @param searchTerm El término a buscar
     * @param field El campo en el que buscar (name, description, price)
     * @return Lista de pizzas filtradas
     */
    List<PizzaDTO> filterPizzas(String searchTerm, String field);
}
package ar.edu.unju.fi.service.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.entity.PizzaEntity;
import ar.edu.unju.fi.mapper.PizzaMapDTO;
import ar.edu.unju.fi.repository.PizzaRepository;
import ar.edu.unju.fi.service.IPizzaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // Anotación para marcar esta clase como un servicio de Spring
public class PizzaServiceImp implements IPizzaService {

    // Inyección de dependencias del Repositorio y el Mapper
    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaMapDTO pizzaMapDTO;

    /**
     * Recupera todas las pizzas de la base de datos y las convierte a DTOs.
     */
    @Override
    public List<PizzaDTO> findAll() {
        log.info("Buscando todas las pizzas en la base de datos");
        // 1. Llama al repositorio para obtener la lista de entidades
        List<PizzaEntity> pizzaEntities = pizzaRepository.findAll();
        // 2. Usa el mapper para convertir la lista de entidades a una lista de DTOs
        return pizzaMapDTO.listPizzaToListPizzaDTO(pizzaEntities);
    }

    /**
     * Busca una pizza por su ID y la convierte a DTO.
     */
    @Override
    public PizzaDTO findById(Integer id) {
        log.info("Buscando pizza con id: {}", id);
        // 1. Busca la entidad por ID. `findById` devuelve un Optional.
        // 2. Si la encuentra, la mapea a DTO. Si no, devuelve null.
        return pizzaRepository.findById(id)
                .map(pizzaMapDTO::toDto) // Mapea la entidad a DTO si está presente
                .orElse(null);          // Devuelve null si no se encontró
    }

    /**
     * Guarda una nueva pizza o actualiza una existente.
     * Recibe un DTO, lo convierte a entidad y lo persiste.
     */
    @Override
    public void save(PizzaDTO pizzaDTO) {
        log.info("Guardando pizza con nombre: {}", pizzaDTO.getName());
        // 1. Convierte el DTO recibido a una entidad JPA
        PizzaEntity pizzaEntity = pizzaMapDTO.toEntity(pizzaDTO);
        // 2. Llama al repositorio para guardar la entidad en la base de datos
        pizzaRepository.save(pizzaEntity);
    }

    /**
     * Elimina una pizza de la base de datos por su ID.
     */
    @Override
    public void deleteById(Integer id) {
        log.info("Eliminando pizza con id: {}", id);
        // Llama directamente al método del repositorio para eliminar por ID
        pizzaRepository.deleteById(id);
    }
    
    @Override
    public List<PizzaDTO> findByNameContaining(String name) {
        log.info("Buscando pizzas con nombre que contiene: {}", name);
        List<PizzaEntity> entities = pizzaRepository.findByNameContainingIgnoreCase(name);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> findByDescriptionContaining(String description) {
        log.info("Buscando pizzas con descripción que contiene: {}", description);
        List<PizzaEntity> entities = pizzaRepository.findByDescriptionContainingIgnoreCase(description);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }
    
    @Override
    public List<PizzaDTO> findByPriceContaining(String priceText) {
        log.info("Buscando pizzas con precio que contiene: {}", priceText);
        List<PizzaEntity> entities = pizzaRepository.findByPriceContaining(priceText);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> findByPriceBetween(Double minPrice, Double maxPrice) {
        log.info("Buscando pizzas con precio entre {} y {}", minPrice, maxPrice);
        List<PizzaEntity> entities = pizzaRepository.findByPriceBetween(minPrice, maxPrice);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> findByPriceLessThanEqual(Double maxPrice) {
        log.info("Buscando pizzas con precio menor o igual a: {}", maxPrice);
        List<PizzaEntity> entities = pizzaRepository.findByPriceLessThanEqual(maxPrice);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> findByPriceGreaterThanEqual(Double minPrice) {
        log.info("Buscando pizzas con precio mayor o igual a: {}", minPrice);
        List<PizzaEntity> entities = pizzaRepository.findByPriceGreaterThanEqual(minPrice);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> findByAnyField(String searchTerm) {
        log.info("Buscando pizzas con término en cualquier campo: {}", searchTerm);
        List<PizzaEntity> entities = pizzaRepository.findByAnyField(searchTerm);
        return pizzaMapDTO.listPizzaToListPizzaDTO(entities);
    }

    @Override
    public List<PizzaDTO> filterPizzas(String searchTerm, String field) {
        log.info("Filtrando pizzas por campo: {} con término: {}", field, searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        
        switch (field) {
            case "name":
                return findByNameContaining(searchTerm);
            case "description":
                return findByDescriptionContaining(searchTerm);
            case "price":
                try {
                    // Intentar parsear como número para búsquedas exactas de precio
                    Double price = Double.parseDouble(searchTerm);
                    return findByPriceContaining(searchTerm);
                } catch (NumberFormatException e) {
                    // Si no se puede parsear, buscar coincidencias parciales en texto
                    return findByPriceContaining(searchTerm);
                }
            case "all":
            default:
                return findByAnyField(searchTerm);
        }
    }
}
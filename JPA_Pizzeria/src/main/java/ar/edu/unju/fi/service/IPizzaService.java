package ar.edu.unju.fi.service;
import java.util.List;
import ar.edu.unju.fi.dto.PizzaDTO; // Usamos DTO en la interfaz
public interface IPizzaService {
    List<PizzaDTO> findAll();
    PizzaDTO findById(Integer id); 
    void save(PizzaDTO pizzaDTO);    
    void deleteById(Integer id);
}

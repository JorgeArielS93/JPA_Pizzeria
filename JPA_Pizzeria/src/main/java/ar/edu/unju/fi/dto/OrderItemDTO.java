package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer idOrder;
    private Integer idItem;
    private Integer idPizza;
    private Double quantity;
    private Double price;
    // Opcional: Si quieres incluir la información de la pizza en el DTO del item
    // private PizzaDTO pizza; 
}

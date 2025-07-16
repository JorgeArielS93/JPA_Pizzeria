package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDTO {
    private Integer idPizza;
    private String name;
    private String description;
    private Double price;
    private Boolean vegetarian;
    private boolean vegan;
    private Boolean avaible;
}

package ar.edu.unju.fi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    private String name;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 150, message = "La descripción debe tener entre 10 y 150 caracteres")
    private String description;
    
    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    private Double price;
    
    private Boolean vegetarian;
    
    private boolean vegan;
    
    @NotNull(message="Debes indicar si la pizza está disponible")
    private boolean avaible;
}
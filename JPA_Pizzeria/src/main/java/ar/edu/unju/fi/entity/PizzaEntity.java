package ar.edu.unju.fi.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pizza")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PizzaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Pizza", nullable = false)
	private Integer idPizza;
	
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
	@Column(nullable = false, length = 30, unique = true)
	private String name;
	
	@NotBlank(message = "La descripción es obligatoria")
	@Size(min = 10, max = 150, message = "La descripción debe tener entre 10 y 150 caracteres")
	@Column(nullable = false, length = 150)
	private String description;
	
	@NotNull(message = "El precio no puede ser nulo")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
	@Column(nullable = false, columnDefinition = "Decimal(10,2)")
	private Double price;
	
	@Column(columnDefinition = "TINYINT")
	private Boolean vegetarian;
	
	@Column(columnDefinition = "TINYINT")
	private boolean vegan;
	
	@NotNull(message="Debes indicar si la pizza está disponible")
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Boolean avaible;
}
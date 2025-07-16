package ar.edu.unju.fi.entity;

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
//Indica que esta clase es una entidad JPA que se mapeará a una tabla de base de datos.

@Table(name = "Pizza")
//Especifica el nombre de la tabla en la base de datos que se usará para esta entidad.

@Getter
@Setter
//Genera automáticamente los métodos getter y setter para todos los campos de la clase.

@NoArgsConstructor
//Genera un constructor sin argumentos.

@AllArgsConstructor
//Genera un constructor con un argumento para cada campo de la clase.

public class PizzaEntity {
	@Id
	// Indica que el campo siguiente es la clave primaria de la entidad.

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Especifica que el valor del campo se generará automáticamente por la base de datos mediante la estrategia de identidad.

	@Column(name = "id_Pizza", nullable = false)
	// Mapea el campo a la columna "id_Pizza" en la tabla de base de datos y especifica que no puede ser nulo.
	private Integer idPizza;
	
	@Column(nullable = false, length = 30, unique = true)
	// Mapea el campo a una columna en la base de datos que no puede ser nula, tiene una longitud máxima de 30 caracteres y debe ser única.
	private String name;
	
	@Column(nullable = false, length = 150)
	// Mapea el campo a una columna en la base de datos que no puede ser nula y tiene una longitud máxima de 150 caracteres.
	private String description;
	
	@Column(nullable = false, columnDefinition = "Decimal(5,2)")
	// Mapea el campo a una columna en la base de datos que no puede ser nula y debe tener un tipo de dato Decimal con 5 dígitos en total y 2 decimales.
	private Double price;
	
	@Column(columnDefinition = "TINYINT")
	// Mapea el campo a una columna en la base de datos con tipo de dato TINYINT.
	private Boolean vegetarian;
	
	@Column(columnDefinition = "TINYINT")
	// Mapea el campo a una columna en la base de datos con tipo de dato TINYINT.
	private boolean vegan;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	// Mapea el campo a una columna en la base de datos con tipo de dato TINYINT que no puede ser nula.
	private Boolean avaible;
}
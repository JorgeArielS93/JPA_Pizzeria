package ar.edu.unju.fi.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//Indica que esta clase es una entidad JPA que se mapeará a una tabla de base de datos.

@Table(name = "pizza_order")
//Especifica el nombre de la tabla en la base de datos que se usará para esta entidad.

@Getter
@Setter
//Genera automáticamente los métodos getter y setter para todos los campos de la clase.

@NoArgsConstructor
//Genera un constructor sin argumentos.

@AllArgsConstructor
//Genera un constructor con un argumento para cada campo de la clase.

public class OrderEntity {
	
	@Id // Indica que el campo 'idOrder' es la clave primaria de la entidad.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación de la clave primaria. IDENTITY delega la generación al motor de la BD (ej. AUTO_INCREMENT en MySQL).
	@Column(name = "id_order", nullable = false) // Mapea el campo a la columna "id_order" y especifica que no puede ser nulo.
	private Integer idOrder;
	
	/**
	 * Relación Muchos a Uno con CustomerEntity.
	 * Muchas órdenes (@ManyToOne) pueden estar asociadas a un único cliente.
	 */
	@ManyToOne
	@JoinColumn(name = "id_customer", nullable = false)
	private CustomerEntity customer;

	@Column(nullable = false, columnDefinition = "DATETIME") // Mapea a una columna no nula con tipo de dato DATETIME en la BD.
	private LocalDate date; // Fecha en que se realizó la orden.
	
	@Column(nullable = false, columnDefinition = "DECIMAL(10,2)") // Mapea a una columna no nula, DECIMAL para precisión monetaria (hasta 99,999,999.99).
	private Double total; // El costo total de la orden.
	
	@Column(nullable = false, columnDefinition = "CHAR(1)") // Mapea a una columna no nula, tipo CHAR de 1 carácter (ej. 'E' para efectivo, 'T' para tarjeta).
	private String method; // Método de pago.
	
	@Column(name = "additional_notes", length = 200) // Mapea a la columna "additional_notes" con longitud máxima de 200 caracteres.
	private String additionalNotes; // Notas adicionales para la orden.
	
	/**
	 * Relación Uno a Muchos con OrderItemEntity.
	 * Una orden (@OneToMany) puede tener muchos ítems.
	 * mappedBy = "order": Indica que la entidad OrderItemEntity es la dueña de la relación,
	 * y que en esa clase hay un campo llamado "order" que define cómo se unen.
	 * Esto evita que se cree una tabla intermedia adicional.
	 */
	@OneToMany(mappedBy = "order")
	private List<OrderItemEntity> items; // Lista de todos los ítems que componen esta orden.
}
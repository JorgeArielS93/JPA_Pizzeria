package ar.edu.unju.fi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item") // Especifica que esta entidad se mapea a la tabla llamada "order_item".
@IdClass(OrderItemId.class) // Indica que la clave primaria de esta entidad es compuesta y está definida en la clase OrderItemId.
public class OrderItemEntity {

	@Id // Marca este campo como parte de la clave primaria.
	@Column(name = "id_order", nullable = false) // Mapea este campo a la columna 'id_order', que no puede ser nula.
	private Integer idOrder;

	@Id // Marca este campo también como parte de la clave primaria.
	@Column(name = "id_item", nullable = false) // Mapea a la columna 'id_item', no nula.
	private Integer idItem;

	@Column(name = "id_pizza", nullable = false) // Mapea a la columna 'id_pizza', no nula.
	private Integer idPizza;

	@Column(nullable = false, columnDefinition = "Decimal(10,2)") // Mapea a una columna que no puede ser nula y define el tipo de dato en la BD como DECIMAL(10,2) para precisión.
	private Double quantity; // Cantidad del producto (ej. 1.5 pizzas).

	@Column(nullable = false, columnDefinition = "Decimal(10,2)") // Mapea a una columna no nula, tipo DECIMAL para valores monetarios.
	private Double price; // Precio del ítem en el momento de la compra.
	
	/**
	 * Relación Muchos a Uno con OrderEntity.
	 * Muchos ítems de orden (@ManyToOne) pertenecen a una sola orden.
	 */
	@ManyToOne
	// Define la columna de unión ('id_order') que conecta con la tabla de órdenes.
	// insertable = false, updatable = false: Indica que JPA no debe intentar insertar o actualizar esta columna a través de esta relación.
	// La persistencia de esta columna se maneja directamente a través del campo 'idOrder'.
	@JoinColumn(name = "id_order", referencedColumnName = "id_order", insertable = false, updatable = false)
	private OrderEntity order;
	
	/**
	 * Relación Uno a Uno con PizzaEntity.
	 * Un ítem de orden (@OneToOne) corresponde a una pizza específica.
	 */
	@OneToOne
	// Define la columna de unión ('id_pizza') que conecta con la tabla de pizzas.
	// insertable = false, updatable = false: JPA no gestionará esta columna a través de esta relación.
	// La persistencia se maneja a través del campo 'idPizza'.
	@JoinColumn(name = "id_pizza", referencedColumnName = "id_pizza", insertable = false, updatable = false)
	private PizzaEntity pizza;
}
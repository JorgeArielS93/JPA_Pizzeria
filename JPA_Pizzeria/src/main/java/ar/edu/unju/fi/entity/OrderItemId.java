package ar.edu.unju.fi.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Anotaciones de Lombok para generar código repetitivo automáticamente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Define la clave primaria compuesta para la entidad OrderItemEntity.
 * Debe implementar Serializable para que JPA pueda manejarla eficientemente.
 */
public class OrderItemId implements Serializable {
	
    /**
	 * Atributo para la serialización, requerido por la interfaz Serializable.
	 */
	private static final long serialVersionUID = 1L;

	// Parte de la clave primaria compuesta, representa el ID de la orden.
	private Integer idOrder;
	
	// Parte de la clave primaria compuesta, representa el ID del ítem dentro de la orden.
	private Integer idItem;

	/**
	 * Sobrescribe el método equals para comparar dos objetos OrderItemId.
	 * Dos objetos son iguales si sus idOrder y idItem son iguales.
	 * Es crucial para que JPA pueda identificar y comparar correctamente las entidades.
	 */
	@Override
	public boolean equals(Object o) {
		// Si es el mismo objeto en memoria, son iguales.
		if (this == o) {
			return true;
		}
		// Si el objeto no es una instancia de OrderItemId, no pueden ser iguales.
		if (!(o instanceof OrderItemId that)) {
			return false;
		}
		// Compara los valores de los campos para determinar la igualdad.
		return Objects.equals(idOrder, that.idOrder) && Objects.equals(idItem, that.idItem);
	}

	/**
	 * Sobrescribe el método hashCode para generar un código hash único basado en los campos de la clave.
	 * Es fundamental que si dos objetos son iguales según equals(), también tengan el mismo hashCode().
	 */
	@Override
	public int hashCode() {
		return Objects.hash(idOrder, idItem);
	}
}
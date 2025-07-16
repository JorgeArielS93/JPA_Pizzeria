package ar.edu.unju.fi.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//Indica que esta clase es una entidad JPA que se mapeará a una tabla de base de datos.

@Getter
@Setter
//Genera automáticamente los métodos getter y setter para todos los campos de la clase.

@NoArgsConstructor
//Genera un constructor sin argumentos.

@AllArgsConstructor
//Genera un constructor con un argumento para cada campo de la clase.

@Table(name = "customer") //Especifica que esta entidad se corresponde con la tabla llamada "customer".
public class CustomerEntity {

	/**
	 * Clave primaria de la entidad.
	 */
	@Id // 🔑 Indica que este campo es la clave primaria (Primary Key) de la tabla.
	@Column(name = "id_customer", nullable = false, length = 15) //Mapea este campo a la columna "id_customer". No puede ser nulo y su longitud máxima es de 15 caracteres.
	private String idCustomer; // Identificador único para el cliente (ej. un DNI o CUIT).

	/**
	 * Atributos de la entidad.
	 */
	@Column(nullable = false, length = 60) //Mapea al campo "name". No puede ser nulo y tiene una longitud máxima de 60 caracteres.
	private String name; // Nombre completo del cliente.

	@Column(length = 100) // 🔗 Mapea al campo "address" (probablemente debería ser "address"). La longitud máxima es de 100 caracteres.
	private String address; // Dirección del cliente.

	@Column(nullable = false, length = 50, unique = true) //Mapea al campo "email". No puede ser nulo, tiene longitud máxima de 50 y su valor debe ser único en toda la tabla.
	private String email; // Correo electrónico del cliente. Se usa como identificador único para inicios de sesión, etc.

	@Column(name = "phone_number", length = 20) //Mapea al campo "phone_number". La longitud máxima es de 20 caracteres.
	private String phoneNumber; // Número de teléfono del cliente.
}
package ar.edu.unju.fi.service;

import java.util.List;

import ar.edu.unju.fi.dto.CustomerDTO;

public interface ICustomerService {
	List<CustomerDTO> findAll();
	CustomerDTO findById(Integer id);
	void save(CustomerDTO customer);
	void deleteById(Integer id);
	CustomerDTO findByDni(String dni);
	boolean existsByDni(String dni);
	
	// Nuevos métodos para filtrado
	List<CustomerDTO> findByDniContaining(String dni);
	List<CustomerDTO> findByNameContaining(String name);
	List<CustomerDTO> findByAddressContaining(String address);
	List<CustomerDTO> findByEmailContaining(String email);
	List<CustomerDTO> findByPhoneContaining(String phone);
	
	/**
	 * Método para buscar clientes que coincidan con el término de búsqueda en cualquier campo
	 * @param searchTerm El término a buscar
	 * @return Lista de clientes que coincidan con la búsqueda
	 */
	List<CustomerDTO> findByAnyField(String searchTerm);
	
	/**
	 * Método para filtrar clientes según un campo específico
	 * @param searchTerm El término a buscar
	 * @param field El campo en el que buscar (dni, name, address, email, phone)
	 * @return Lista de clientes filtrados
	 */
	List<CustomerDTO> filterCustomers(String searchTerm, String field);
}
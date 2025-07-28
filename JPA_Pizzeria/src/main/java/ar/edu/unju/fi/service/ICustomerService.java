package ar.edu.unju.fi.service;

import java.util.List;

import ar.edu.unju.fi.dto.CustomerDTO;

public interface ICustomerService {
	List<CustomerDTO> findAll();
	CustomerDTO findById(Integer id);
	void save(CustomerDTO customer);
	void deleteById(Integer id);
	public CustomerDTO findByDni(String dni);
	public boolean existsByDni(String dni);
}

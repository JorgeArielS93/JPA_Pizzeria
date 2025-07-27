package ar.edu.unju.fi.service;

import java.util.List;

import ar.edu.unju.fi.dto.CustomerDTO;

public interface ICustomerService {
	List<CustomerDTO> findAll();
	CustomerDTO findById(String id);
	void save(CustomerDTO customer);
	void deleteById(String id); 
}

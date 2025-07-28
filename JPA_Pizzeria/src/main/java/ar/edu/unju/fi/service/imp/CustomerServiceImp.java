package ar.edu.unju.fi.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.entity.CustomerEntity;
import ar.edu.unju.fi.mapper.CustomerMapDTO;
import ar.edu.unju.fi.repository.CustomerRepository;
import ar.edu.unju.fi.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class CustomerServiceImp implements ICustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerMapDTO customerMapDTO;
	
	@Override
	public List<CustomerDTO> findAll() {
		log.info("Buscando todos los clientes en la base de datos");
		List<CustomerEntity> customerEntities = customerRepository.findAll();
		return customerMapDTO.listCustomerToListCustomerDTO(customerEntities);
	}

	@Override
	public CustomerDTO findById(Integer id) {
		log.info("Buscando cliente con id: {}", id);
		return customerRepository.findById(id).map(customerMapDTO::toDto)
				.orElse(null);
	}

	@Override
	public void save(CustomerDTO customer) {
		log.info("Guardando cliente con nombre: {}", customer.getName());
		CustomerEntity customerEntity = customerMapDTO.toEntity(customer);
		customerRepository.save(customerEntity);
	}

	@Override
	public void deleteById(Integer id) {
		log.info("Eliminando cliente con id: {}", id);
		customerRepository.deleteById(id);
	}

	public boolean existsByDni(String dni) {
		return customerRepository.existsByDni(dni);
	}

	public CustomerDTO findByDni(String dni) {
		CustomerEntity entity = customerRepository.findByDni(dni);
		return entity != null ? customerMapDTO.toDto(entity) : null;
	}
}
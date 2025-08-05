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

	@Override
	public boolean existsByDni(String dni) {
		return customerRepository.existsByDni(dni);
	}

	@Override
	public CustomerDTO findByDni(String dni) {
		CustomerEntity entity = customerRepository.findByDni(dni);
		return entity != null ? customerMapDTO.toDto(entity) : null;
	}
	
	@Override
	public List<CustomerDTO> findByDniContaining(String dni) {
		log.info("Buscando clientes con DNI que contiene: {}", dni);
		List<CustomerEntity> entities = customerRepository.findByDniContaining(dni);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> findByNameContaining(String name) {
		log.info("Buscando clientes con nombre que contiene: {}", name);
		List<CustomerEntity> entities = customerRepository.findByNameContainingIgnoreCase(name);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> findByAddressContaining(String address) {
		log.info("Buscando clientes con dirección que contiene: {}", address);
		List<CustomerEntity> entities = customerRepository.findByAddressContainingIgnoreCase(address);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> findByEmailContaining(String email) {
		log.info("Buscando clientes con email que contiene: {}", email);
		List<CustomerEntity> entities = customerRepository.findByEmailContainingIgnoreCase(email);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> findByPhoneContaining(String phone) {
		log.info("Buscando clientes con teléfono que contiene: {}", phone);
		List<CustomerEntity> entities = customerRepository.findByPhoneNumberContaining(phone);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> findByAnyField(String searchTerm) {
		log.info("Buscando clientes con término en cualquier campo: {}", searchTerm);
		List<CustomerEntity> entities = customerRepository.findByAnyField(searchTerm);
		return customerMapDTO.listCustomerToListCustomerDTO(entities);
	}

	@Override
	public List<CustomerDTO> filterCustomers(String searchTerm, String field) {
		log.info("Filtrando clientes por campo: {} con término: {}", field, searchTerm);
		
		if (searchTerm == null || searchTerm.trim().isEmpty()) {
			return findAll();
		}
		
		switch (field) {
			case "dni":
				return findByDniContaining(searchTerm);
			case "name":
				return findByNameContaining(searchTerm);
			case "address":
				return findByAddressContaining(searchTerm);
			case "email":
				return findByEmailContaining(searchTerm);
			case "phone":
				return findByPhoneContaining(searchTerm);
			case "all":
			default:
				return findByAnyField(searchTerm);
		}
	}
}
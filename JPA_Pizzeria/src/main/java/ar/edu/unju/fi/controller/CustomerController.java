package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	private ICustomerService customerService;
	
	/**
	 * Método que muestra la lista de clientes
	 * Accesible para cualquier usuario autenticado
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/lista")
	public String getListadoClientesPage(Model model) {
		model.addAttribute("clientes", customerService.findAll());
		log.info("INFO - Mostrando listado de clientes en /clientes/listado");
		return "lists/listaClientes"; // Devuelve el nombre de la vista html
	}

	/**
	 * Método que muestra el formulario para crear un nuevo cliente
	 * Accesible solo para ADMIN y OPERADOR
	 */
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
	@GetMapping("/nuevo")
	public String getNuevoClientePage(Model model) {
		model.addAttribute("cliente", new CustomerDTO());
		model.addAttribute("isEdit", false);
		log.info("INFO - Mostrando formulario para nuevo cliente en /clientes/nuevo");
		return "forms/form-cliente";
	}

	/**
	 * Método que guarda un cliente nuevo o modificado
	 * Accesible solo para ADMIN y OPERADOR
	 */
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
	@PostMapping("/guardar")
	public String saveCliente(@Valid @ModelAttribute("cliente") CustomerDTO cliente, BindingResult bindingResult, Model model) {
		boolean isEdit = cliente.getIdCustomer() != null;
		// Validación de DNI único solo si es alta
		if (!isEdit && customerService.existsByDni(cliente.getDni())) {
			log.warn("WARN - El DNI {} ya existe en la base de datos.", cliente.getDni());
			bindingResult.rejectValue("dni", "error.cliente", "El DNI ingresado ya existe. Por favor, ingrese uno diferente.");
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("isEdit", isEdit);
			return "forms/form-cliente";
		}
		
		customerService.save(cliente);
		// Log de éxito
		if (isEdit) {
			log.info("INFO - Cliente con ID {} actualizado correctamente.", cliente.getIdCustomer());
		} else {
			log.info("INFO - Nuevo cliente creado con DNI: {}", cliente.getDni());
		}
		return "redirect:/customers/lista";
	}
	
	/**
	 * Método que muestra el formulario para editar un cliente existente
	 * Accesible solo para ADMIN y OPERADOR
	 */
	@PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
	@GetMapping("/editar/{id}")
    public String getEditarClientePage(@PathVariable(value="id") Integer id, Model model) {
        CustomerDTO foundCustomer = customerService.findById(id);
        if (foundCustomer == null) {
            log.warn("WARN - Cliente con ID {} no encontrado para edición.", id);
            return "redirect:/customers/lista"; // Redirigir si no se encuentra
        }
        model.addAttribute("cliente", foundCustomer);
        model.addAttribute("isEdit", true); // Indica que es una edición
        log.info("INFO - Mostrando formulario para editar cliente con ID: {}", id);
        return "forms/form-cliente";
    }
	
	/**
	 * Método que elimina un cliente por su ID
	 * Accesible solo para ADMIN
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{id}")
	public String deleteCliente(@PathVariable(value="id") Integer id) {
		CustomerDTO foundCustomer = customerService.findById(id);
		if (foundCustomer == null) {
			log.warn("WARN - Cliente con ID {} no encontrado para eliminar.", id);
			return "redirect:/customers/lista"; // Redirigir si no se encuentra
		}
		customerService.deleteById(id);
		log.info("INFO - Cliente con ID {} eliminado correctamente.", id);
		return "redirect:/customers/lista";
	}
}
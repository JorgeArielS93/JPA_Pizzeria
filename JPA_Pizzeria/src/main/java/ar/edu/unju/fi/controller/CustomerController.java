package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	private ICustomerService customerService;
	
	
	@GetMapping("/lista")
	public String getListadoClientesPage(Model model) {
		model.addAttribute("clientes", customerService.findAll());
		log.info("INFO - Mostrando listado de clientes en /clientes/listado");
		return "lists/listaClientes"; // Devuelve el nombre de la vista html
	}
	
	@GetMapping("/nuevo")
	public String getNuevoClientePage(Model model) {
		model.addAttribute("cliente", new CustomerDTO());
		model.addAttribute("isEdit", false);
		log.info("INFO - Mostrando formulario para nuevo cliente en /clientes/nuevo");
		return "forms/form-cliente";
	}
	
}
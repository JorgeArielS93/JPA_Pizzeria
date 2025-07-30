package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.service.ICustomerService;
import ar.edu.unju.fi.service.IOrderItemService;
import ar.edu.unju.fi.service.IOrderService;
import ar.edu.unju.fi.service.IPizzaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IPizzaService pizzaService;
	
	@GetMapping("/lista")
	public String getListadoPedidosPage(Model model) {
		model.addAttribute("pedidos", orderService.getAllOrders());
		log.info("INFO - Mostrando listado de pedidos en /orders/lista");
		return "lists/listaPedidos";
	}
	
	@GetMapping("/nuevo")
	public String getNuevoPedidoPage(Model model) {
		OrderDTO order = new OrderDTO();
		order.setItems(new ArrayList<>());
		
		// Asegurar que los campos de selección inicien vacíos
		order.setIdCustomer(""); // Iniciar con string vacío para el idCustomer
		order.setMethod(""); // Iniciar con string vacío para el método de pago
		
		model.addAttribute("order", order);
		model.addAttribute("isEdit", false);
		model.addAttribute("listaClientes", customerService.findAll());
		model.addAttribute("pizzas", pizzaService.findAll());
		log.info("INFO - Mostrando formulario para nuevo pedido en /orders/nuevo");
		return "forms/form-order";
	}

	@PostMapping("/nuevo")
	public String modificarItems(
			@ModelAttribute OrderDTO order,
			Model model,
			@RequestParam(value = "addItem", required = false) String addItem,
			@RequestParam(value = "removeItem", required = false) Integer removeItemIndex,
			@RequestParam(value = "selectedPizza", required = false) Integer selectedPizzaId,
			@RequestParam(value = "selectedQuantity", required = false) Integer selectedQuantity) {
			
			// Inicializar lista de items si es null
			if (order.getItems() == null) {
				order.setItems(new ArrayList<>());
			}
			
			// Agregar item si se solicitó
			if (addItem != null && selectedPizzaId != null && selectedQuantity != null) {
				log.info("INFO - Agregando pizza con ID {} y cantidad {}", selectedPizzaId, selectedQuantity);
				
				// Buscar si ya existe un item con la misma pizza
				boolean pizzaExistente = false;
				for (OrderItemDTO existingItem : order.getItems()) {
					if (existingItem.getIdPizza() != null && existingItem.getIdPizza().equals(selectedPizzaId)) {
						// Actualizar la cantidad en vez de crear un nuevo item
						existingItem.setQuantity(existingItem.getQuantity() + selectedQuantity);
						pizzaExistente = true;
						log.info("INFO - Pizza ya existente. Actualizando cantidad a {}", existingItem.getQuantity());
						break;
					}
				}
				
				// Si no existe un item con esa pizza, crear uno nuevo
				if (!pizzaExistente) {
					OrderItemDTO newItem = new OrderItemDTO();
					PizzaDTO pizza = pizzaService.findById(selectedPizzaId);
					newItem.setPizza(pizza);
					newItem.setIdPizza(selectedPizzaId);
					newItem.setQuantity(selectedQuantity);
					newItem.setPrice(pizza.getPrice());
					order.getItems().add(newItem);
					log.info("INFO - Agregada nueva pizza a la orden");
				}
			}
			
			// Quitar item si se solicitó
			if (removeItemIndex != null && removeItemIndex >= 0 && removeItemIndex < order.getItems().size()) {
				log.info("INFO - Quitando pizza en posición {}", removeItemIndex);
				order.getItems().remove((int)removeItemIndex);
			}
			
			// Refrescar los datos de pizza en todos los ítems
			for (OrderItemDTO item : order.getItems()) {
				if ((item.getPizza() == null || item.getPizza().getName() == null) && item.getIdPizza() != null) {
					PizzaDTO pizzaItem = pizzaService.findById(item.getIdPizza());
					item.setPizza(pizzaItem);
				}
			}
			
			// Calcular el total
			double total = 0;
			for (OrderItemDTO item : order.getItems()) {
				if (item.getQuantity() != null && item.getPrice() != null) {
					total += item.getQuantity() * item.getPrice();
				}
			}
			order.setTotal(total);
			
			model.addAttribute("order", order);
			model.addAttribute("isEdit", false);
			model.addAttribute("listaClientes", customerService.findAll());
			model.addAttribute("pizzas", pizzaService.findAll());
			return "forms/form-order";
	}
	
	@PostMapping("/guardar")
	public String guardarOrder(@ModelAttribute("order") OrderDTO order,
			BindingResult bindingResult,
			Model model) {
		boolean isEdit = order.getIdOrder() != null;
		
		// Inicializar campos requeridos
		if (!isEdit) {
			// Solo asignamos la fecha actual para nuevas órdenes
			order.setDate(LocalDate.now());
			log.info("INFO - Asignando fecha actual a nueva orden: {}", order.getDate());
		} else {
			// Para órdenes existentes, si no tiene fecha, asignar la actual
			if (order.getDate() == null) {
				order.setDate(LocalDate.now());
				log.info("INFO - Asignando fecha actual a orden existente sin fecha: {}", order.getDate());
			} else {
				log.info("INFO - Manteniendo fecha original de la orden: {}", order.getDate());
			}
		}
		
		// Inicializar la lista de ítems si es null
		if (order.getItems() == null) {
			order.setItems(new ArrayList<>());
		}
		
		// Asignar el CustomerDTO correspondiente
		if (order.getIdCustomer() != null && !order.getIdCustomer().isEmpty()) {
			try {
				Integer id = Integer.valueOf(order.getIdCustomer());
				CustomerDTO foundCustomer = customerService.findById(id);
				order.setCustomer(foundCustomer);
				log.info("INFO - Cliente asignado: {}", (foundCustomer != null ? foundCustomer.getName() : "null"));
			} catch (Exception e) {
				log.error("ERROR - Error al obtener el cliente: {}", e.getMessage());
				order.setCustomer(null);
			}
		}
		
		// Calcular el total
		double total = 0.0;
		for (OrderItemDTO item : order.getItems()) {
			if (item.getQuantity() != null && item.getPrice() != null) {
				total += item.getQuantity() * item.getPrice();
			}
		}
		order.setTotal(total);
		
		// --- Realizar validaciones manuales ---
		boolean hasErrors = false;
		
		// Validar cliente
		if (order.getCustomer() == null) {
			bindingResult.rejectValue("customer", "NotNull", "Debe seleccionar un cliente válido");
			hasErrors = true;
		}
		
		// Validar método de pago
		if (order.getMethod() == null || order.getMethod().isEmpty()) {
			bindingResult.rejectValue("method", "NotBlank", "El método de pago no puede estar vacío");
			hasErrors = true;
		}
		
		// Validar items
		if (order.getItems().isEmpty()) {
			bindingResult.rejectValue("items", "Size", "Debe agregar al menos un ítem al pedido");
			hasErrors = true;
		}
		
		// Si hay errores, volver al formulario
		if (hasErrors || bindingResult.hasErrors()) {
			model.addAttribute("order", order);
			model.addAttribute("isEdit", isEdit);
			model.addAttribute("listaClientes", customerService.findAll());
			model.addAttribute("pizzas", pizzaService.findAll());
			log.warn("WARN - Error de validación al guardar orden: {}", bindingResult.getAllErrors());
			return "forms/form-order";
		}
		
		// Todo OK, guardar la orden
		orderService.saveOrder(order);
		if (isEdit) {
			log.info("INFO - Orden con ID {} actualizada correctamente.", order.getIdOrder());
		} else {
			log.info("INFO - Nueva orden creada.");
		}
		return "redirect:/orders/lista";
	}

	@GetMapping("/editar/{id}")
	public String getEditarOrderPage(@PathVariable("id") Integer id, Model model) {
		OrderDTO orderEncontrada = orderService.getOrderById(id);
		if (orderEncontrada == null) {
			log.warn("WARN - No se encontró orden con ID {} para editar. Redirigiendo a la lista.", id);
			return "redirect:/orders/lista";
		}
		model.addAttribute("order", orderEncontrada);
		model.addAttribute("isEdit", true);
		model.addAttribute("listaClientes", customerService.findAll());
		model.addAttribute("pizzas", pizzaService.findAll());
		log.info("INFO - Mostrando formulario para editar orden con ID: {}", id);
		return "forms/form-order";
	}

	@GetMapping("/borrar/{id}")
	public String eliminarOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		orderService.deleteOrder(id);
		log.info("INFO - Orden eliminada con ID: {}", id);
		return "redirect:/orders/lista";
	}
}
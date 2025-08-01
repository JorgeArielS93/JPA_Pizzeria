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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Nueva importación para redirecciones con datos

import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.service.IPizzaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private IPizzaService pizzaService;
    /**
	 * Muestra el listado de pizzas.
	 * Accesible para cualquier usuario autenticado
	 * @param model El modelo para pasar datos a la vista.
	 * @return El nombre de la vista que muestra el listado de pizzas.
	 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lista") // Cambiado a /list para coherencia con el navbar
    public String getListadoPizzasPage(Model model) {
        model.addAttribute("pizzas", pizzaService.findAll());
        log.info("INFO - Mostrando listado de pizzas en /pizzas/lista");
        return "lists/listaPizzas"; // Devuelve el nombre de la vista
    }
	/**
	 * Muestra el formulario para crear una nueva pizza.
	 * Accesible solo para ADMIN
	 * @param model El modelo para pasar datos a la vista.
	 * @return El nombre de la vista que muestra el formulario para crear una nueva pizza.
	 */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nuevo")
    public String getNuevaPizzaPage(Model model) {
        model.addAttribute("pizza", new PizzaDTO() ); // Inicializa un nuevo objeto PizzaDTO
        model.addAttribute("isEdit", false);
        log.info("INFO - Mostrando formulario para nueva pizza en /pizzas/nuevo");
        return "forms/form-pizza"; // Devuelve el nombre de la vista
    }

    /**
     * Guarda una nueva pizza o actualiza una existente.
     * Accesible solo para ADMIN
     * @param pizza La pizza a guardar.
     * @param bindingResult Resultados de la validación.
     * @param model El modelo para pasar datos a la vista.
     * @return Redirección a la lista de pizzas o regresa al formulario en caso de errores.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public String guardarPizza(@Valid @ModelAttribute("pizza") PizzaDTO pizza,
                               BindingResult bindingResult,
                               Model model // Necesario para agregar atributos si hay errores
                               ) { // Nuevo parámetro
    	boolean isEdit = pizza.getIdPizza() != null; // Verifica si es una edición o una nueva pizza
        if (bindingResult.hasErrors()) {
            log.warn("WARN - Error de validación al guardar pizza: {}", bindingResult.getAllErrors());
            // Si hay errores, volvemos a la misma vista del formulario y el 'Model' ya contiene el 'pizza' con errores
            model.addAttribute("isEdit", isEdit);
            return "forms/form-pizza";
        }

        pizzaService.save(pizza);
        
        if (isEdit) {
			log.info("INFO - Pizza con ID {} actualizada correctamente.", pizza.getIdPizza());
		} else {
			log.info("INFO - Nueva pizza creada con nombre: {}", pizza.getName());
		}
        return "redirect:/pizzas/lista"; // Redirecciona usando el prefijo "redirect:"
    }

    /**
	 * Muestra el formulario para editar una pizza existente.
     * Accesible solo para ADMIN
	 * @param id El ID de la pizza a editar.
	 * @param model El modelo para pasar datos a la vista.
	 * @return El nombre de la vista que muestra el formulario para editar una pizza.
	 */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}") // Cambiado a /edit para coherencia
    public String getEditarPizzaPage(@PathVariable("id") Integer id, Model model) {
        PizzaDTO pizzaEncontrada = pizzaService.findById(id);
        if (pizzaEncontrada == null) {
            log.warn("WARN - No se encontró pizza con ID {} para editar. Redirigiendo a la lista.", id);
            return "redirect:/pizzas/lista";
        }
        
        model.addAttribute("pizza", pizzaEncontrada);
        model.addAttribute("isEdit", true);
        log.info("INFO - Mostrando formulario para editar pizza con ID: {}", id);
        return "forms/form-pizza";
    }
    
    /**
	 * Elimina una pizza por su ID.
     * Accesible solo para ADMIN
	 * @param id El ID de la pizza a eliminar.
	 * @param redirectAttributes Para pasar mensajes flash a la redirección.
	 * @return Redirige a la lista de pizzas después de eliminar.
	 */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/borrar/{id}")
    public String eliminarPizza(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        pizzaService.deleteById(id);
        log.info("INFO - Pizza eliminada con ID: {}", id);
        return "redirect:/pizzas/lista";
    }
}
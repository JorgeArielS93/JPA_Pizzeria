package ar.edu.unju.fi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Ya lo usas, pero es clave aquí
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.ModelAndView; // ¡Esta importación ya no es necesaria!
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
     * Muestra la página con el listado de todas las pizzas disponibles.
     * La URL es /pizzas/list.
     */
    @GetMapping("/lista") // Cambiado a /list para coherencia con el navbar
    public String getListadoPizzasPage(Model model) {
        model.addAttribute("pizzas", pizzaService.findAll());
        log.info("INFO - Mostrando listado de pizzas en /pizzas/list");
        return "pizzas"; // Devuelve el nombre de la vista
    }

    /**
     * Muestra el formulario para crear una nueva pizza.
     * La URL es /pizzas/new.
     */
    @GetMapping("/nuevo") // Cambiado a /new para coherencia con el navbar
    public String getNuevaPizzaPage(Model model) {
        model.addAttribute("pizza", new PizzaDTO());
        model.addAttribute("isEdit", false);
        log.info("INFO - Mostrando formulario para nueva pizza en /pizzas/nuevo");
        return "forms/form-pizza"; // Devuelve el nombre de la vista
    }

    /**
     * Procesa la solicitud para guardar una pizza (tanto para crear como para editar).
     * @param redirectAttributes Para pasar atributos a la redirección (ej. mensajes flash).
     */
    @PostMapping("/guardar")
    public String guardarPizza(@Valid @ModelAttribute("pizza") PizzaDTO pizza,
                               BindingResult bindingResult,
                               Model model, // Necesario para agregar atributos si hay errores
                               RedirectAttributes redirectAttributes) { // Nuevo parámetro
        if (bindingResult.hasErrors()) {
            log.warn("WARN - Error de validación al guardar pizza: {}", bindingResult.getAllErrors());
            // Si hay errores, volvemos a la misma vista del formulario y el 'Model' ya contiene el 'pizza' con errores
            model.addAttribute("isEdit", pizza.getIdPizza() != null && pizza.getIdPizza() != 0);
            return "forms/form-pizza";
        }

        pizzaService.save(pizza);
        log.info("INFO - Pizza guardada con ID: {}", pizza.getIdPizza());
        // Puedes añadir un mensaje flash para mostrar en la página de lista
        redirectAttributes.addFlashAttribute("message", "Pizza guardada exitosamente!");
        return "redirect:/pizzas/lista"; // Redirecciona usando el prefijo "redirect:"
    }

    /**
     * Muestra el formulario para editar una pizza existente.
     */
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
     * Procesa la solicitud para eliminar una pizza.
     * @param redirectAttributes Para pasar atributos a la redirección (ej. mensajes flash).
     */
    @GetMapping("/borrar/{id}") // Cambiado a /delete para coherencia
    public String eliminarPizza(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        pizzaService.deleteById(id);
        log.info("INFO - Pizza eliminada con ID: {}", id);
        redirectAttributes.addFlashAttribute("message", "Pizza eliminada exitosamente!");
        return "redirect:/pizzas/lista";
    }
}
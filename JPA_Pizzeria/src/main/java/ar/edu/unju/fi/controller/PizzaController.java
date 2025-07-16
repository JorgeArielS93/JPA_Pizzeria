package ar.edu.unju.fi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pizza")
public class PizzaController {
	@GetMapping("")
	public String mostrarFormularioPizzas() {
        return "form-cliente.html";
    }
}

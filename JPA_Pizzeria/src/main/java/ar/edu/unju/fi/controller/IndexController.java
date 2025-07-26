package ar.edu.unju.fi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/principal")
public class IndexController {

	@GetMapping({ "/inicio", "/home" })
	public String getIndex() {
		log.info("INFO - Se ha accedido a la URL /principal/inicio o /principal/home");
		log.info("Navegando a la página de inicio (index.html)");

		return "index";
	}
}
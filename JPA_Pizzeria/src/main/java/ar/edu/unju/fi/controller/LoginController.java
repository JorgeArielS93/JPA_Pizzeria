package ar.edu.unju.fi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

    /**
     * Método que maneja la petición a la página de login
     * @return la vista de login
     */
    @GetMapping("/login")
    public String login() {
        log.info("Accediendo a la página de login");
        return "login";
    }
    
    /**
     * Método que maneja la petición a la página principal
     * @return redirecciona al home
     */
    @GetMapping("/")
    public String index() {
        log.info("Redireccionando a la página de inicio desde la raíz");
        return "redirect:/home";
    }
    
    /**
     * Método que maneja la página principal de la aplicación
     * @param model Modelo para agregar atributos
     * @return la vista de home
     */
    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();
        String username = auth.getName();
        
        log.info("Usuario '{}' con rol '{}' accediendo a home", username, role);
        
        model.addAttribute("username", username);
        model.addAttribute("role", role.replace("ROLE_", ""));
        return "home";
    }
    
    /**
     * Método que maneja la página de acceso denegado
     * @param model Modelo para agregar atributos
     * @return la vista de acceso-denegado
     */
    @GetMapping("/acceso-denegado")
    public String accessDenied(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.warn("Usuario '{}' intentó acceder a un recurso restringido", auth.getName());
        
        model.addAttribute("username", auth.getName());
        return "error/acceso-denegado";
    }
    
    /**
     * Endpoint de prueba para operadores
     */
    @GetMapping("/operador/dashboard")
    public String operadorDashboard() {
        log.info("Accediendo al panel de operador");
        return "operador/dashboard";
    }
}
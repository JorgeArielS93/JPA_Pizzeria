package ar.edu.unju.fi.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.unju.fi.dto.UserDTO;
import ar.edu.unju.fi.service.IUsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador para la gestión de usuarios del sistema
 */
@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class UserManagementController {
    
    @Autowired
    private IUsuarioService usuarioService;
    
    /**
     * Lista todos los usuarios activos
     */
    @GetMapping
    public String listUsers(Model model) {
        log.info("Mostrando lista de usuarios activos");
        List<UserDTO> usuarios = usuarioService.obtenerUsuariosPorEstado(true);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Usuarios Activos");
        return "admin/users/lista";
    }
    
    /**
     * Lista usuarios inactivos
     */
    @GetMapping("/inactive")
    public String listInactiveUsers(Model model) {
        log.info("Mostrando lista de usuarios inactivos");
        List<UserDTO> usuarios = usuarioService.obtenerUsuariosPorEstado(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Usuarios Inactivos");
        return "admin/users/lista";
    }
    
    /**
     * Formulario para crear nuevo usuario
     */
    @GetMapping("/new")
    public String newUserForm(Model model) {
        log.info("Mostrando formulario para nuevo usuario");
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", getRolesDisponibles());
        model.addAttribute("accion", "crear");
        return "admin/users/form";
    }
    
    /**
     * Guardar nuevo usuario
     */
    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO, 
                          BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        log.info("Procesando guardar usuario: {}", userDTO.getUsername());
        
        if (result.hasErrors()) {
            log.warn("Error en validación de formulario para: {}", userDTO.getUsername());
            model.addAttribute("roles", getRolesDisponibles());
            model.addAttribute("accion", userDTO.getId() == null ? "crear" : "editar");
            return "admin/users/form";
        }
        
        try {
            if (userDTO.getId() == null) {
                // Crear nuevo usuario
                usuarioService.crearNuevoUsuario(userDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado con éxito");
            } else {
                // Actualizar usuario existente
                usuarioService.actualizarUsuarioDesdeDTO(userDTO.getId(), userDTO);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado con éxito");
            }
            
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("Error al guardar usuario: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", getRolesDisponibles());
            model.addAttribute("accion", userDTO.getId() == null ? "crear" : "editar");
            return "admin/users/form";
        }
    }
    
    /**
     * Formulario para editar usuario
     */
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        log.info("Mostrando formulario para editar usuario con ID: {}", id);
        
        try {
            UserDTO userDTO = usuarioService.obtenerUsuarioDTOPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("roles", getRolesDisponibles());
            model.addAttribute("accion", "editar");
            return "admin/users/form";
        } catch (Exception e) {
            log.error("Error al editar usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }
    
    /**
     * Cambiar estado de usuario (activar/desactivar)
     */
    @GetMapping("/toggle/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Cambiando estado de usuario con ID: {}", id);
        
        try {
            UserDTO user = usuarioService.obtenerUsuarioDTOPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            usuarioService.cambiarEstadoUsuario(id, !user.isActivo());
            
            String nuevoEstado = !user.isActivo() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", 
                "Usuario " + user.getUsername() + " " + nuevoEstado + " con éxito");
            
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("Error al cambiar estado del usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }
    
    /**
     * Ver página para gestionar roles
     */
    @GetMapping("/roles")
    public String manageRoles(Model model) {
        log.info("Mostrando página de gestión de roles");
        
        List<UserDTO> usuarios = usuarioService.obtenerTodosLosUsuariosDTO();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", getRolesDisponibles());
        
        return "admin/users/roles";
    }
    
    /**
     * Actualizar roles de un usuario
     */
    @PostMapping("/updateRoles")
    public String updateUserRoles(@RequestParam Long userId, 
                                 @RequestParam(required = false) List<String> selectedRoles,
                                 RedirectAttributes redirectAttributes) {
        
        log.info("Actualizando roles para usuario con ID: {}", userId);
        
        try {
            Set<String> roles = new HashSet<>();
            if (selectedRoles != null) {
                roles.addAll(selectedRoles);
            }
            
            usuarioService.actualizarRolesUsuario(userId, roles);
            redirectAttributes.addFlashAttribute("mensaje", "Roles actualizados con éxito");
        } catch (Exception e) {
            log.error("Error al actualizar roles: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/users/roles";
    }
    
    /**
     * Obtiene los roles disponibles en el sistema
     */
    private Set<String> getRolesDisponibles() {
        // Aquí deberías obtener los roles desde el repositorio de roles
        // Por ahora, usamos valores estáticos
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("OPERADOR");
        return roles;
    }
}
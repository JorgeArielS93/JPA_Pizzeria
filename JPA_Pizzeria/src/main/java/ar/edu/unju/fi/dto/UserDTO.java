package ar.edu.unju.fi.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String username;
    private String password; // Solo se usa para creación/modificación
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaModificacion;
    private Set<String> roles = new HashSet<>();
    
    // Constructor sin password para listar usuarios de forma segura
    public UserDTO(Long id, String username, String nombre, String apellido, String email, 
                 boolean activo, LocalDateTime fechaCreacion, LocalDateTime ultimaModificacion, 
                 Set<String> roles) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.ultimaModificacion = ultimaModificacion;
        this.roles = roles;
    }
}
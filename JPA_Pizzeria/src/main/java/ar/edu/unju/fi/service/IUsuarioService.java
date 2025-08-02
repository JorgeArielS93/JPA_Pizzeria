package ar.edu.unju.fi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ar.edu.unju.fi.dto.UserDTO;
import ar.edu.unju.fi.entity.Rol;
import ar.edu.unju.fi.entity.Usuario;

/**
 * Interfaz para el servicio de usuarios
 */
public interface IUsuarioService {
    
    /**
     * Guarda un usuario en la base de datos
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    Usuario guardarUsuario(Usuario usuario);
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<Usuario> buscarPorId(Long id);
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username el nombre de usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<Usuario> buscarPorUsername(String username);
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<Usuario> buscarPorEmail(String email);
    
    /**
     * Obtiene todos los usuarios
     * @return lista de usuarios
     */
    List<Usuario> listarUsuarios();
    
    /**
     * Actualiza un usuario existente
     * @param usuario el usuario con los datos actualizados
     * @return el usuario actualizado
     */
    Usuario actualizarUsuario(Usuario usuario);
    
    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     */
    void eliminarUsuario(Long id);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado
     * @param username el nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existePorUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email proporcionado
     * @param email el email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existePorEmail(String email);
    
    /**
     * Asigna un rol a un usuario
     * @param usuarioId el ID del usuario
     * @param rolNombre el nombre del rol
     * @return el usuario actualizado
     */
    Usuario asignarRol(Long usuarioId, String rolNombre);
    
    /**
     * Quita un rol a un usuario
     * @param usuarioId el ID del usuario
     * @param rolNombre el nombre del rol
     * @return el usuario actualizado
     */
    Usuario quitarRol(Long usuarioId, String rolNombre);
    
    // --- Métodos para la gestión de usuarios desde el dashboard ---
    
    /**
     * Obtiene todos los usuarios del sistema convertidos a DTO
     * @return Lista de usuarios como DTOs
     */
    List<UserDTO> obtenerTodosLosUsuariosDTO();
    
    /**
     * Obtiene un usuario por su ID y lo convierte a DTO
     * @param id ID del usuario
     * @return DTO del usuario
     */
    Optional<UserDTO> obtenerUsuarioDTOPorId(Long id);
    
    /**
     * Crea un nuevo usuario a partir de un DTO
     * @param userDTO Datos del usuario
     * @return Usuario creado como DTO
     */
    UserDTO crearNuevoUsuario(UserDTO userDTO);
    
    /**
     * Actualiza los roles de un usuario
     * @param id ID del usuario
     * @param roles Set de nombres de roles
     * @return Usuario actualizado como DTO
     */
    UserDTO actualizarRolesUsuario(Long id, Set<String> roles);
    
    /**
     * Activa o desactiva un usuario
     * @param id ID del usuario
     * @param activo true para activar, false para desactivar
     * @return Usuario actualizado como DTO
     */
    UserDTO cambiarEstadoUsuario(Long id, boolean activo);
    
    /**
     * Obtiene los usuarios por estado (activo/inactivo)
     * @param activo Estado de los usuarios a buscar
     * @return Lista de usuarios como DTOs
     */
    List<UserDTO> obtenerUsuariosPorEstado(boolean activo);
    
    /**
     * Convierte un Usuario a UserDTO
     * @param usuario Entidad Usuario
     * @return UserDTO
     */
    UserDTO convertirADTO(Usuario usuario);
    
    /**
     * Convierte un UserDTO a Usuario
     * @param userDTO DTO de usuario
     * @return Entidad Usuario
     */
    Usuario convertirAEntidad(UserDTO userDTO);
    Set<Rol> convertirNombresARoles(Set<String> rolNames);
    
    /**
     * Actualiza un usuario desde un DTO
     * @param id el ID del usuario a actualizar
     * @param userDTO el DTO con los datos actualizados
     * @return el DTO del usuario actualizado
     */
    UserDTO actualizarUsuarioDesdeDTO(Long id, UserDTO userDTO);
}

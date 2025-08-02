package ar.edu.unju.fi.service.imp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.dto.UserDTO;
import ar.edu.unju.fi.entity.Rol;
import ar.edu.unju.fi.entity.Usuario;
import ar.edu.unju.fi.mapper.UserMapDTO;
import ar.edu.unju.fi.repository.RolRepository;
import ar.edu.unju.fi.repository.UsuarioRepository;
import ar.edu.unju.fi.service.IUsuarioService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de usuarios
 */
@Service
@Slf4j
public class UsuarioServiceImp implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserMapDTO userMapper;

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        // Verificar si ya existe un usuario con el mismo username o email
        if (existePorUsername(usuario.getUsername())) {
            log.warn("Ya existe un usuario con el nombre: {}", usuario.getUsername());
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        if (existePorEmail(usuario.getEmail())) {
            log.warn("Ya existe un usuario con el email: {}", usuario.getEmail());
            throw new RuntimeException("El email ya está en uso");
        }
        
        // Cifrar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Establecer fechas
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setUltimaModificacion(LocalDateTime.now());
        
        log.info("Guardando nuevo usuario: {}", usuario.getUsername());
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        log.debug("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorUsername(String username) {
        log.debug("Buscando usuario con username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        log.debug("Buscando usuario con email: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        log.debug("Listando todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        log.info("Actualizando usuario con ID: {}", usuario.getId());
        
        // Verificar que el usuario existe
        Usuario usuarioActual = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", usuario.getId());
                    return new RuntimeException("No se encontró el usuario para actualizar");
                });
        
        // Verificar si el username ha cambiado y ya existe otro usuario con ese nombre
        if (!usuario.getUsername().equals(usuarioActual.getUsername())) {
            Optional<Usuario> existingUser = usuarioRepository.findByUsername(usuario.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
                log.warn("El username {} ya está en uso por otro usuario", usuario.getUsername());
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
        }
        
        // Verificar si el email ha cambiado y ya existe otro usuario con ese email
        if (!usuario.getEmail().equals(usuarioActual.getEmail())) {
            Optional<Usuario> existingUser = usuarioRepository.findByEmail(usuario.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
                log.warn("El email {} ya está en uso por otro usuario", usuario.getEmail());
                throw new RuntimeException("El email ya está en uso");
            }
        }
        
        // Si la contraseña no está establecida o es vacía, mantener la actual
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            usuario.setPassword(usuarioActual.getPassword());
        } else {
            // Si hay una nueva contraseña, cifrarla
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        // Mantener fecha de creación y actualizar fecha de modificación
        usuario.setFechaCreacion(usuarioActual.getFechaCreacion());
        usuario.setUltimaModificacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Usuario asignarRol(Long usuarioId, String rolNombre) {
        log.info("Asignando rol {} al usuario con ID: {}", rolNombre, usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", usuarioId);
                    return new RuntimeException("Usuario no encontrado");
                });
        
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> {
                    log.error("No se encontró el rol: {}", rolNombre);
                    return new RuntimeException("Rol no encontrado");
                });
        
        usuario.agregarRol(rol);
        usuario.setUltimaModificacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario quitarRol(Long usuarioId, String rolNombre) {
        log.info("Quitando rol {} al usuario con ID: {}", rolNombre, usuarioId);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", usuarioId);
                    return new RuntimeException("Usuario no encontrado");
                });
        
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> {
                    log.error("No se encontró el rol: {}", rolNombre);
                    return new RuntimeException("Rol no encontrado");
                });
        
        usuario.eliminarRol(rol);
        usuario.setUltimaModificacion(LocalDateTime.now());
        
        return usuarioRepository.save(usuario);
    }
    
    // --- Implementación de métodos para gestión de usuarios desde el dashboard ---
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerTodosLosUsuariosDTO() {
        log.debug("Obteniendo todos los usuarios como DTO");
        return userMapper.toUserDtoList(usuarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerUsuarioDTOPorId(Long id) {
        log.debug("Obteniendo usuario con ID {} como DTO", id);
        return usuarioRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserDTO crearNuevoUsuario(UserDTO userDTO) {
        log.info("Creando nuevo usuario a partir de DTO: {}", userDTO.getUsername());
        
        // Convertir DTO a entidad usando el mapper
        Usuario usuario = userMapper.toEntity(userDTO);
        
        // Verificar si ya existen username o email
        if (existePorUsername(usuario.getUsername())) {
            log.warn("El username {} ya está en uso", usuario.getUsername());
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        if (existePorEmail(usuario.getEmail())) {
            log.warn("El email {} ya está en uso", usuario.getEmail());
            throw new RuntimeException("El email ya está en uso");
        }
        
        // Cifrar contraseña y establecer fechas
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setUltimaModificacion(LocalDateTime.now());
        
        // Guardar y convertir de vuelta a DTO
        usuario = usuarioRepository.save(usuario);
        return userMapper.toDto(usuario);
    }

    @Override
    @Transactional
    public UserDTO actualizarRolesUsuario(Long id, Set<String> roles) {
        log.info("Actualizando roles para el usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        
        // Limpiar roles actuales
        usuario.getRoles().clear();
        
        // Asignar nuevos roles
        for (String rolNombre : roles) {
            Rol rol = rolRepository.findByNombre(rolNombre)
                    .orElseThrow(() -> {
                        log.error("No se encontró el rol: {}", rolNombre);
                        return new RuntimeException("Rol no encontrado: " + rolNombre);
                    });
            usuario.agregarRol(rol);
        }
        
        usuario.setUltimaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        
        return userMapper.toDto(usuario);
    }

    @Override
    @Transactional
    public UserDTO cambiarEstadoUsuario(Long id, boolean activo) {
        log.info("Cambiando estado de usuario con ID {} a: {}", id, activo ? "activo" : "inactivo");
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        
        usuario.setActivo(activo);
        usuario.setUltimaModificacion(LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);
        
        return userMapper.toDto(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerUsuariosPorEstado(boolean activo) {
        log.debug("Obteniendo usuarios con estado activo={}", activo);
        return usuarioRepository.findAll().stream()
                .filter(u -> u.isActivo() == activo)
                .map(userMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public UserDTO convertirADTO(Usuario usuario) {
        return userMapper.toDto(usuario);
    }

    @Override
    public Usuario convertirAEntidad(UserDTO userDTO) {
        return userMapper.toEntity(userDTO);
    }

    /**
     * Convierte un conjunto de nombres de roles en entidades Rol
     * @param rolNames nombres de los roles
     * @return conjunto de entidades Rol
     */
    public Set<Rol> convertirNombresARoles(Set<String> rolNames) {
        Set<Rol> roles = new HashSet<>();
        
        if (rolNames != null) {
            for (String rolName : rolNames) {
                rolRepository.findByNombre(rolName).ifPresent(roles::add);
            }
        }
        
        return roles;
    }

    /**
     * Actualiza un usuario desde un DTO
     * @param id el ID del usuario a actualizar
     * @param userDTO el DTO con los datos actualizados
     * @return el DTO del usuario actualizado
     */
    @Override
    @Transactional
    public UserDTO actualizarUsuarioDesdeDTO(Long id, UserDTO userDTO) {
        log.info("Actualizando usuario con ID {} desde DTO", id);
        
        // Verificar que el usuario existe
        Usuario usuarioActual = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con ID: {}", id);
                    return new RuntimeException("No se encontró el usuario para actualizar");
                });
        
        // Verificar si el username ha cambiado y ya existe otro usuario con ese nombre
        if (!userDTO.getUsername().equals(usuarioActual.getUsername())) {
            Optional<Usuario> existingUser = usuarioRepository.findByUsername(userDTO.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                log.warn("El username {} ya está en uso por otro usuario", userDTO.getUsername());
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
        }
        
        // Verificar si el email ha cambiado y ya existe otro usuario con ese email
        if (!userDTO.getEmail().equals(usuarioActual.getEmail())) {
            Optional<Usuario> existingUser = usuarioRepository.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                log.warn("El email {} ya está en uso por otro usuario", userDTO.getEmail());
                throw new RuntimeException("El email ya está en uso");
            }
        }
        
        // Guardar la contraseña actual antes de actualizar los campos desde el DTO
        String passwordActual = usuarioActual.getPassword();
        
        // Actualizar los campos del usuario desde el DTO
        userMapper.updateUsuarioFromDTO(userDTO, usuarioActual);
        
        // Si la contraseña está vacía, mantener la contraseña actual
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            usuarioActual.setPassword(passwordActual);
        } else {
            // Si hay una nueva contraseña, cifrarla
            usuarioActual.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        // Actualizar los roles si se proporcionan
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            usuarioActual.getRoles().clear();
            usuarioActual.getRoles().addAll(convertirNombresARoles(userDTO.getRoles()));
        }
        
        // Actualizar fecha de modificación
        usuarioActual.setUltimaModificacion(LocalDateTime.now());
        
        // Guardar y convertir de vuelta a DTO
        Usuario usuarioActualizado = usuarioRepository.save(usuarioActual);
        return userMapper.toDto(usuarioActualizado);
    }
}

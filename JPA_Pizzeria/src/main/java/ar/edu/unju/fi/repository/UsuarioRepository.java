package ar.edu.unju.fi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.Usuario;

/**
 * Repositorio para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username el nombre de usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si se encuentra
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario proporcionado
     * @param username el nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email proporcionado
     * @param email el email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}
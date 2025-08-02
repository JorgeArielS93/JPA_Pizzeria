package ar.edu.unju.fi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.entity.Rol;

/**
 * Repositorio para la entidad Rol
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    /**
     * Busca un rol por su nombre
     * @param nombre el nombre del rol
     * @return Optional con el rol si se encuentra
     */
    Optional<Rol> findByNombre(String nombre);
    
    /**
     * Verifica si existe un rol con el nombre proporcionado
     * @param nombre el nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
package ar.edu.unju.fi.service;

import java.util.List;
import java.util.Optional;

import ar.edu.unju.fi.entity.Rol;

/**
 * Interfaz para el servicio de roles
 */
public interface IRolService {
    
    /**
     * Guarda un rol en la base de datos
     * @param rol el rol a guardar
     * @return el rol guardado
     */
    Rol guardarRol(Rol rol);
    
    /**
     * Busca un rol por su ID
     * @param id el ID del rol
     * @return Optional con el rol si se encuentra
     */
    Optional<Rol> buscarPorId(Long id);
    
    /**
     * Busca un rol por su nombre
     * @param nombre el nombre del rol
     * @return Optional con el rol si se encuentra
     */
    Optional<Rol> buscarPorNombre(String nombre);
    
    /**
     * Obtiene todos los roles
     * @return lista de roles
     */
    List<Rol> listarRoles();
    
    /**
     * Actualiza un rol existente
     * @param rol el rol con los datos actualizados
     * @return el rol actualizado
     */
    Rol actualizarRol(Rol rol);
    
    /**
     * Elimina un rol por su ID
     * @param id el ID del rol a eliminar
     */
    void eliminarRol(Long id);
    
    /**
     * Verifica si existe un rol con el nombre proporcionado
     * @param nombre el nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existePorNombre(String nombre);
}
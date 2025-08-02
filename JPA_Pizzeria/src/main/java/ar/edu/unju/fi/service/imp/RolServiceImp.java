package ar.edu.unju.fi.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.entity.Rol;
import ar.edu.unju.fi.repository.RolRepository;
import ar.edu.unju.fi.service.IRolService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de roles
 */
@Service
@Slf4j
public class RolServiceImp implements IRolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional
    public Rol guardarRol(Rol rol) {
        if (existePorNombre(rol.getNombre())) {
            log.warn("Ya existe un rol con el nombre: {}", rol.getNombre());
            throw new RuntimeException("El nombre de rol ya está en uso");
        }
        
        Rol rolGuardado = rolRepository.save(rol);
        log.info("Rol guardado correctamente con ID: {}", rolGuardado.getId());
        return rolGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional
    public Rol actualizarRol(Rol rol) {
        if (rol.getId() == null) {
            log.error("No se puede actualizar un rol sin ID");
            throw new RuntimeException("ID de rol no proporcionado para actualización");
        }
        
        if (!rolRepository.existsById(rol.getId())) {
            log.error("No se encontró el rol con ID: {} para actualizar", rol.getId());
            throw new RuntimeException("Rol no encontrado");
        }
        
        // Si el nombre del rol cambia, verificar que no esté en uso
        Optional<Rol> existingRol = rolRepository.findById(rol.getId());
        if (existingRol.isPresent() && 
            !existingRol.get().getNombre().equals(rol.getNombre()) && 
            existePorNombre(rol.getNombre())) {
            log.warn("El nuevo nombre de rol {} ya está en uso", rol.getNombre());
            throw new RuntimeException("El nuevo nombre de rol ya está en uso");
        }
        
        Rol rolActualizado = rolRepository.save(rol);
        log.info("Rol con ID: {} actualizado correctamente", rol.getId());
        return rolActualizado;
    }

    @Override
    @Transactional
    public void eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            log.warn("No se encontró rol con ID: {} para eliminar", id);
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
        log.info("Rol con ID: {} eliminado correctamente", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }
}
package ar.edu.unju.fi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ar.edu.unju.fi.entity.Rol;
import ar.edu.unju.fi.entity.Usuario;
import ar.edu.unju.fi.service.IRolService;
import ar.edu.unju.fi.service.IUsuarioService;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente para inicializar datos en la aplicación
 * Crea roles y usuarios por defecto si no existen
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private IRolService rolService;
    
    @Autowired
    private IUsuarioService usuarioService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando datos de la aplicación...");
        
        // Crear roles si no existen
        initRoles();
        
        // Crear usuarios por defecto si no existen
        initUsuarios();
        
        log.info("Inicialización de datos completada.");
    }
    
    private void initRoles() {
        // Crear rol ADMIN si no existe
        if (!rolService.existePorNombre("ADMIN")) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setDescripcion("Administrador con acceso completo al sistema");
            rolService.guardarRol(rolAdmin);
            log.info("Rol ADMIN creado correctamente");
        }
        
        // Crear rol OPERADOR si no existe
        if (!rolService.existePorNombre("OPERADOR")) {
            Rol rolOperador = new Rol();
            rolOperador.setNombre("OPERADOR");
            rolOperador.setDescripcion("Operador con acceso limitado al sistema");
            rolService.guardarRol(rolOperador);
            log.info("Rol OPERADOR creado correctamente");
        }
    }
    
    private void initUsuarios() {
        // Crear usuario admin por defecto si no existe
        if (!usuarioService.existePorUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("adminpass");  // Se cifrará al guardar
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEmail("admin@pizzeria.com");
            
            Usuario adminGuardado = usuarioService.guardarUsuario(admin);
            usuarioService.asignarRol(adminGuardado.getId(), "ADMIN");
            log.info("Usuario administrador creado correctamente");
        }
        
        // Crear usuario operador por defecto si no existe
        if (!usuarioService.existePorUsername("operador")) {
            Usuario operador = new Usuario();
            operador.setUsername("operador");
            operador.setPassword("operadorpass");  // Se cifrará al guardar
            operador.setNombre("Operador");
            operador.setApellido("Sistema");
            operador.setEmail("operador@pizzeria.com");
            
            Usuario operadorGuardado = usuarioService.guardarUsuario(operador);
            usuarioService.asignarRol(operadorGuardado.getId(), "OPERADOR");
            log.info("Usuario operador creado correctamente");
        }
    }
}
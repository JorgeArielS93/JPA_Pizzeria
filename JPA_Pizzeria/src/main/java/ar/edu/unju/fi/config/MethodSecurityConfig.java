package ar.edu.unju.fi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Configuración para habilitar la seguridad basada en métodos usando anotaciones como @PreAuthorize
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
    // No se necesita código adicional, la anotación @EnableMethodSecurity activa las anotaciones de seguridad
}
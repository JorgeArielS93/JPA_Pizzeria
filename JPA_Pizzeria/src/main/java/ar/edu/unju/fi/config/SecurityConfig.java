package ar.edu.unju.fi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configurar el servicio personalizado de autenticación y el codificador de contraseñas
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Recursos públicos (CSS, JS, imágenes)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/bootstrap-icons-1.13.1/**").permitAll()
                
                // Páginas públicas
                .requestMatchers("/", "/login").permitAll()
                
                // Rutas administrativas - solo ADMIN
                .requestMatchers("/admin/**", "/pizzas/nuevo", "/pizzas/editar/**", "/pizzas/borrar/**").hasRole("ADMIN")
                
                // Rutas para operadores - OPERADOR o ADMIN
                .requestMatchers("/operador/**", "/orders/**", "/customers/nuevo", "/customers/editar/**").hasAnyRole("ADMIN", "OPERADOR")
                
                // Rutas de consulta - cualquier usuario autenticado
                .requestMatchers("/customers/lista", "/pizzas/lista").authenticated()
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login") // Nuestra página de login personalizada
                .defaultSuccessUrl("/home", true) // Redirección después del login exitoso
                .failureUrl("/login?error=true") // URL en caso de error de autenticación
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout=true") // Redirección después de logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling((exceptionHandling) -> exceptionHandling
                .accessDeniedPage("/acceso-denegado") // Página para acceso denegado
            );

        return http.build();
    }
}
package ar.edu.unju.fi.service.imp;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.entity.Usuario;
import ar.edu.unju.fi.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio que implementa UserDetailsService para Spring Security
 * Se encarga de cargar los usuarios desde la base de datos para la autenticación
 */
@Service
@Slf4j
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Intentando autenticar al usuario: {}", username);
        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });
        
        if (!usuario.isActivo()) {
            log.warn("El usuario {} está desactivado", username);
            throw new UsernameNotFoundException("Usuario desactivado: " + username);
        }
        
        Collection<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());
        
        log.info("Usuario {} autenticado con roles: {}", username, 
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        
        return new User(
            usuario.getUsername(), 
            usuario.getPassword(), 
            true,             // enabled
            true,             // accountNonExpired
            true,             // credentialsNonExpired
            true,             // accountNonLocked
            authorities
        );
    }
}
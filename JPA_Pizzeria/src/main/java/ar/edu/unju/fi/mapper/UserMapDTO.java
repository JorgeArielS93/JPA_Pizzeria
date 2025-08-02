package ar.edu.unju.fi.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import ar.edu.unju.fi.dto.UserDTO;
import ar.edu.unju.fi.entity.Rol;
import ar.edu.unju.fi.entity.Usuario;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapDTO {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(target = "password", ignore = true) // No mapear contraseña por seguridad
    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "apellido", target = "apellido")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "activo", target = "activo")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "ultimaModificacion", target = "ultimaModificacion")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToStringSet")
    UserDTO toDto(Usuario usuario);
    
    @InheritInverseConfiguration
    @Mapping(source = "password", target = "password") // Permitir mapeo de contraseña al convertir DTO a entidad
    @Mapping(source = "roles", target = "roles", ignore = true) // Ignorar el mapeo de roles - lo manejará el service
    Usuario toEntity(UserDTO userDTO);
    
    // Actualizar entidad existente con datos de DTO
    @Mapping(target = "id", ignore = true) // No actualizar el ID
    @Mapping(target = "fechaCreacion", ignore = true) // No actualizar la fecha de creación
    @Mapping(target = "roles", ignore = true) // Ignorar el mapeo de roles - lo manejará el service
    void updateUsuarioFromDTO(UserDTO dto, @MappingTarget Usuario usuario);
    
    List<UserDTO> toUserDtoList(List<Usuario> usuarios);
    
    List<Usuario> toUsuarioList(List<UserDTO> userDTOs);
    
    @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<Rol> roles) {
        return roles.stream()
                .map(Rol::getNombre)
                .collect(java.util.stream.Collectors.toSet());
    }
}
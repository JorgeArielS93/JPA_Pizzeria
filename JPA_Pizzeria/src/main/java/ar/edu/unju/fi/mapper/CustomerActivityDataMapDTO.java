package ar.edu.unju.fi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ar.edu.unju.fi.dto.CustomerActivityDataDTO;
import ar.edu.unju.fi.entity.dashboard.CustomerActivityData;

/**
 * Mapper para convertir entre CustomerActivityData y CustomerActivityDataDTO
 */
@Mapper(componentModel = "spring")
public interface CustomerActivityDataMapDTO {
    
    CustomerActivityDataMapDTO INSTANCE = Mappers.getMapper(CustomerActivityDataMapDTO.class);
    
    CustomerActivityDataDTO entityToDto(CustomerActivityData entity);
    
    CustomerActivityData dtoToEntity(CustomerActivityDataDTO dto);
}
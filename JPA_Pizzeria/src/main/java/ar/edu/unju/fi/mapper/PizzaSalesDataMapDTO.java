package ar.edu.unju.fi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ar.edu.unju.fi.dto.PizzaSalesDataDTO;
import ar.edu.unju.fi.entity.dashboard.PizzaSalesData;

/**
 * Mapper para convertir entre PizzaSalesData y PizzaSalesDataDTO
 */
@Mapper(componentModel = "spring")
public interface PizzaSalesDataMapDTO {
    
    PizzaSalesDataMapDTO INSTANCE = Mappers.getMapper(PizzaSalesDataMapDTO.class);
    
    PizzaSalesDataDTO entityToDto(PizzaSalesData entity);
    
    PizzaSalesData dtoToEntity(PizzaSalesDataDTO dto);
}
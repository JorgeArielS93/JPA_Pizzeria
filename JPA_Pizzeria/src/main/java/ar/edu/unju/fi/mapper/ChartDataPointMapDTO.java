package ar.edu.unju.fi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import ar.edu.unju.fi.dto.ChartDataPointDTO;
import ar.edu.unju.fi.entity.dashboard.ChartDataPoint;

/**
 * Mapper para convertir entre ChartDataPoint y ChartDataPointDTO
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChartDataPointMapDTO {
    
    ChartDataPointMapDTO INSTANCE = Mappers.getMapper(ChartDataPointMapDTO.class);
    
    ChartDataPointDTO entityToDto(ChartDataPoint entity);
    
    ChartDataPoint dtoToEntity(ChartDataPointDTO dto);
}
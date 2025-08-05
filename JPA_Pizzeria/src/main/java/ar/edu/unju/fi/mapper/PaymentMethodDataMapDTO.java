package ar.edu.unju.fi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import ar.edu.unju.fi.dto.PaymentMethodDataDTO;
import ar.edu.unju.fi.entity.dashboard.PaymentMethodData;

/**
 * Mapper para convertir entre PaymentMethodData y PaymentMethodDataDTO
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMethodDataMapDTO {
    
    PaymentMethodDataMapDTO INSTANCE = Mappers.getMapper(PaymentMethodDataMapDTO.class);
    
    PaymentMethodDataDTO entityToDto(PaymentMethodData entity);
    
    PaymentMethodData dtoToEntity(PaymentMethodDataDTO dto);
}
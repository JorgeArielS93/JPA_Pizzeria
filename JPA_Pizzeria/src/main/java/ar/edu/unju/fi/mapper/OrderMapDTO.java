package ar.edu.unju.fi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.entity.OrderEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomerMapDTO.class, OrderItemMapDTO.class})
public interface OrderMapDTO {

    @Mapping(source = "idOrder", target = "idOrder")
    @Mapping(source = "customer", target = "customer")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "method", target = "method")
    @Mapping(source = "additionalNotes", target = "additionalNotes")
    @Mapping(source = "items", target = "items") 
    OrderDTO toDto(OrderEntity orderEntity);
    
    @InheritInverseConfiguration
    OrderEntity toEntity(OrderDTO orderDTO);
    
    List<OrderDTO> listOrderToListOrderDTO(List<OrderEntity> listaOrder);
    
    List<OrderEntity> listOrderDTOToListOrder(List<OrderDTO> listaOrderDTO);
}
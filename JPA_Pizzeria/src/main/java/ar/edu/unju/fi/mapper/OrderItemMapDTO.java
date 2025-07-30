package ar.edu.unju.fi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.entity.OrderItemEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapDTO {

    @Mapping(source = "idOrder", target = "idOrder")
    @Mapping(source = "idItem", target = "idItem")
    @Mapping(source = "pizza", target = "pizza")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderItemDTO toDto(OrderItemEntity orderItemEntity);
    
    @InheritInverseConfiguration
    OrderItemEntity toEntity(OrderItemDTO orderItemDTO);
    
    List<OrderItemDTO> listOrderItemToListOrderItemDTO(List<OrderItemEntity> listaOrderItem);
    
    List<OrderItemEntity> listOrderItemDTOToListOrderItem(List<OrderItemDTO> listaOrderItemDTO);
}
package ar.edu.unju.fi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.entity.PizzaEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PizzaMapDTO {

    @Mapping(source = "idPizza", target = "idPizza")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "vegetarian", target = "vegetarian")
    @Mapping(source = "vegan", target = "vegan")
    @Mapping(source = "avaible", target = "avaible")
    PizzaDTO toDto(PizzaEntity pizzaEntity);
    
    @InheritInverseConfiguration
    PizzaEntity toEntity(PizzaDTO pizzaDTO);
    
    List<PizzaDTO> listPizzaToListPizzaDTO(List<PizzaEntity> listaPizza);
    
    List<PizzaEntity> listPizzaDTOToListPizza(List<PizzaDTO> listaPizzaDTO);
}

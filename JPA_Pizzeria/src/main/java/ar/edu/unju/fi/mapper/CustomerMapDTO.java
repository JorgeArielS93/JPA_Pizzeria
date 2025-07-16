package ar.edu.unju.fi.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.entity.CustomerEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapDTO {

    @Mapping(source = "idCustomer", target = "idCustomer")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address") // Asegúrate de que coincida con tu entidad, o considera cambiar a 'address'
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    CustomerDTO toDto(CustomerEntity customerEntity);
    
    @InheritInverseConfiguration
    CustomerEntity toEntity(CustomerDTO customerDTO);
    
    List<CustomerDTO> listCustomerToListCustomerDTO(List<CustomerEntity> listaCustomer);
    
    List<CustomerEntity> listCustomerDTOToListCustomer(List<CustomerDTO> listaCustomerDTO);
}

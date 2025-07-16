package ar.edu.unju.fi.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer idOrder;
    private String idCustomer;
    private LocalDate date;
    private Double total;
    private String method;
    private String additionalNotes;
    // Para las relaciones, en el DTO incluimos el DTO de la entidad relacionada
    private CustomerDTO customer; 
    private List<OrderItemDTO> items;
}

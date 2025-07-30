package ar.edu.unju.fi.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "La fecha no puede ser nula")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate date;

    @NotNull(message = "El total no puede ser nulo")
    @Positive(message = "El total debe ser mayor que cero")
    private Double total;

    @NotBlank(message = "El método de pago no puede estar vacío")
    private String method;

    @Size(max = 200, message = "Las notas adicionales no pueden superar los 200 caracteres")
    private String additionalNotes;

    @NotNull(message = "El cliente no puede ser nulo")
    private CustomerDTO customer; 

    @NotNull(message = "La lista de ítems no puede ser nula")
    @Size(min = 1, message = "Debe haber al menos un ítem en el pedido")
    private List<OrderItemDTO> items;

    private Integer selectedPizza;
    private Integer selectedQuantity;

    public Double getTotal() {
        if (items == null) return 0.0;
        return items.stream()
            .filter(i -> i.getQuantity() != null && i.getPrice() != null)
            .mapToDouble(i -> i.getQuantity() * i.getPrice())
            .sum();
    }
}
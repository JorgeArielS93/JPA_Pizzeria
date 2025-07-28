package ar.edu.unju.fi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Integer idCustomer; // autogenerado, oculto en el formulario

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 7, max = 15, message = "El DNI debe tener entre 7 y 15 caracteres")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    private String address;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "^\\+?\\d{7,15}$", message = "El número de teléfono debe ser válido")
    private String phoneNumber;
}
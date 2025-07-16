package ar.edu.unju.fi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String idCustomer;
    private String name;
    private String address; // Nota: Considera cambiar a 'address' para consistencia.
    private String email;
    private String phoneNumber;
}
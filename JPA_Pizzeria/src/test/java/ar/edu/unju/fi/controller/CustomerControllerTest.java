package ar.edu.unju.fi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ar.edu.unju.fi.dto.CustomerDTO;
import ar.edu.unju.fi.service.ICustomerService;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    private CustomerDTO mockCustomerDTO;
    private List<CustomerDTO> mockCustomerList;

    @BeforeEach
    void setUp() {
        // Mock Customer
        mockCustomerDTO = new CustomerDTO();
        mockCustomerDTO.setIdCustomer(1);
        mockCustomerDTO.setName("Cliente Test");
        mockCustomerDTO.setDni("12345678");
        mockCustomerDTO.setEmail("cliente@test.com");
        mockCustomerDTO.setAddress("Dirección Test");
        mockCustomerDTO.setPhoneNumber("3885123456");
        
        mockCustomerList = new ArrayList<>();
        mockCustomerList.add(mockCustomerDTO);

        // Configure mocks
        when(customerService.findAll()).thenReturn(mockCustomerList);
        when(customerService.findById(anyInt())).thenReturn(mockCustomerDTO);
        when(customerService.existsByDni(anyString())).thenReturn(false);
        when(customerService.existsByDni("12345678")).thenReturn(true); // DNI que ya existe
        
        // Para métodos void, usar doNothing().when()
        doNothing().when(customerService).save(any(CustomerDTO.class));
        doNothing().when(customerService).deleteById(anyInt());
    }

    @Test
    @DisplayName("GET /customers/lista - Mostrar lista de clientes")
    public void testGetListaClientes() throws Exception {
        mockMvc.perform(get("/customers/lista"))
            .andExpect(status().isOk())
            .andExpect(view().name("lists/listaClientes"))
            .andExpect(model().attributeExists("clientes"));
    }

    @Test
    @DisplayName("GET /customers/nuevo - Mostrar formulario de nuevo cliente")
    public void testGetNuevoFormulario() throws Exception {
        mockMvc.perform(get("/customers/nuevo"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-cliente"))
            .andExpect(model().attributeExists("cliente"))
            .andExpect(model().attribute("isEdit", false));
    }

    @Test
    @DisplayName("POST /customers/guardar - Guardar cliente con datos válidos")
    public void testGuardarClienteValido() throws Exception {
        mockMvc.perform(post("/customers/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Nuevo Cliente")
                .param("dni", "87654321")
                .param("email", "nuevo@test.com")
                .param("address", "Dirección Nueva")
                .param("phoneNumber", "3885654321"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/customers/lista"));
    }

    @Test
    @DisplayName("POST /customers/guardar - Error por DNI duplicado")
    public void testGuardarClienteDNIDuplicado() throws Exception {
        mockMvc.perform(post("/customers/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Cliente Duplicado")
                .param("dni", "12345678") // DNI que ya existe en el sistema
                .param("email", "duplicado@test.com")
                .param("address", "Dirección Duplicada")
                .param("phoneNumber", "3885000000"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-cliente"))
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("cliente", "dni"));
    }

    @Test
    @DisplayName("POST /customers/guardar - Error por falta de nombre")
    public void testGuardarClienteSinNombre() throws Exception {
        mockMvc.perform(post("/customers/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("dni", "99999999")
                .param("email", "sinnombre@test.com")
                .param("address", "Dirección Test")
                .param("phoneNumber", "3885123456"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-cliente"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("GET /customers/editar/1 - Mostrar formulario de edición")
    public void testGetEditarFormulario() throws Exception {
        mockMvc.perform(get("/customers/editar/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-cliente"))
            .andExpect(model().attributeExists("cliente"))
            .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @DisplayName("GET /customers/editar/999 - Cliente no encontrado")
    public void testGetEditarClienteNoEncontrado() throws Exception {
        // Configurar el mock para que devuelva null (cliente no encontrado)
        when(customerService.findById(999)).thenReturn(null);
        
        mockMvc.perform(get("/customers/editar/999"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/customers/lista"));
    }

    @Test
    @DisplayName("GET /customers/borrar/1 - Eliminar cliente")
    public void testBorrarCliente() throws Exception {
        mockMvc.perform(get("/customers/borrar/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/customers/lista"));
    }

    @Test
    @DisplayName("GET /customers/borrar/999 - Cliente no encontrado para eliminar")
    public void testBorrarClienteNoEncontrado() throws Exception {
        // Configurar el mock para que devuelva null (cliente no encontrado)
        when(customerService.findById(999)).thenReturn(null);
        
        mockMvc.perform(get("/customers/borrar/999"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/customers/lista"));
    }
}
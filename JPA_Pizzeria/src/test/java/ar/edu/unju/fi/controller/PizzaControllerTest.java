package ar.edu.unju.fi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.service.IPizzaService;

@SpringBootTest
@AutoConfigureMockMvc
public class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPizzaService pizzaService;

    private PizzaDTO mockPizzaDTO;
    private List<PizzaDTO> mockPizzaList;

    @BeforeEach
    void setUp() {
        // Mock Pizza
        mockPizzaDTO = new PizzaDTO();
        mockPizzaDTO.setIdPizza(1);
        mockPizzaDTO.setName("Pizza Test");
        mockPizzaDTO.setPrice(1500.0);
        mockPizzaDTO.setDescription("Pizza para test");
        mockPizzaDTO.setAvaible(true);
        
        mockPizzaList = new ArrayList<>();
        mockPizzaList.add(mockPizzaDTO);

        // Configure mocks
        when(pizzaService.findAll()).thenReturn(mockPizzaList);
        when(pizzaService.findById(anyInt())).thenReturn(mockPizzaDTO);
        
        // Para métodos void, usar doNothing().when() en lugar de when()
        doNothing().when(pizzaService).save(any(PizzaDTO.class));
        doNothing().when(pizzaService).deleteById(anyInt());
    }

    @Test
    @DisplayName("GET /pizzas/lista - Mostrar lista de pizzas")
    public void testGetListaPizzas() throws Exception {
        mockMvc.perform(get("/pizzas/lista"))
            .andExpect(status().isOk())
            .andExpect(view().name("lists/listaPizzas"))
            .andExpect(model().attributeExists("pizzas"));
    }

    @Test
    @DisplayName("GET /pizzas/nuevo - Mostrar formulario de nueva pizza")
    public void testGetNuevoFormulario() throws Exception {
        mockMvc.perform(get("/pizzas/nuevo"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-pizza"))
            .andExpect(model().attributeExists("pizza"));
    }

    @Test
    @DisplayName("POST /pizzas/guardar - Guardar pizza con datos válidos")
    public void testGuardarPizzaValida() throws Exception {
        mockMvc.perform(post("/pizzas/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Pizza Test")
                .param("price", "1500.0")
                .param("description", "Pizza para test")
                .param("avaible", "true"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/pizzas/lista"));
    }

    @Test
    @DisplayName("POST /pizzas/guardar - Error por falta de nombre")
    public void testGuardarPizzaSinNombre() throws Exception {
        mockMvc.perform(post("/pizzas/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("price", "1500.0")
                .param("description", "Pizza para test")
                .param("avaible", "true"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-pizza"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /pizzas/guardar - Error por precio inválido")
    public void testGuardarPizzaPrecioInvalido() throws Exception {
        mockMvc.perform(post("/pizzas/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Pizza Test")
                .param("price", "-1500.0") // Precio negativo
                .param("description", "Pizza para test")
                .param("avaible", "true"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-pizza"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("GET /pizzas/editar/1 - Mostrar formulario de edición")
    public void testGetEditarFormulario() throws Exception {
        mockMvc.perform(get("/pizzas/editar/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-pizza"))
            .andExpect(model().attributeExists("pizza"));
    }

    @Test
    @DisplayName("GET /pizzas/borrar/1 - Eliminar pizza")
    public void testBorrarPizza() throws Exception {
        mockMvc.perform(get("/pizzas/borrar/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/pizzas/lista"));
    }
}
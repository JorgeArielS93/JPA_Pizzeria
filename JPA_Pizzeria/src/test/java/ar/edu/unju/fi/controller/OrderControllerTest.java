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

import java.time.LocalDate;
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
import ar.edu.unju.fi.dto.OrderDTO;
import ar.edu.unju.fi.dto.OrderItemDTO;
import ar.edu.unju.fi.dto.PizzaDTO;
import ar.edu.unju.fi.service.ICustomerService;
import ar.edu.unju.fi.service.IOrderService;
import ar.edu.unju.fi.service.IPizzaService;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    @MockBean
    private ICustomerService customerService;

    @MockBean
    private IPizzaService pizzaService;

    private OrderDTO mockOrderDTO;
    private CustomerDTO mockCustomerDTO;
    private PizzaDTO mockPizzaDTO;
    private List<OrderDTO> mockOrderList;
    private List<CustomerDTO> mockCustomerList;
    private List<PizzaDTO> mockPizzaList;

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

        // Mock Pizza
        mockPizzaDTO = new PizzaDTO();
        mockPizzaDTO.setIdPizza(1);
        mockPizzaDTO.setName("Pizza Test");
        mockPizzaDTO.setPrice(1500.0);
        mockPizzaDTO.setDescription("Pizza para test");
        mockPizzaDTO.setAvaible(true);
        
        mockPizzaList = new ArrayList<>();
        mockPizzaList.add(mockPizzaDTO);

        // Mock Order Item
        OrderItemDTO mockOrderItemDTO = new OrderItemDTO();
        mockOrderItemDTO.setIdOrder(1);
        mockOrderItemDTO.setIdItem(1);
        mockOrderItemDTO.setIdPizza(1);
        mockOrderItemDTO.setPizza(mockPizzaDTO);
        mockOrderItemDTO.setQuantity(2);
        mockOrderItemDTO.setPrice(1500.0);

        List<OrderItemDTO> mockOrderItemList = new ArrayList<>();
        mockOrderItemList.add(mockOrderItemDTO);

        // Mock Order
        mockOrderDTO = new OrderDTO();
        mockOrderDTO.setIdOrder(1);
        mockOrderDTO.setCustomer(mockCustomerDTO);
        mockOrderDTO.setIdCustomer("1");
        mockOrderDTO.setDate(LocalDate.now());
        mockOrderDTO.setMethod("E");
        mockOrderDTO.setTotal(3000.0);
        mockOrderDTO.setItems(mockOrderItemList);
        
        mockOrderList = new ArrayList<>();
        mockOrderList.add(mockOrderDTO);

        // Configure mocks
        when(customerService.findAll()).thenReturn(mockCustomerList);
        when(customerService.findById(anyInt())).thenReturn(mockCustomerDTO);
        
        when(pizzaService.findAll()).thenReturn(mockPizzaList);
        when(pizzaService.findById(anyInt())).thenReturn(mockPizzaDTO);
        
        when(orderService.getAllOrders()).thenReturn(mockOrderList);
        when(orderService.getOrderById(anyInt())).thenReturn(mockOrderDTO);
        
        // Para métodos void, usar doNothing().when() en lugar de when()
        doNothing().when(orderService).saveOrder(any(OrderDTO.class));
        doNothing().when(orderService).deleteOrder(anyInt());
    }

    @Test
    @DisplayName("GET /orders/lista - Mostrar lista de pedidos")
    public void testGetListaPedidos() throws Exception {
        mockMvc.perform(get("/orders/lista"))
            .andExpect(status().isOk())
            .andExpect(view().name("lists/listaPedidos"))
            .andExpect(model().attributeExists("pedidos"));
    }

    @Test
    @DisplayName("GET /orders/nuevo - Mostrar formulario de nueva orden")
    public void testGetNuevoFormulario() throws Exception {
        mockMvc.perform(get("/orders/nuevo"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attributeExists("listaClientes"))
            .andExpect(model().attributeExists("pizzas"))
            .andExpect(model().attribute("isEdit", false));
    }

    @Test
    @DisplayName("POST /orders/nuevo - Añadir item a la orden")
    public void testAddItemToOrder() throws Exception {
        mockMvc.perform(post("/orders/nuevo")
                .param("addItem", "true")
                .param("selectedPizza", "1")
                .param("selectedQuantity", "2")
                .param("idCustomer", "")
                .param("method", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"));
    }

    @Test
    @DisplayName("GET /orders/editar/1 - Mostrar formulario de edición")
    public void testGetEditarFormulario() throws Exception {
        mockMvc.perform(get("/orders/editar/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"))
            .andExpect(model().attributeExists("order"))
            .andExpect(model().attributeExists("listaClientes"))
            .andExpect(model().attributeExists("pizzas"))
            .andExpect(model().attribute("isEdit", true));
    }

    @Test
    @DisplayName("POST /orders/guardar - Guardar orden con datos válidos")
    public void testGuardarOrderValida() throws Exception {
        mockMvc.perform(post("/orders/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idCustomer", "1")
                .param("method", "E")
                .flashAttr("order", mockOrderDTO))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/orders/lista"));
    }

    @Test
    @DisplayName("POST /orders/guardar - Error por falta de cliente")
    public void testGuardarOrderSinCliente() throws Exception {
        OrderDTO invalidOrder = new OrderDTO();
        invalidOrder.setMethod("E");
        
        // Crear al menos un item para el pedido
        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item = new OrderItemDTO();
        item.setPizza(mockPizzaDTO);
        item.setIdPizza(1);
        item.setQuantity(1);
        item.setPrice(1500.0);
        items.add(item);
        invalidOrder.setItems(items);
        
        mockMvc.perform(post("/orders/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", invalidOrder))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /orders/guardar - Error por falta de método de pago")
    public void testGuardarOrderSinMetodoPago() throws Exception {
        OrderDTO invalidOrder = new OrderDTO();
        invalidOrder.setCustomer(mockCustomerDTO);
        invalidOrder.setIdCustomer("1");
        
        // Crear al menos un item para el pedido
        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item = new OrderItemDTO();
        item.setPizza(mockPizzaDTO);
        item.setIdPizza(1);
        item.setQuantity(1);
        item.setPrice(1500.0);
        items.add(item);
        invalidOrder.setItems(items);
        
        mockMvc.perform(post("/orders/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", invalidOrder))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /orders/guardar - Error por falta de items")
    public void testGuardarOrderSinItems() throws Exception {
        OrderDTO invalidOrder = new OrderDTO();
        invalidOrder.setCustomer(mockCustomerDTO);
        invalidOrder.setIdCustomer("1");
        invalidOrder.setMethod("E");
        invalidOrder.setItems(new ArrayList<>());
        
        mockMvc.perform(post("/orders/guardar")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", invalidOrder))
            .andExpect(status().isOk())
            .andExpect(view().name("forms/form-order"))
            .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("GET /orders/borrar/1 - Eliminar orden")
    public void testBorrarOrder() throws Exception {
        mockMvc.perform(get("/orders/borrar/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/orders/lista"));
    }
}
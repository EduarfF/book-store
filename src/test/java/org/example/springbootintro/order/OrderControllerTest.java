package org.example.springbootintro.order;

import static org.example.springbootintro.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.springbootintro.controller.OrderController;
import org.example.springbootintro.dto.order.CreateOrderRequestDto;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.dto.orderitem.UpdateOrderItemDto;
import org.example.springbootintro.model.Order;
import org.example.springbootintro.model.User;
import org.example.springbootintro.service.order.OrderService;
import org.example.springbootintro.service.orderitem.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test save order")
    public void saveOrderTest() throws Exception {
        // Given
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setShippingAddress("123 Main St, City, Country");

        OrderDto savedOrderDto = new OrderDto();
        savedOrderDto.setId(1L);

        given(orderService.save(any(User.class), any(CreateOrderRequestDto.class)))
                .willReturn(savedOrderDto);

        // When
        var resultActions = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto)));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(savedOrderDto.getId()));

        // Verify
        verify(orderService).save(any(User.class), any(CreateOrderRequestDto.class));
        verifyNoMoreInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update order status")
    public void updateOrderStatusTest() throws Exception {
        // Given
        Long orderId = 1L;
        UpdateOrderItemDto updateDto = new UpdateOrderItemDto();
        updateDto.setStatus(Order.Status.PENDING);
        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setId(orderId);
        updatedOrderDto.setStatus("SHIPPED");

        given(orderService.update(anyLong(), any(UpdateOrderItemDto.class)))
                .willReturn(updatedOrderDto);

        // When
        var resultActions = mockMvc.perform(patch("/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDto)));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("SHIPPED"));

        // Verify
        verify(orderService).update(anyLong(), any(UpdateOrderItemDto.class));
        verifyNoMoreInteractions(orderService);
    }
}

package org.example.springbootintro.order;

import static org.example.springbootintro.service.order.OrderServiceImpl.getTotal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.springbootintro.dto.order.CreateOrderRequestDto;
import org.example.springbootintro.dto.order.OrderDto;
import org.example.springbootintro.dto.orderitem.UpdateOrderItemDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.order.OrderMapper;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.model.CartItem;
import org.example.springbootintro.model.Order;
import org.example.springbootintro.model.OrderItem;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.cartitem.CartItemRepository;
import org.example.springbootintro.repository.order.OrderRepository;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.example.springbootintro.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private ShoppingCart shoppingCart;
    private Set<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        shoppingCart = new ShoppingCart();
        cartItems = new HashSet<>();
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setPrice(BigDecimal.valueOf(10.0));
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setPrice(BigDecimal.valueOf(20.0));
        cartItems.add(new CartItem(book1, 2));
        cartItems.add(new CartItem(book2, 1));
        shoppingCart.setCartItems(cartItems);
    }

    @Test
    @DisplayName("Test getAll method")
    void getAll_ReturnsListOfOrderDtos() {
        // Given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(user, Order.Status.PENDING, LocalDateTime.now(), "Address 1"));
        orders.add(new Order(user, Order.Status.COMPLETED, LocalDateTime.now(), "Address 2"));
        Page<Order> page = new PageImpl<>(orders);

        // When
        when(orderRepository.findAllByUserId(userId, pageable)).thenReturn(orders);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        List<OrderDto> foundDtos = orderService.getAll(userId, pageable);

        // Then
        assertEquals(orders.size(), foundDtos.size());
        verify(orderMapper, times(orders.size())).toDto(any(Order.class));
    }

    @Test
    @DisplayName("Test save method with invalid user")
    void save_InvalidUser_ThrowsEntityNotFoundException() {
        // Given
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();

        // When
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class, () -> orderService.save(user, requestDto));
    }

    @Test
    @DisplayName("Test update method")
    void update_ExistingOrderId_ReturnsUpdatedOrderDto() {
        // Given
        Long orderId = 1L;
        UpdateOrderItemDto requestDto = new UpdateOrderItemDto();
        requestDto.setStatus(Order.Status.COMPLETED);

        Order order = new Order(user, Order.Status.PENDING, LocalDateTime.now(), "Address");
        order.setId(orderId);

        // When
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto updatedDto = orderService.update(orderId, requestDto);

        // Then
        assertNotNull(updatedDto);
        assertEquals(requestDto.getStatus(), order.getStatus());
    }

    @Test
    @DisplayName("Test update method with non-existing order")
    void update_NonExistingOrderId_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistingOrderId = 999L;
        UpdateOrderItemDto requestDto = new UpdateOrderItemDto();
        requestDto.setStatus(Order.Status.COMPLETED);

        // When
        when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> orderService.update(nonExistingOrderId, requestDto));
    }

    @Test
    @DisplayName("Test getOrderItems with non-empty shopping cart")
    public void getOrderItems_ShoppingCartNotEmpty_ReturnsCorrectOrderItems() {
        // Given

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setPrice(BigDecimal.valueOf(10.0));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setPrice(BigDecimal.valueOf(20.0));

        CartItem cartItem1 = new CartItem(book1, 2);
        CartItem cartItem2 = new CartItem(book2, 1);

        shoppingCart.setCartItems(Set.of(cartItem1, cartItem2));

        Order order = new Order();

        // When
        Set<OrderItem> result = orderService.getOrderItems(shoppingCart, order);

        // Then
        assertEquals(2, result.size());

        for (OrderItem orderItem : result) {
            assertEquals(order, orderItem.getOrder());
            assertTrue(Set.of(book1, book2).contains(orderItem.getBook()));
            assertTrue(orderItem.getQuantity() == 2 || orderItem.getQuantity() == 1);
            assertTrue(orderItem.getPrice().equals(book1.getPrice())
                    || orderItem.getPrice().equals(book2.getPrice()));
        }
    }

    @Test
    @DisplayName("Test getOrderItems with empty shopping cart")
    public void getOrderItems_ShoppingCartEmpty_ReturnsEmptySet() {
        // Given
        Order order = new Order();

        shoppingCart.setCartItems(new HashSet<>());

        // When
        Set<OrderItem> result = orderService.getOrderItems(shoppingCart, order);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test getTotal with non-empty order items")
    public void getTotal_OrderItemsNotEmpty_CalculatesCorrectTotal() {
        // Given
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(new OrderItem(null, null, 2, new BigDecimal("10.00")));
        orderItems.add(new OrderItem(null, null, 1, new BigDecimal("5.50")));

        // When
        BigDecimal result = getTotal(orderItems);

        // Then
        assertEquals(new BigDecimal("25.50"), result);
    }

    @Test
    @DisplayName("Test getTotal with empty order items")
    public void getTotal_OrderItemsEmpty_ReturnsZero() {
        // Given
        Set<OrderItem> orderItems = new HashSet<>();

        // When
        BigDecimal result = getTotal(orderItems);

        // Then
        assertEquals(BigDecimal.ZERO, result);
    }
}

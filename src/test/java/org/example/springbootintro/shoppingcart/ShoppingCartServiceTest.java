package org.example.springbootintro.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.cartitem.CartItemMapper;
import org.example.springbootintro.mapper.shoppingcart.ShoppingCartMapper;
import org.example.springbootintro.model.CartItem;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.cartitem.CartItemRepository;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.example.springbootintro.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test getById method with existing user")
    void getById_ExistingUserId_ReturnsShoppingCartDto() {
        // Given
        Long userId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        given(shoppingCartRepository.findByUserId(userId)).willReturn(Optional.of(shoppingCart));

        Set<CartItem> cartItems = Stream.of(new CartItem(),
                new CartItem()).collect(Collectors.toSet());
        given(cartItemRepository.findByShoppingCartId(shoppingCart.getId())).willReturn(cartItems);

        given(shoppingCartMapper.toDto(shoppingCart)).willReturn(new ShoppingCartDto());

        // When
        ShoppingCartDto result = shoppingCartService.getById(userId);

        // Then
        assertEquals(1, result.getCartItems().size());
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Test getById method with non-existing user")
    void getById_NonExistingUserId_ThrowsEntityNotFoundException() {
        // Given
        Long userId = 2L;
        given(shoppingCartRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When
        Executable whenAction = () -> shoppingCartService.getById(userId);

        // Then
        assertThrows(EntityNotFoundException.class, whenAction);
    }

    @Test
    @DisplayName("Test createShoppingCartForUser method")
    void createShoppingCartForUser_ValidUser_ReturnsShoppingCart() {
        // Given
        User user = new User();

        // When
        ShoppingCart result = shoppingCartService.createShoppingCartForUser(user);

        // Then
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Test getShoppingCartForUser method with existing user")
    void getShoppingCartForUser_ExistingUserId_ReturnsShoppingCart() {
        // Given
        Long userId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        given(shoppingCartRepository.findByUserId(userId)).willReturn(Optional.of(shoppingCart));

        // When
        ShoppingCart result = shoppingCartService.getShoppingCartForUser(userId);

        // Then
        assertEquals(shoppingCart.getId(), result.getId());
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Test getShoppingCartForUser method with non-existing user")
    void getShoppingCartForUser_NonExistingUserId_ThrowsEntityNotFoundException() {
        // Given
        Long userId = 1L;
        given(shoppingCartRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When
        Executable whenAction = () -> shoppingCartService.getShoppingCartForUser(userId);

        // Then
        assertThrows(EntityNotFoundException.class, whenAction);
    }
}

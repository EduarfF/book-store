package org.example.springbootintro.shoppingcart;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.springbootintro.TestUtils;
import org.example.springbootintro.controller.ShoppingCartController;
import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.cartitem.UpdateCartItemDto;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.service.cartitem.CartItemService;
import org.example.springbootintro.service.shoppingcart.ShoppingCartService;
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
public class ShoppingCartControllerTest {
    private static final String BASE_URL = "/cart";
    private static final String CART_ITEM_ID_URL = "/cart-items/{cartItemId}";
    private static final String CART_ITEMS = "/cart-items";

    private MockMvc mockMvc;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("Test get shopping cart")
    public void getShoppingCartTest() throws Exception {
        // Given
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);

        given(shoppingCartService.getById(null)).willReturn(shoppingCartDto);

        // When
        var resultActions = mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Verify
        verify(shoppingCartService).getById(null);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("Test update quantity of books in cart")
    public void updateQuantityTest() throws Exception {
        // Given
        Long cartItemId = 1L;
        UpdateCartItemDto updateDto = new UpdateCartItemDto();
        updateDto.setQuantity(5);

        CartItemDto responseDto = new CartItemDto();
        responseDto.setId(cartItemId);
        responseDto.setQuantity(5);

        given(cartItemService.updateCartItem(updateDto, null, cartItemId)).willReturn(responseDto);

        // When
        var resultActions = mockMvc.perform(put(BASE_URL + CART_ITEM_ID_URL, cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(updateDto)));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartItemId))
                .andExpect(jsonPath("$.quantity").value(5));

        // Verify
        verify(cartItemService).updateCartItem(updateDto, null, cartItemId);
        verifyNoMoreInteractions(cartItemService);
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    @DisplayName("Test delete all books from shopping cart")
    public void deleteAllBooksFromShoppingCartTest() throws Exception {
        // When
        var resultActions = mockMvc.perform(delete(BASE_URL + CART_ITEMS)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isNoContent());

        // Verify
        verify(cartItemService).deleteCartItemsAll(null);
        verifyNoMoreInteractions(cartItemService);
    }
}

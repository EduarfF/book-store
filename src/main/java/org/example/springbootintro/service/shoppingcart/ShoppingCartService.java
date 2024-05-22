package org.example.springbootintro.service.shoppingcart;

import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import org.example.springbootintro.dto.cartitem.UpdateCartItemDto;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCartForUser(User user);

    ShoppingCartDto getById(Long id);

    CartItemDto addCartItem(CreateCartItemRequestDto requestDto, User user);

    CartItemDto updateCartItem(UpdateCartItemDto updateDto, Long userId, Long cartItemId);

    void deleteCartItemById(Long userId, Long cartItemId);

    void deleteCartItemsAll(Long userId);
}

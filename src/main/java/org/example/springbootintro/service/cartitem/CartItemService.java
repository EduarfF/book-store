package org.example.springbootintro.service.cartitem;

import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import org.example.springbootintro.dto.cartitem.UpdateCartItemDto;
import org.example.springbootintro.model.User;

public interface CartItemService {
    CartItemDto addCartItem(CreateCartItemRequestDto requestDto, User user);

    CartItemDto updateCartItem(UpdateCartItemDto updateDto, Long userId, Long cartItemId);

    void deleteCartItemById(Long userId, Long cartItemId);

    void deleteCartItemsAll(Long userId);
}

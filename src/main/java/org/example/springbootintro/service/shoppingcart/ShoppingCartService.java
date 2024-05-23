package org.example.springbootintro.service.shoppingcart;

import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCartForUser(User user);

    ShoppingCartDto getById(Long id);

    ShoppingCart getShoppingCartForUser(Long userId);
}

package org.example.springbootintro.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import org.example.springbootintro.dto.cartitem.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}

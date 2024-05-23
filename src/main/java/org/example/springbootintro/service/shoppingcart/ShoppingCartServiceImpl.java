package org.example.springbootintro.service.shoppingcart;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.cartitem.CartItemMapper;
import org.example.springbootintro.mapper.shoppingcart.ShoppingCartMapper;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.cartitem.CartItemRepository;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getById(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart with id: " + userId)
                );
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        Set<CartItemDto> cartItemDtos = cartItemRepository
                .findByShoppingCartId(shoppingCart.getId())
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        shoppingCartDto.setCartItems(cartItemDtos);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCart createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getShoppingCartForUser(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart by id: " + userId)
                );
    }
}

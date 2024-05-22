package org.example.springbootintro.service.cartitem;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import org.example.springbootintro.dto.cartitem.UpdateCartItemDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.cartitem.CartItemMapper;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.model.CartItem;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.book.BookRepository;
import org.example.springbootintro.repository.cartitem.CartItemRepository;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public CartItemDto addCartItem(CreateCartItemRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = getShoppingCartForUser(user.getId());
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can not find book by id: " + requestDto.getBookId())
                );
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateCartItem(UpdateCartItemDto updateDto, Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        CartItem cartItem = getCartItem(cartItemId, shoppingCart.getId());
        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItemById(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        CartItem cartItem = getCartItem(cartItemId, shoppingCart.getId());
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteCartItemsAll(Long userId) {
        ShoppingCart shoppingCart = getShoppingCartForUser(userId);
        Set<CartItem> cartItem = cartItemRepository.findByShoppingCartId(shoppingCart.getId());
        cartItemRepository.deleteAll(cartItem);
    }

    private CartItem getCartItem(Long cartItemId, Long shoppingCartId) {
        return cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCartId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can not find cart item by id:" + cartItemId)
                );
    }

    private ShoppingCart getShoppingCartForUser(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find shopping cart by id: " + userId)
                );
    }
}

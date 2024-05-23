package org.example.springbootintro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.cartitem.CartItemDto;
import org.example.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import org.example.springbootintro.dto.cartitem.UpdateCartItemDto;
import org.example.springbootintro.dto.shoppingcart.ShoppingCartDto;
import org.example.springbootintro.model.User;
import org.example.springbootintro.service.cartitem.CartItemService;
import org.example.springbootintro.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book to shopping cart", description = "Add book to shopping cart")
    public CartItemDto addBookToShoppingCart(
            @RequestBody @Valid CreateCartItemRequestDto addCartItemDto,
            @AuthenticationPrincipal User user
    ) {
        return cartItemService.addCartItem(addCartItemDto, user);
    }

    @GetMapping
    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get shopping cart", description = "Get shopping cart")
    public ShoppingCartDto getShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getById(user.getId());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update quantity of books", description = "Update quantity of books")
    public CartItemDto updateQuantity(
            @RequestBody @Valid UpdateCartItemDto updateDto,
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId
    ) {
        return cartItemService.updateCartItem(updateDto, user.getId(), cartItemId);
    }

    @DeleteMapping("/cart-items/{cartItemsId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete book from shopping cart",
            description = "Delete book from shopping cart"
    )
    public void delete(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal User user
    ) {
        cartItemService.deleteCartItemById(user.getId(), cartItemId);
    }

    @DeleteMapping("/cart-items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete all books from shopping cart",
            description = "Delete all books from shopping cart"
    )
    public void deleteAll(@AuthenticationPrincipal User user) {
        cartItemService.deleteCartItemsAll(user.getId());
    }
}

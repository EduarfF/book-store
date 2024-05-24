package org.example.springbootintro.repository.cartitem;

import java.util.Optional;
import java.util.Set;
import org.example.springbootintro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findByShoppingCartId(Long id);

    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);

    void deleteAllByShoppingCartId(Long shoppingCartId);
}

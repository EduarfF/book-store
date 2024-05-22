package org.example.springbootintro.repository.shoppingcart;

import java.util.Optional;
import org.example.springbootintro.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);
}

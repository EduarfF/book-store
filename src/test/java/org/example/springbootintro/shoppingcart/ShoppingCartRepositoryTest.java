package org.example.springbootintro.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.example.springbootintro.model.ShoppingCart;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private EntityManager entityManager;

    private Long existingUserId;

    private Long deletedCartUserId;

    @BeforeEach
    void setUp() {
        existingUserId = createUserWithCart(
                "user1@example.com",
                "password123",
                "John",
                "Doe",
                false
        );
        deletedCartUserId = createUserWithCart(
                "user2@example.com",
                "password123",
                "Jane",
                "Doe",
                true
        );
    }

    private Long createUserWithCart(
            String email, String password, String firstName, String lastName, boolean isDeleted
    ) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        entityManager.persist(user);
        entityManager.flush();

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setDeleted(isDeleted);
        entityManager.persist(cart);
        entityManager.flush();

        return user.getId();
    }

    @Test
    @DisplayName("Test finding shopping cart by existing user ID")
    void findByUserId_ExistingUserId_ReturnsOptionalOfShoppingCart() {
        // Given
        Long userId = existingUserId;

        // When
        Optional<ShoppingCart> foundCart = shoppingCartRepository.findByUserId(userId);

        // Then
        assertTrue(foundCart.isPresent());
        assertEquals(userId, foundCart.get().getUser().getId());
        assertFalse(foundCart.get().isDeleted());
    }

    @Test
    @DisplayName("Test finding shopping cart by non-existing user ID")
    void findByUserId_NonExistingUserId_ReturnsEmptyOptional() {
        // Given
        Long nonExistingUserId = 99L;

        // When
        Optional<ShoppingCart> foundCart = shoppingCartRepository.findByUserId(nonExistingUserId);

        // Then
        assertTrue(foundCart.isEmpty());
    }

    @Test
    @DisplayName("Test finding shopping cart by user ID when cart is marked as deleted")
    void findByUserId_DeletedCart_ReturnsEmptyOptional() {
        // Given
        Long userId = deletedCartUserId;

        // When
        Optional<ShoppingCart> foundCart = shoppingCartRepository.findByUserId(userId);

        // Then
        assertTrue(foundCart.isEmpty());
    }
}

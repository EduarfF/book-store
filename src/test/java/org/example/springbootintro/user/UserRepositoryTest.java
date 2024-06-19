package org.example.springbootintro.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.example.springbootintro.model.User;
import org.example.springbootintro.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test finding user by email")
    void findByEmail_ExistingEmail_ReturnsUser() {
        // Given
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "testPassword";

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);

        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
        assertEquals(firstName, foundUser.get().getFirstName());
        assertEquals(password, foundUser.get().getPassword());
    }

    @Test
    @DisplayName("Test finding user by non-existing email")
    void findByEmail_NonExistingEmail_ReturnsEmptyOptional() {
        // Given
        String nonExistingEmail = "nonexistent@example.com";

        // When
        Optional<User> foundUser = userRepository.findByEmail(nonExistingEmail);

        // Then
        assertTrue(foundUser.isEmpty());
    }
}

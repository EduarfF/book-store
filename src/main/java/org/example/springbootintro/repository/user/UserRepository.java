package org.example.springbootintro.repository.user;

import java.util.Optional;
import org.example.springbootintro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}


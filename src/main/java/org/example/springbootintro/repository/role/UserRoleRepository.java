package org.example.springbootintro.repository.role;

import java.util.Optional;
import org.example.springbootintro.model.Role;
import org.example.springbootintro.model.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}

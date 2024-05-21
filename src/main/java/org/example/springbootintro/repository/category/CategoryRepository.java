package org.example.springbootintro.repository.category;

import java.util.Set;
import org.example.springbootintro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.id FROM Category c")
    Set<Long> findAllIds();
}

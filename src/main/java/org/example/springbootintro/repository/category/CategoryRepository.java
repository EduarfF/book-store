package org.example.springbootintro.repository.category;

import org.example.springbootintro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

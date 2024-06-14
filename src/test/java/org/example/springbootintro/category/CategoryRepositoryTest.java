package org.example.springbootintro.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.example.springbootintro.model.Category;
import org.example.springbootintro.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Test finding all category IDs")
    void findAllIds_ReturnsSetOfCategoryIds() {
        // Given
        Set<Long> expectedIds = new HashSet<>();
        expectedIds.add(1L);
        expectedIds.add(2L);
        expectedIds.add(3L);

        // When
        Set<Long> actualIds = categoryRepository.findAllIds();

        // Then
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @DisplayName("Test finding a category by existing ID")
    void findById_ExistingId_ReturnsOptionalOfCategory() {
        // Given
        Long categoryId = 1L;

        // When
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);

        // Then
        assertTrue(foundCategory.isPresent());
        assertEquals(categoryId, foundCategory.get().getId());
    }

    @Test
    @DisplayName("Test finding a category by non-existing ID")
    void findById_NonExistingId_ReturnsEmptyOptional() {
        // Given
        Long nonExistingCategoryId = 100L;

        // When
        Optional<Category> foundCategory = categoryRepository.findById(nonExistingCategoryId);

        // Then
        assertTrue(foundCategory.isEmpty());
    }

    @Test
    @DisplayName("Test finding all categories as a page")
    void findAll_ReturnsPageOfCategories() {
        // Given
        Pageable pageable = Pageable.unpaged();

        // When
        Page<Category> foundPage = categoryRepository.findAll(pageable);

        // Then
        assertEquals(3, foundPage.getContent().size());
    }
}

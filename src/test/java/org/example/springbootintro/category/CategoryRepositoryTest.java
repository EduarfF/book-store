package org.example.springbootintro.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.Set;
import org.example.springbootintro.repository.category.CategoryRepository;
import org.example.springbootintro.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Test findAllIds method")
    void findAllIds_ReturnsSetOfCategoryIds() {
        // Given
        Set<Long> expectedIds = new HashSet<>();
        expectedIds.add(1L);
        expectedIds.add(2L);

        given(categoryRepository.findAllIds()).willReturn(expectedIds);

        // When
        Set<Long> actualIds = categoryService.getAllCategoryIds();

        // Then
        assertEquals(expectedIds, actualIds);
    }
}

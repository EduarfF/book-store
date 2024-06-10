package org.example.springbootintro.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import org.example.springbootintro.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void findAllIds_ReturnsSetOfCategoryIds() {
        Set<Long> expectedIds = new HashSet<>();
        expectedIds.add(1L);
        expectedIds.add(2L);

        when(categoryRepository.findAllIds()).thenReturn(expectedIds);

        // When
        Set<Long> actualIds = categoryRepository.findAllIds();

        // Then
        assertEquals(expectedIds, actualIds);
    }
}

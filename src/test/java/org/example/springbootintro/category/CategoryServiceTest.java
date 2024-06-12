package org.example.springbootintro.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.springbootintro.dto.category.CategoryDto;
import org.example.springbootintro.dto.category.CreateCategoryRequestDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.category.CategoryMapper;
import org.example.springbootintro.model.Category;
import org.example.springbootintro.repository.category.CategoryRepository;
import org.example.springbootintro.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Test findAll method")
    void findAll_ReturnsListOfCategoryDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());
        Page<Category> page = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(any())).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return new CategoryDto();
        });

        // When
        List<CategoryDto> foundDtos = categoryService.findAll(pageable);

        // Then
        assertEquals(categories.size(), foundDtos.size());
    }

    @Test
    @DisplayName("Test findById method with existing id")
    void findById_ExistingId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);

        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
        given(categoryMapper.toDto(category)).willReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.findById(categoryId);

        // Then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Test findById method with non-existing id")
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;

        given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(categoryId));
    }

    @Test
    @DisplayName("Test save method with valid CreateCategoryRequestDto")
    void save_ValidCreateCategoryRequestDto_ReturnsCategoryDtoWithId() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Test Category");
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto();

        given(categoryMapper.toModel(requestDto)).willReturn(category);
        given(categoryRepository.save(category)).willReturn(category);
        given(categoryMapper.toDto(category)).willReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.save(requestDto);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Test updateById method with non-existing id")
    void updateById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Updated Category");

        given(categoryRepository.existsById(categoryId)).willReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService
                .updateById(categoryId, requestDto));
    }

    @Test
    @DisplayName("Test deleteById method with existing id")
    void deleteById_ExistingId_DeletesCategory() {
        // Given
        Long categoryId = 1L;

        given(categoryRepository.existsById(categoryId)).willReturn(true);

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Test deleteById method with non-existing id")
    void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;

        given(categoryRepository.existsById(categoryId)).willReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(categoryId));
    }

    @Test
    @DisplayName("Test getAllCategoryIds method")
    void getAllCategoryIds_ReturnsSetOfCategoryIds() {
        // Given
        Set<Long> expectedIds = Set.of(1L, 2L);

        given(categoryRepository.findAllIds()).willReturn(expectedIds);

        // When
        Set<Long> actualIds = categoryService.getAllCategoryIds();

        // Then
        assertEquals(expectedIds, actualIds);
    }
}

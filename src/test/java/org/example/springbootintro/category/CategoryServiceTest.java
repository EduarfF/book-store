package org.example.springbootintro.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void findAll_ReturnsListOfCategoryDtos() {
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
    void findById_ExistingId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.findById(categoryId);

        // Then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(categoryId));
    }

    @Test
    void save_ValidCreateCategoryRequestDto_ReturnsCategoryDtoWithId() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Test Category");
        Category category = new Category();

        CategoryDto expectedDto = new CategoryDto();

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.save(requestDto);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateById_NonExistingId_ThrowsEntityNotFoundException() {
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Updated Category");

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService
                .updateById(categoryId, requestDto));
    }

    @Test
    void deleteById_ExistingId_DeletesCategory() {
        Long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteById(categoryId));
    }

    @Test
    void getAllCategoryIds_ReturnsSetOfCategoryIds() {
        Set<Long> expectedIds = Set.of(1L, 2L);

        when(categoryRepository.findAllIds()).thenReturn(expectedIds);

        // When
        Set<Long> actualIds = categoryService.getAllCategoryIds();

        // Then
        assertEquals(expectedIds, actualIds);
    }
}

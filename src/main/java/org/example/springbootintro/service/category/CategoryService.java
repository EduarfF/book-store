package org.example.springbootintro.service.category;

import java.util.List;
import java.util.Set;
import org.example.springbootintro.dto.category.CategoryDto;
import org.example.springbootintro.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto updateById(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);

    Set<Long> getAllCategoryIds();
}

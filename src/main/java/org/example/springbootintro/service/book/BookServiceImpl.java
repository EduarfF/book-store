package org.example.springbootintro.service.book;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.BookDtoWithoutCategoryIds;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.exception.InvalidCategoryIdsException;
import org.example.springbootintro.mapper.book.BookMapper;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.example.springbootintro.repository.book.BookSpecificationBuilder;
import org.example.springbootintro.service.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryService categoryService;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        checkAllIdsExist(requestDto.getCategoryIds());
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAllBooks(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        checkIfBookExists(id);
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        checkIfBookExists(id);
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(Pageable pageable, BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        Page<Book> page = bookRepository.findAll(bookSpecification, pageable);

        return page.getContent().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoriesId(categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private void checkIfBookExists(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book by id: " + id);
        }
    }

    private void checkAllIdsExist(Set<Long> categoryIds) {
        Set<Long> allCategoryIds = categoryService.getAllCategoryIds();
        Set<Long> notExistingCategoryIds = new HashSet<>();
        categoryIds.forEach(id -> {
            if (!allCategoryIds.contains(id)) {
                notExistingCategoryIds.add(id);
            }
        });
        if (!notExistingCategoryIds.isEmpty()) {
            throw new InvalidCategoryIdsException("Not existing category ids: "
                    + notExistingCategoryIds);
        }
    }
}

package org.example.springbootintro.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.BookDtoWithoutCategoryIds;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.book.BookMapper;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.example.springbootintro.repository.book.BookSpecificationBuilder;
import org.example.springbootintro.service.book.BookServiceImpl;
import org.example.springbootintro.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save_ValidCreateBookRequestDto_ReturnsBookDtoWithId() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Test Book",
                "Test Author",
                "123-123-0001",
                BigDecimal.valueOf(100),
                null,
                null,
                Set.of(1L, 2L)
        );

        Book book = new Book();

        BookDto expectedDto = new BookDto();

        when(categoryService.getAllCategoryIds()).thenReturn(Set.of(1L, 2L));
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        // When
        BookDto actualDto = bookService.save(requestDto);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(bookRepository).save(book);
    }

    @Test
    void findById_ExistingId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        // When
        BookDto actualDto = bookService.findById(bookId);

        // Then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.findById(bookId));
    }

    @Test
    void deleteById_ExistingId_DeletesBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteById(bookId);

        // Then
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteById_NonExistingId_ThrowsEntityNotFoundException() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(bookId));
    }

    @Test
    void search_ByDefault_ReturnsListOfBookDtos() {
        String[] titles = {"title1", "title2"};
        String[] authors = {"author1", "author2"};
        BookSearchParametersDto params = new BookSearchParametersDto(titles, authors);

        Specification<Book> bookSpecification = mock(Specification.class);
        when(bookSpecificationBuilder.build(params)).thenReturn(bookSpecification);

        List<Book> books = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        books.add(new Book());
        books.add(new Book());
        Page<Book> page = new PageImpl<>(books);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(page);

        when(bookMapper.toDto(any())).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return new BookDto();
        });

        // When
        List<BookDto> foundDtos = bookService.search(pageable, params);

        // Then
        assertEquals(books.size(), foundDtos.size());
    }

    @Test
    void findAllByCategoryId_ByDefault_ReturnsListOfBookDtosWithoutCategoryIds() {
        Long categoryId = 1L;

        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(books);

        List<BookDtoWithoutCategoryIds> expectedDtos = books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());

        // When
        List<BookDtoWithoutCategoryIds> foundDtos = bookService.findAllByCategoryId(categoryId);

        // Then
        assertEquals(expectedDtos.size(), foundDtos.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), foundDtos.get(i));
        }
    }

    @Test
    void findAll_ByDefault_ReturnsListOfBookDtos() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        when(bookRepository.findAllBooks(pageable)).thenReturn(books);

        // When
        List<BookDto> foundDtos = bookService.findAll(pageable);

        // Then
        assertEquals(books.size(), foundDtos.size());
    }

    @Test
    void updateById_NonExistingId_ThrowsEntityNotFoundException() {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Updated Book",
                "Updated Author",
                "123-123-0002",
                BigDecimal.valueOf(200),
                null,
                null,
                Set.of(1L, 2L)
        );

        // When, Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(bookId, requestDto));
    }
}

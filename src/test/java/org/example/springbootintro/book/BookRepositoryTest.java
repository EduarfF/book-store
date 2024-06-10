package org.example.springbootintro.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {
    @Mock
    private BookRepository bookRepository;

    @Test
    void findAllByCategoriesId_ReturnsListOfBooks() {
        Long categoryId = 1L;

        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(books);

        // When
        List<Book> foundBooks = bookRepository.findAllByCategoriesId(categoryId);

        // Then
        assertEquals(2, foundBooks.size());
    }

    @Test
    void findAllBooks_ReturnsListOfBooks() {
        Pageable pageable = Pageable.unpaged();

        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAllBooks(pageable)).thenReturn(books);

        // When
        List<Book> foundBooks = bookRepository.findAllBooks(pageable);

        // Then
        assertEquals(2, foundBooks.size());
    }

    @Test
    void findById_ExistingId_ReturnsOptionalOfBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        Optional<Book> foundBook = bookRepository.findById(bookId);

        // Then
        assertTrue(foundBook.isPresent());
        assertEquals(bookId, foundBook.get().getId());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Optional<Book> foundBook = bookRepository.findById(bookId);

        // Then
        assertTrue(foundBook.isEmpty());
    }

    @Test
    void findAll_ReturnsPageOfBooks() {
        Pageable pageable = Pageable.unpaged();

        List<Book> books = List.of(new Book(), new Book());
        Page<Book> page = new PageImpl<>(books);
        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        // When
        Page<Book> foundPage = bookRepository.findAll(mock(Specification.class), pageable);

        // Then
        assertEquals(2, foundPage.getContent().size());
    }
}

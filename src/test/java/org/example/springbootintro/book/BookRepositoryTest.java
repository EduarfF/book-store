package org.example.springbootintro.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Test finding all books by category ID")
    void findAllByCategoriesId_ReturnsListOfBooks() {
        // Given
        Long categoryId = 1L;

        // When
        List<Book> foundBooks = bookRepository.findAllByCategoriesId(categoryId);

        // Then
        assertEquals(2, foundBooks.size());
    }

    @Test
    @DisplayName("Test finding all books")
    void findAllBooks_ReturnsListOfBooks() {
        // When
        List<Book> foundBooks = bookRepository
                .findAll(mock(Specification.class), Pageable.unpaged())
                .getContent();

        // Then
        assertEquals(5, foundBooks.size());
    }

    @Test
    @DisplayName("Test finding a book by existing ID")
    void findById_ExistingId_ReturnsOptionalOfBook() {
        // Given
        Long bookId = 1L;

        // When
        Optional<Book> foundBook = bookRepository.findById(bookId);

        // Then
        assertTrue(foundBook.isPresent());
        assertEquals(bookId, foundBook.get().getId());
    }

    @Test
    @DisplayName("Test finding a book by non-existing ID")
    void findById_NonExistingId_ReturnsEmptyOptional() {
        // Given
        Long nonExistingBookId = 100L;

        // When
        Optional<Book> foundBook = bookRepository.findById(nonExistingBookId);

        // Then
        assertTrue(foundBook.isEmpty());
    }

    @Test
    @DisplayName("Test finding all books as a page")
    void findAll_ReturnsPageOfBooks() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Specification<Book> specification = mock(Specification.class);

        // When
        Page<Book> foundPage = bookRepository.findAll(specification, pageable);

        // Then
        assertEquals(5, foundPage.getContent().size());
    }
}

package org.example.springbootintro.book;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationProvider;
import org.example.springbootintro.repository.book.BookSpecificationBuilder;
import org.example.springbootintro.repository.book.BookSpecificationProviderManager;
import org.example.springbootintro.repository.book.spec.AuthorSpecificationProvider;
import org.example.springbootintro.repository.book.spec.TitleSpecificationProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
@DisplayName("Book Specification Builder Tests")
class BookSpecificationBuilderTest {
    @InjectMocks
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private BookSpecificationProviderManager bookSpecificationProviderManager;

    @Mock
    private SpecificationProvider<Book> titleSpecificationProvider;

    @Mock
    private SpecificationProvider<Book> authorSpecificationProvider;

    @Test
    @DisplayName("Build with empty search parameters returns null")
    void build_WithEmptySearchParameters_ReturnsNull() {
        // Given
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto();

        // When
        Specification<Book> spec = bookSpecificationBuilder.build(searchParametersDto);

        // Then
        assertNull(spec);
    }

    @Test
    @DisplayName("Build with title search parameter returns title specification")
    void build_WithTitleSearchParameter_ReturnsTitleSpecification() {
        // Given
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto();
        searchParametersDto.setTitle(new String[]{"Title"});
        when(bookSpecificationProviderManager
                .getSpecificationProvider(TitleSpecificationProvider.TITLE))
                .thenReturn(titleSpecificationProvider);
        when(titleSpecificationProvider.getSpecification(any()))
                .thenReturn((root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("title"), "Some Title"));

        // When
        Specification<Book> spec = bookSpecificationBuilder.build(searchParametersDto);

        // Then
        assertNotNull(spec);
    }

    @Test
    @DisplayName("Build with author search parameter returns author specification")
    void build_WithAuthorSearchParameter_ReturnsAuthorSpecification() {
        // Given
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto();
        searchParametersDto.setAuthor(new String[]{"Author"});
        when(bookSpecificationProviderManager
                .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR))
                .thenReturn(authorSpecificationProvider);
        when(authorSpecificationProvider
                .getSpecification(any()))
                .thenReturn((root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("author"), "Some Author"));

        // When
        Specification<Book> spec = bookSpecificationBuilder.build(searchParametersDto);

        // Then
        assertNotNull(spec);
    }
}

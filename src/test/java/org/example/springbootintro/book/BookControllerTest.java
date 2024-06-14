package org.example.springbootintro.book;

import static org.example.springbootintro.TestDataBook.BOOK_BY_ID_URL;
import static org.example.springbootintro.TestDataBook.BOOK_ID;
import static org.example.springbootintro.TestDataBook.BOOK_TITLE;
import static org.example.springbootintro.TestDataBook.BOOK_URL;
import static org.example.springbootintro.TestDataBook.NEW_AUTHOR;
import static org.example.springbootintro.TestDataBook.NEW_COVER_IMAGE;
import static org.example.springbootintro.TestDataBook.NEW_DESCRIPTION;
import static org.example.springbootintro.TestDataBook.NEW_ISBN;
import static org.example.springbootintro.TestDataBook.NEW_PRICE;
import static org.example.springbootintro.TestDataBook.NEW_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.example.springbootintro.service.book.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static MockMvc mockMvc;

    private CreateBookRequestDto requestDto;

    private BookDto expected;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp() {
        requestDto = new CreateBookRequestDto();
        requestDto.setTitle(NEW_TITLE);
        requestDto.setAuthor(NEW_AUTHOR);
        requestDto.setPrice(NEW_PRICE);
        requestDto.setIsbn(NEW_ISBN);
        requestDto.setCoverImage(NEW_COVER_IMAGE);
        requestDto.setDescription(NEW_DESCRIPTION);
        requestDto.setCategoryIds(Set.of(1L));

        expected = new BookDto();
        expected.setId(BOOK_ID);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setPrice(requestDto.getPrice());
        expected.setIsbn(requestDto.getIsbn());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());
        expected.setCategoryIds(requestDto.getCategoryIds());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Test save book")
    void saveBookTest() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        String result = mockMvc.perform(post(BOOK_URL)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Then
        BookDto actual = objectMapper.readValue(result, BookDto.class);

        // Verify
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Test find all books")
    void findAllBooksTest() throws Exception {
        // When
        String result = mockMvc.perform(get(BOOK_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        List<BookDto> actual = objectMapper.readValue(result,
                new TypeReference<List<BookDto>>() {});

        // Verify
        assertNotNull(actual);
        assertEquals(4, actual.size());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Test find book by id")
    void findBookById() throws Exception {
        // Given
        BookDto expected = new BookDto();
        expected.setId(BOOK_ID);
        expected.setTitle(BOOK_TITLE);
        expected.setAuthor(NEW_AUTHOR);
        expected.setPrice(NEW_PRICE);
        expected.setIsbn(NEW_ISBN);
        expected.setDescription(NEW_DESCRIPTION);
        expected.setCoverImage(NEW_COVER_IMAGE);
        expected.setCategoryIds(Set.of(1L));

        when(bookService.findById(BOOK_ID)).thenReturn(expected);

        // When
        mockMvc.perform(get(BOOK_BY_ID_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOK_ID))
                .andExpect(jsonPath("$.title").value(BOOK_TITLE));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete book by id")
    void deleteBookSuccess() throws Exception {
        // When
        mockMvc.perform(delete(BOOK_BY_ID_URL, BOOK_ID))
                .andExpect(status().isNoContent());

        // Then
        Optional<Book> deleteBook = bookRepository.findById(BOOK_ID);

        // Verify
        assertTrue(deleteBook.isEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test delete book by id")
    void deleteBookUnSuccess() throws Exception {
        mockMvc.perform(delete(BOOK_BY_ID_URL))
                .andExpect(status().isForbidden());
    }
}

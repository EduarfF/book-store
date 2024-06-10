package org.example.springbootintro.book;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.example.springbootintro.controller.BookController;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.dto.book.validator.BookSearchParametersValidator;
import org.example.springbootintro.service.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class BookControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private BookSearchParametersValidator bookSearchParametersValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new BookController(bookService, bookSearchParametersValidator))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test save book with valid request")
    public void saveBook_ValidRequest_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Book");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(new BigDecimal("10.99"));
        requestDto.setAuthor("Test Author");
        requestDto.setCategoryIds(Set.of(1L, 2L));

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setTitle("Test Book");
        expectedBookDto.setIsbn("1234567890");
        expectedBookDto.setPrice(new BigDecimal("10.99"));
        expectedBookDto.setAuthor("Test Author");
        expectedBookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookService.save(any())).thenReturn(expectedBookDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.price").value("10.99"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.categoryIds").isArray())
                .andExpect(jsonPath("$.categoryIds", hasSize(2)))
                .andExpect(jsonPath("$.categoryIds", containsInAnyOrder(1, 2)));

        verify(bookService, times(1)).save(requestDto);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void findAllBooksTest() throws Exception {
        // Prepare test data
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1L, "Book1", "Author1"));
        bookDtoList.add(new BookDto(2L, "Book2", "Author2"));

        when(bookService.findAll(any(Pageable.class))).thenReturn(bookDtoList);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Book2"))
                .andExpect(jsonPath("$[1].author").value("Author2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test find book by id")
    public void findBookByIdTest() throws Exception {
        BookDto bookDto = new BookDto(1L, "TestBook", "TestAuthor");

        when(bookService.findById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("TestBook"))
                .andExpect(jsonPath("$.author").value("TestAuthor"));

        verify(bookService, times(1)).findById(1L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update book with valid request")
    public void updateBook_ValidRequest_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Test Book");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(new BigDecimal("15.99"));
        requestDto.setCategoryIds(Set.of(1L, 2L));
        requestDto.setAuthor("John Doe");

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setTitle("Updated Test Book");
        expectedBookDto.setIsbn("1234567890");
        expectedBookDto.setPrice(new BigDecimal("15.99"));
        expectedBookDto.setCategoryIds(Set.of(1L, 2L));

        when(bookService.updateById(eq(1L), any())).thenReturn(expectedBookDto);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.price").value("15.99"))
                .andExpect(jsonPath("$.categoryIds").isArray())
                .andExpect(jsonPath("$.categoryIds", hasSize(2)))
                .andExpect(jsonPath("$.categoryIds", containsInAnyOrder(1, 2)));

        verify(bookService, times(1)).updateById(eq(1L), any());
        verifyNoMoreInteractions(bookService);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete book by id")
    public void deleteBookByIdTest() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(1L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test search books")
    public void searchBooksTest() throws Exception {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1L, "Book1", "Author1"));
        bookDtoList.add(new BookDto(2L, "Book2", "Author2"));

        when(bookService.search(any(Pageable.class), any(BookSearchParametersDto.class)))
                .thenReturn(bookDtoList);

        mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("title", "TestTitle")
                        .param("author", "TestAuthor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Book2"))
                .andExpect(jsonPath("$[1].author").value("Author2"));

        verify(bookService, times(1)).search(any(Pageable.class),
                any(BookSearchParametersDto.class));
        verifyNoMoreInteractions(bookService);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

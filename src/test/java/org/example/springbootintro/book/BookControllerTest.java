package org.example.springbootintro.book;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.example.springbootintro.UtilsTest;
import org.example.springbootintro.controller.BookController;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.dto.book.validator.BookSearchParametersValidator;
import org.example.springbootintro.service.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ExtendWith(MockitoExtension.class)
@EnableWebMvc
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
        // Given
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expectedBookDto = createBookDto(requestDto);

        // When
        when(bookService.save(any())).thenReturn(expectedBookDto);

        // Then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UtilsTest.asJsonString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.price").value("10.99"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.categoryIds").isArray())
                .andExpect(jsonPath("$.categoryIds", hasSize(2)))
                .andExpect(jsonPath("$.categoryIds", containsInAnyOrder(1, 2)));

        // Verify
        verify(bookService).save(requestDto);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @DisplayName("Test find all books")
    public void findAllBooksTest() throws Exception {
        // Given
        List<BookDto> bookDtoList = createBookDtoList();

        // When
        when(bookService.findAll(any(Pageable.class))).thenReturn(bookDtoList);

        // Then
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
    @DisplayName("Test find book by ID")
    public void findBookByIdTest() throws Exception {
        // Given
        BookDto bookDto = new BookDto(1L, "TestBook", "TestAuthor");

        // When
        when(bookService.findById(1L)).thenReturn(bookDto);

        // Then
        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("TestBook"))
                .andExpect(jsonPath("$.author").value("TestAuthor"));

        // Verify
        verify(bookService).findById(1L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update book with valid request")
    public void updateBook_ValidRequest_Success() throws Exception {
        // Given
        CreateBookRequestDto requestDto = createBookRequestDto();
        BookDto expectedBookDto = createBookDto(requestDto);

        // When
        when(bookService.updateById(eq(1L), any())).thenReturn(expectedBookDto);

        // Then
        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UtilsTest.asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.price").value("10.99"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.categoryIds").isArray())
                .andExpect(jsonPath("$.categoryIds", hasSize(2)))
                .andExpect(jsonPath("$.categoryIds", containsInAnyOrder(1, 2)));

        // Verify
        verify(bookService).updateById(eq(1L), any());
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete book by ID")
    public void deleteBookByIdTest() throws Exception {
        // Then
        mockMvc.perform(delete("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify
        verify(bookService).deleteById(1L);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test search books")
    public void searchBooksTest() throws Exception {
        // Given
        List<BookDto> bookDtoList = createBookDtoList();

        // When
        when(bookService.search(any(Pageable.class), any(BookSearchParametersDto.class)))
                .thenReturn(bookDtoList);

        // Then
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

        // Verify
        verify(bookService).search(any(Pageable.class), any(BookSearchParametersDto.class));
        verifyNoMoreInteractions(bookService);
    }

    private CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Book");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(new BigDecimal("10.99"));
        requestDto.setAuthor("Test Author");
        requestDto.setCategoryIds(Set.of(1L, 2L));
        return requestDto;
    }

    private BookDto createBookDto(CreateBookRequestDto requestDto) {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(requestDto.getTitle());
        bookDto.setIsbn(requestDto.getIsbn());
        bookDto.setPrice(requestDto.getPrice());
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setCategoryIds(requestDto.getCategoryIds());
        return bookDto;
    }

    private List<BookDto> createBookDtoList() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto(1L, "Book1", "Author1"));
        bookDtoList.add(new BookDto(2L, "Book2", "Author2"));
        return bookDtoList;
    }
}

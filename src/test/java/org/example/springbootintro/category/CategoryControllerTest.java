package org.example.springbootintro.category;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.example.springbootintro.UtilsTest;
import org.example.springbootintro.controller.CategoryController;
import org.example.springbootintro.dto.book.BookDtoWithoutCategoryIds;
import org.example.springbootintro.dto.category.CategoryDto;
import org.example.springbootintro.dto.category.CreateCategoryRequestDto;
import org.example.springbootintro.service.book.BookService;
import org.example.springbootintro.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test save category")
    public void saveCategoryTest() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("TestCategory");
        requestDto.setDescription("TestDescription");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("TestCategory");
        expectedCategoryDto.setDescription("TestDescription");

        given(categoryService.save(requestDto)).willReturn(expectedCategoryDto);

        // When
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UtilsTest.asJsonString(requestDto)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        // Verify
        verify(categoryService).save(requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("Test find category by id")
    public void findCategoryByIdTest() throws Exception {
        // Given
        CategoryDto categoryDto = new CategoryDto(1L, "TestCategory", "TestDescription");
        given(categoryService.findById(1L)).willReturn(categoryDto);

        // When
        mockMvc.perform(get("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        // Verify
        verify(categoryService).findById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update category by id")
    public void updateCategoryByIdTest() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("TestCategory");
        requestDto.setDescription("TestDescription");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("TestCategory");
        expectedCategoryDto.setDescription("TestDescription");

        given(categoryService.updateById(1L, requestDto)).willReturn(expectedCategoryDto);

        // When
        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UtilsTest.asJsonString(requestDto)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        // Verify
        verify(categoryService).updateById(1L, requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete category by id")
    public void deleteCategoryByIdTest() throws Exception {
        // When
        mockMvc.perform(delete("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNoContent());

        // Verify
        verify(categoryService).deleteById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test find books by category id")
    public void findBooksByCategoryIdTest() throws Exception {
        // Given
        List<BookDtoWithoutCategoryIds> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDtoWithoutCategoryIds(1L, "Book1", "Author1"));
        bookDtoList.add(new BookDtoWithoutCategoryIds(2L, "Book2", "Author2"));

        given(bookService.findAllByCategoryId(1L)).willReturn(bookDtoList);

        // When
        mockMvc.perform(get("/categories/{id}/books", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Book2"))
                .andExpect(jsonPath("$[1].author").value("Author2"));

        // Verify
        verify(bookService).findAllByCategoryId(1L);
        verifyNoMoreInteractions(bookService);
    }
}

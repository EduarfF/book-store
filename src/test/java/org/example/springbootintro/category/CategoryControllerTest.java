package org.example.springbootintro.category;

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
import java.util.ArrayList;
import java.util.List;
import org.example.springbootintro.controller.CategoryController;
import org.example.springbootintro.dto.book.BookDtoWithoutCategoryIds;
import org.example.springbootintro.dto.category.CategoryDto;
import org.example.springbootintro.dto.category.CreateCategoryRequestDto;
import org.example.springbootintro.service.book.BookService;
import org.example.springbootintro.service.category.CategoryService;
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

public class CategoryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CategoryController(categoryService, bookService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test save category")
    public void saveCategoryTest() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("TestCategory");
        requestDto.setDescription("TestDescription");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("TestCategory");
        expectedCategoryDto.setDescription("TestDescription");

        when(categoryService.save(requestDto)).thenReturn(expectedCategoryDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        verify(categoryService, times(1)).save(requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void findAllCategoriesTest() throws Exception {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(new CategoryDto(1L, "TestCategory1", "TestDescription1"));
        categoryDtoList.add(new CategoryDto(2L, "TestCategory2", "TestDescription2"));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryDtoList);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page","0")
                        .param("size","10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("TestCategory1"))
                .andExpect(jsonPath("$[0].description").value("TestDescription1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("TestCategory2"))
                .andExpect(jsonPath("$[1].description").value("TestDescription2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test find category by id")
    public void findCategoryByIdTest() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1L, "TestCategory", "TestDescription");

        when(categoryService.findById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        verify(categoryService, times(1)).findById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update category by id")
    public void updateCategoryByIdTest() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("TestCategory");
        requestDto.setDescription("TestDescription");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("TestCategory");
        expectedCategoryDto.setDescription("TestDescription");

        when(categoryService.updateById(1L, requestDto)).thenReturn(expectedCategoryDto);

        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TestCategory"))
                .andExpect(jsonPath("$.description").value("TestDescription"));

        verify(categoryService, times(1)).updateById(1L, requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete category by id")
    public void deleteCategoryByIdTest() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(1L);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test find books by category id")
    public void findBooksByCategoryIdTest() throws Exception {
        List<BookDtoWithoutCategoryIds> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDtoWithoutCategoryIds(1L, "Book1", "Author1"));
        bookDtoList.add(new BookDtoWithoutCategoryIds(2L, "Book2", "Author2"));

        when(bookService.findAllByCategoryId(1L)).thenReturn(bookDtoList);

        mockMvc.perform(get("/categories/{id}/books", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Book2"))
                .andExpect(jsonPath("$[1].author").value("Author2"));

        verify(bookService, times(1)).findAllByCategoryId(1L);
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

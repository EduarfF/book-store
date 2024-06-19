package org.example.springbootintro.category;

import static org.example.springbootintro.testdata.TestDataCategory.BASE_URL;
import static org.example.springbootintro.testdata.TestDataCategory.CATEGORY_BOOKS_URL;
import static org.example.springbootintro.testdata.TestDataCategory.CATEGORY_ID_URL;
import static org.example.springbootintro.testdata.TestDataCategory.JSON_CONTENT_TYPE;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_AUTHOR_1;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_AUTHOR_2;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_ID_1;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_ID_2;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_TITLE_1;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_BOOK_TITLE_2;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_CATEGORY_DESCRIPTION;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_CATEGORY_ID;
import static org.example.springbootintro.testdata.TestDataCategory.TEST_CATEGORY_NAME;
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
import org.example.springbootintro.TestUtils;
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
        requestDto.setName(TEST_CATEGORY_NAME);
        requestDto.setDescription(TEST_CATEGORY_DESCRIPTION);

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(TEST_CATEGORY_ID);
        expectedCategoryDto.setName(TEST_CATEGORY_NAME);
        expectedCategoryDto.setDescription(TEST_CATEGORY_DESCRIPTION);

        given(categoryService.save(requestDto)).willReturn(expectedCategoryDto);

        // When
        var resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(JSON_CONTENT_TYPE)
                .content(TestUtils.asJsonString(requestDto)));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        // Verify
        verify(categoryService).save(requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @DisplayName("Test find category by id")
    public void findCategoryByIdTest() throws Exception {
        // Given
        CategoryDto categoryDto =
                new CategoryDto(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_CATEGORY_DESCRIPTION);
        given(categoryService.findById(TEST_CATEGORY_ID)).willReturn(categoryDto);

        // When
        var resultActions = mockMvc.perform(get(CATEGORY_ID_URL, TEST_CATEGORY_ID)
                .contentType(JSON_CONTENT_TYPE));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        // Verify
        verify(categoryService).findById(TEST_CATEGORY_ID);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test update category by id")
    public void updateCategoryByIdTest() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(TEST_CATEGORY_NAME);
        requestDto.setDescription(TEST_CATEGORY_DESCRIPTION);

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(TEST_CATEGORY_ID);
        expectedCategoryDto.setName(TEST_CATEGORY_NAME);
        expectedCategoryDto.setDescription(TEST_CATEGORY_DESCRIPTION);

        given(categoryService.updateById(TEST_CATEGORY_ID, requestDto))
                .willReturn(expectedCategoryDto);

        // When
        var resultActions = mockMvc.perform(put(CATEGORY_ID_URL, TEST_CATEGORY_ID)
                .contentType(JSON_CONTENT_TYPE)
                .content(TestUtils.asJsonString(requestDto)));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(TEST_CATEGORY_NAME))
                .andExpect(jsonPath("$.description").value(TEST_CATEGORY_DESCRIPTION));

        // Verify
        verify(categoryService).updateById(TEST_CATEGORY_ID, requestDto);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test delete category by id")
    public void deleteCategoryByIdTest() throws Exception {
        // When
        var resultActions = mockMvc.perform(delete(CATEGORY_ID_URL, TEST_CATEGORY_ID)
                .contentType(JSON_CONTENT_TYPE));

        // Then
        resultActions.andExpect(status().isNoContent());

        // Verify
        verify(categoryService).deleteById(TEST_CATEGORY_ID);
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test find books by category id")
    public void findBooksByCategoryIdTest() throws Exception {
        // Given
        List<BookDtoWithoutCategoryIds> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDtoWithoutCategoryIds(
                TEST_BOOK_ID_1, TEST_BOOK_TITLE_1, TEST_BOOK_AUTHOR_1));
        bookDtoList.add(new BookDtoWithoutCategoryIds(
                TEST_BOOK_ID_2, TEST_BOOK_TITLE_2, TEST_BOOK_AUTHOR_2));

        given(bookService.findAllByCategoryId(TEST_CATEGORY_ID)).willReturn(bookDtoList);

        // When
        var resultActions = mockMvc.perform(get(CATEGORY_BOOKS_URL, TEST_CATEGORY_ID)
                .contentType(JSON_CONTENT_TYPE));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_BOOK_ID_1))
                .andExpect(jsonPath("$[0].title").value(TEST_BOOK_TITLE_1))
                .andExpect(jsonPath("$[0].author").value(TEST_BOOK_AUTHOR_1))
                .andExpect(jsonPath("$[1].id").value(TEST_BOOK_ID_2))
                .andExpect(jsonPath("$[1].title").value(TEST_BOOK_TITLE_2))
                .andExpect(jsonPath("$[1].author").value(TEST_BOOK_AUTHOR_2));

        // Verify
        verify(bookService).findAllByCategoryId(TEST_CATEGORY_ID);
        verifyNoMoreInteractions(bookService);
    }
}

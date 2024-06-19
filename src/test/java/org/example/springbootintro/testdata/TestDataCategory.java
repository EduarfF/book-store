package org.example.springbootintro.testdata;

import org.springframework.http.MediaType;

public class TestDataCategory {
    public static final String BASE_URL = "/categories";
    public static final String CATEGORY_ID_URL = BASE_URL + "/{id}";
    public static final String CATEGORY_BOOKS_URL = CATEGORY_ID_URL + "/books";
    public static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    public static final Long TEST_CATEGORY_ID = 1L;
    public static final String TEST_CATEGORY_NAME = "TestCategory";
    public static final String TEST_CATEGORY_DESCRIPTION = "TestDescription";
    public static final Long TEST_BOOK_ID_1 = 1L;
    public static final String TEST_BOOK_TITLE_1 = "Book1";
    public static final String TEST_BOOK_AUTHOR_1 = "Author1";
    public static final Long TEST_BOOK_ID_2 = 2L;
    public static final String TEST_BOOK_TITLE_2 = "Book2";
    public static final String TEST_BOOK_AUTHOR_2 = "Author2";
}

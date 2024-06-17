package org.example.springbootintro.dto.book.validator;

import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookSearchParametersValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookSearchParametersDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookSearchParametersDto searchParameters = (BookSearchParametersDto) target;

        if (searchParameters.getTitle() == null && searchParameters.getAuthor() == null) {
            errors.reject(
                    "searchParameters",
                    "At least one search parameter (title or author) must be provided."
            );
        }

        if (searchParameters.getTitle() != null && searchParameters.getAuthor() != null) {
            if (searchParameters.getAuthor().length == 0
                    && searchParameters.getTitle().length == 0) {
                errors.reject(
                        "searchParameters",
                        "Author and Title can not be empty when searching by author and title."
                );
            }
            if (searchParameters.getAuthor().length == 0
                    && searchParameters.getTitle().length > 0) {
                errors.reject(
                        "searchParameters",
                        "Author can not be empty when searching by author and title."
                );
            }
            if (searchParameters.getTitle().length == 0
                    && searchParameters.getAuthor().length > 0) {
                errors.reject(
                        "searchParameters",
                        "Title can not be empty when searching by author and title."
                );
            }
        }

        if (searchParameters.getTitle() == null && searchParameters.getAuthor() != null) {
            if (searchParameters.getAuthor().length == 0) {
                errors.reject(
                        "searchParameters",
                        "Author can not be empty."
                );
            }
        }

        if (searchParameters.getAuthor() == null && searchParameters.getTitle() != null) {
            if (searchParameters.getTitle().length == 0) {
                errors.reject(
                        "searchParameters",
                        "Title can not be empty."
                );
            }
        }
    }
}

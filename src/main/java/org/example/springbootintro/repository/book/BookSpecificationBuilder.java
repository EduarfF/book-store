package org.example.springbootintro.repository.book;

import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationBuilder;
import org.example.springbootintro.repository.SpecificationProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    @Autowired
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(searchParametersDto.author()));
        }
        return spec;
    }
}

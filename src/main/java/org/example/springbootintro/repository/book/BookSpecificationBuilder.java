package org.example.springbootintro.repository.book;

import lombok.AllArgsConstructor;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationBuilder;
import org.example.springbootintro.repository.SpecificationProviderManager;
import org.example.springbootintro.repository.book.spec.AuthorSpecificationProvider;
import org.example.springbootintro.repository.book.spec.TitleSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR)
                    .getSpecification(searchParametersDto.author()));
        }
        return spec;
    }
}

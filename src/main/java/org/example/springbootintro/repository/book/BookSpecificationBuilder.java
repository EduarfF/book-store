package org.example.springbootintro.repository.book;

import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationBuilder;
import org.example.springbootintro.repository.SpecificationProviderManager;
import org.example.springbootintro.repository.book.spec.AuthorSpecificationProvider;
import org.example.springbootintro.repository.book.spec.TitleSpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto == null || (searchParametersDto.getTitle() == null
                && searchParametersDto.getAuthor() == null)) {
            return null;
        }
        Specification<Book> spec = Specification.where(null);
        if (searchParametersDto.getTitle() != null && searchParametersDto.getTitle().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TitleSpecificationProvider.TITLE)
                    .getSpecification(searchParametersDto.getTitle()));
        }
        if (searchParametersDto.getAuthor() != null && searchParametersDto.getAuthor().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AuthorSpecificationProvider.AUTHOR)
                    .getSpecification(searchParametersDto.getAuthor()));
        }
        return spec;
    }
}

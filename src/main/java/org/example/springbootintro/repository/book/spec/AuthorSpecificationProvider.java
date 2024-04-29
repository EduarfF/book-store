package org.example.springbootintro.repository.book.spec;

import java.util.Arrays;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR = "author";

    @Override
    public String getKey() {
        return AUTHOR;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(AUTHOR).in(Arrays.stream(params).toArray()));

    }
}

package org.example.springbootintro.repository.book.spec;

import java.util.Arrays;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(TITLE).in(Arrays.stream(params).toArray()));
    }
}

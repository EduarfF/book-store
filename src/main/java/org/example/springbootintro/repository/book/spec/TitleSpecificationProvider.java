package org.example.springbootintro.repository.book.spec;

import java.util.Arrays;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get("title").in(Arrays.stream(params).toArray()));
    }
}

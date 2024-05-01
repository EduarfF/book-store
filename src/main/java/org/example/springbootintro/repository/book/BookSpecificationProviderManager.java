package org.example.springbootintro.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.SpecificationProvider;
import org.example.springbootintro.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviderList.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find specification provider for key: " + key)
                );
    }
}

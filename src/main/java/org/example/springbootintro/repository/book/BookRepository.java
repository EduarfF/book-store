package org.example.springbootintro.repository.book;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}

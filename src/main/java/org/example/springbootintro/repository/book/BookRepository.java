package org.example.springbootintro.repository.book;

import java.util.List;
import org.example.springbootintro.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}

package org.example.springbootintro.service.book;

import java.util.List;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}

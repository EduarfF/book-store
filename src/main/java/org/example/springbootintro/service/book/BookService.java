package org.example.springbootintro.service.book;

import java.util.List;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.BookSearchParametersDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(Pageable pageable, BookSearchParametersDto params);
}

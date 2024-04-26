package org.example.springbootintro.service.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.springbootintro.dto.book.BookDto;
import org.example.springbootintro.dto.book.CreateBookRequestDto;
import org.example.springbootintro.exception.EntityNotFoundException;
import org.example.springbootintro.mapper.book.BookMapper;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.repository.book.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        checkIfBookExists(id);
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        checkIfBookExists(id);
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    private void checkIfBookExists(Long id) {
        if (bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book by id: " + id);
        }
    }
}

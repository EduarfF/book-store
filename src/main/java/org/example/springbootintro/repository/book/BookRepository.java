package org.example.springbootintro.repository.book;

import java.util.List;
import java.util.Optional;
import org.example.springbootintro.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = {"categories"})
    List<Book> findAllByCategoriesId(Long categoryId);

    @Query("FROM Book b JOIN FETCH b.categories")
    List<Book> findAllBooks(Pageable pageable);

    @Query("FROM Book b JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);
}

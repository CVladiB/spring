package ru.baranova.spring.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.baranova.spring.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @Override
    <S extends Book> S save(S entity);

    @Override
    Optional<Book> findById(Integer integer);

    @Query("select b from Book b join fetch b.author where b.title = :title")
    List<Book> findByTitle(@Param("title") @NonNull String title);

    @Query("select b from Book b join fetch b.author where b.title = :title and b.author.id = :author_id")
    List<Book> findByTitleAndAuthor(@Param("title") @NonNull String title, @Param("author_id") @NonNull Integer authorId);

    @Override
    List<Book> findAll();

    @Override
    void delete(Book entity);
}

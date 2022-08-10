package ru.baranova.spring.dao.book;

import ru.baranova.spring.domain.BookEntity;

import java.util.List;

public interface BookDao {
    BookEntity create(String title, Integer authorId, List<Integer> genreId);

    BookEntity getById(Integer id);

    BookEntity getByIdWithoutGenre(Integer id);

    List<BookEntity> getByTitle(String title);

    List<BookEntity> getAll();

    BookEntity update(Integer id, String title, Integer authorId, List<Integer> genreId);

    boolean delete(Integer id);
}

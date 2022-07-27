package ru.baranova.spring.dao.book;

import ru.baranova.spring.domain.Book;

import java.util.List;

public interface BookDao {
    Integer create(String title, Integer authorId, List<Integer> genreId);

    Book getById(Integer id);

    List<Book> getByTitle(String title);

    List<Book> getAll();

    void update(Integer id, String title, Integer authorId, List<Integer> genreId);

    void delete(Integer id);
}

package ru.baranova.spring.dao.book;

import ru.baranova.spring.domain.Book;

import java.util.List;
import java.util.stream.Stream;

public interface BookDao {
    Integer create(Book book);

    Book getById(Integer id);

    List<Book> getByTitle(String title);

    List<Book> getAll();

    void update(Book book);

    void delete(Integer id);
}

package ru.baranova.spring.dao.book;

import ru.baranova.spring.domain.Book;

import java.util.List;

public interface BookDao {
    void create(Book book);

    Book read(Integer id);

    List<Book> read();

    void update(Book book);

    void delete(Integer id);
}

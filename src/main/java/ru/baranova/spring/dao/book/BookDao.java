package ru.baranova.spring.dao.book;

import ru.baranova.spring.domain.Book;

import java.util.List;

public interface BookDao {
    void create(Book book);
    Book read(int id);
    List<Book> read();
    void update(Book book);
    void delete(int id);
}

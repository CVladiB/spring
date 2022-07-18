package ru.baranova.spring.dao;

import ru.baranova.spring.domain.Book;

public interface BookDao {
    void create(Book book);
    void read(int id);
    void update(Book book);
    void delete(int id);
}

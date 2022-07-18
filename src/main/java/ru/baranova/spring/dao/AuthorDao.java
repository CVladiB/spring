package ru.baranova.spring.dao;

import ru.baranova.spring.domain.Author;

public interface AuthorDao {
    void create(Author author);
    void read(int id);
    void update(Author author);
    void delete(int id);
}

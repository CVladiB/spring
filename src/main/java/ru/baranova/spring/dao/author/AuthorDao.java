package ru.baranova.spring.dao.author;

import ru.baranova.spring.domain.Author;

import java.util.List;

public interface AuthorDao {
    void create(Author author);
    Author read(int id);
    List<Author> read();
    void update(Author author);
    void delete(int id);
}

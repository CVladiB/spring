package ru.baranova.spring.dao.author;

import ru.baranova.spring.domain.Author;

import java.util.List;

public interface AuthorDao {
    Author create(String surname, String name);

    Author getById(Integer id);

    List<Author> getBySurnameAndName(String surname, String name);

    List<Author> getAll();

    Author update(Integer id, String surname, String name);

    boolean delete(Integer id);
}

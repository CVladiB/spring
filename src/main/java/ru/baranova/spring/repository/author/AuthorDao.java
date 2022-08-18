package ru.baranova.spring.repository.author;

import ru.baranova.spring.model.Author;

import java.util.List;

public interface AuthorDao {
    Author create(String surname, String name);

    Author getById(Integer id);

    List<Author> getBySurnameAndName(String surname, String name);

    List<Author> getAll();

    Author update(Integer id, String surname, String name);

    Boolean delete(Integer id);
}

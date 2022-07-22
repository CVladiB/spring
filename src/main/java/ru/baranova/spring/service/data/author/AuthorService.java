package ru.baranova.spring.service.data.author;

import ru.baranova.spring.domain.Author;

import java.util.List;

public interface AuthorService {
    Author create(String surname, String name);

    Author read(Integer id);

    List<Author> getBySurnameAndName(String surname, String name);

    List<Author> readAll();

    Author update(Integer id, String surname, String name);

    void delete(Integer id);

}

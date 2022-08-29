package ru.baranova.spring.service.data.author;

import ru.baranova.spring.model.Author;

import java.util.List;

public interface AuthorService {
    Author create(String surname, String name);

    Author readById(Integer id);

    List<Author> readBySurnameAndName(String surname, String name);

    List<Author> readAll();

    Author update(Integer id, String surname, String name);

    boolean delete(Integer id);
}

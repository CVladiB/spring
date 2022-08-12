package ru.baranova.spring.service.data.author;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.data.BaseService;

import java.util.List;

public interface AuthorService extends BaseService {
    Author create(String surname, String name);

    Author readById(Integer id);

    List<Author> readBySurnameAndName(String surname, String name);

    List<Author> readAll();

    Author update(Integer id, String surname, String name);

    boolean delete(Integer id);
}

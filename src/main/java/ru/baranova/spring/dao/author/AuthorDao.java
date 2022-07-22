package ru.baranova.spring.dao.author;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.Author;

import java.util.List;

public interface AuthorDao {
    Integer create(@NonNull Author author);

    Author getById(Integer id);

    List<Author> getBySurnameAndName(String surname, String name);

    List<Author> getAll();

    void update(Author author);

    void delete(Integer id);
}

package ru.baranova.spring.dao.author;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.Author;

import java.util.List;

public interface AuthorDao {
    void create(@NonNull String surname, @NonNull String name);

    Author read(Integer id);

    List<Author> read();

    void update(Author author);

    void delete(Integer id);
}

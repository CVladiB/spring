package ru.baranova.spring.dao.genre;

import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface GenreDao {
    Integer create(Genre genre);

    Genre getById(Integer id);

    Genre getByName(String name);

    List<Genre> getAll();

    void update(Genre genre);

    void delete(Integer id);
}

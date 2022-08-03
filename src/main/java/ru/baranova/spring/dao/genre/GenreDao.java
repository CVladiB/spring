package ru.baranova.spring.dao.genre;

import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface GenreDao {
    Genre create(String name, String description);

    Genre getById(Integer id);

    Genre getByName(String name);

    List<Genre> getAll();

    Genre update(Integer id, String name, String description);

    int delete(Integer id);
}

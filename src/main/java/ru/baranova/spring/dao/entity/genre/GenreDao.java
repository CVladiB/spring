package ru.baranova.spring.dao.entity.genre;

import ru.baranova.spring.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre create(String name, String description);

    Genre getById(Integer id);

    List<Genre> getByName(String name);

    List<Genre> getAll();

    Genre update(Integer id, String name, String description);

    Boolean delete(Integer id);
}

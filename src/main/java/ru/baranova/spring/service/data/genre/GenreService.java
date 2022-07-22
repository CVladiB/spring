package ru.baranova.spring.service.data.genre;

import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface GenreService {
    Genre create(String title, String description);

    Genre getByName(String name);

    Genre read(Integer id);

    List<Genre> readAll();

    Genre update(Integer id, String name, String description);

    void delete(Integer id);

}

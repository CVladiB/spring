package ru.baranova.spring.service.data.genre;

import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.BaseService;

import java.util.List;

public interface GenreService extends BaseService {
    Genre create(String name, String description);

    Genre readById(Integer id);

    List<Genre> readByName(String name);

    List<Genre> readAll();

    Genre update(Integer id, String name, String description);

    boolean delete(Integer id);
}

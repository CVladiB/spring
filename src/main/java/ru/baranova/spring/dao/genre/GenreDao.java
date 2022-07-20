package ru.baranova.spring.dao.genre;

import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface GenreDao {
    void create(Genre genre);
    Genre read(int id);
    List<Genre> read();
    void update(Genre genre);
    void delete(int id);
}

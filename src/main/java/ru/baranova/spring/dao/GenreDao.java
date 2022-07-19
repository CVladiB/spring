package ru.baranova.spring.dao;

import ru.baranova.spring.domain.Genre;

public interface GenreDao {
    void create(Genre genre);
    Genre read(int id);
    void update(Genre genre);
    void delete(int id);
}

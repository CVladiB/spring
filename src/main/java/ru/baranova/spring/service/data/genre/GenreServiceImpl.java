package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDaoJdbc;

    @Override
    public Genre create(String name, String description) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);
        Integer id = genreDaoJdbc.create(genre);
        genre.setId(id);
        return genre;
    }

    @Override
    public Genre getByName(String name) {
        Genre genre = genreDaoJdbc.getByName(name);
        return genre;
    }

    @Override
    public Genre read(Integer id) {
        Genre genre = genreDaoJdbc.getById(id);
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        List<Genre> genreList = genreDaoJdbc.getAll();
        return genreList;
    }

    @Override
    public Genre update(Integer id, String name, String description) {
        Genre genre = genreDaoJdbc.getById(id);
        genre.setName(name);
        genre.setDescription(description);
        genreDaoJdbc.update(genre);
        return genre;
    }

    @Override
    public void delete(Integer id) {
        genreDaoJdbc.delete(id);
    }

}

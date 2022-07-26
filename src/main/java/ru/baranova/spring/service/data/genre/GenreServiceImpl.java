package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDaoJdbc;
    private final CheckService checkServiceImpl;
    private int minInput;
    private int maxInputName;
    private int maxInputDescription;

    private void init() {
        minInput = 3;
        maxInputName = 20;
        maxInputDescription = 200;
    }

    @Override
    public Genre create(String name, String description) {
        init();
        List<String> listGenreName;
        Genre genre = null;

        if (checkServiceImpl.checkCorrectInput(name, minInput, maxInputName)
                && checkServiceImpl.checkCorrectInput(description, minInput, maxInputDescription)
                && checkServiceImpl.checkCorrectInputWordWithoutSymbol(name)
                && checkServiceImpl.checkCorrectInputWordWithoutSymbol(description)) {
            listGenreName = readAll().stream().map(Genre::getName).toList();
            if (checkServiceImpl.checkCorrectInputFromExist(name, listGenreName)) {
                genre = new Genre();
                genre.setName(name);
                genre.setDescription(description);
                Integer id = genreDaoJdbc.create(genre);
                genre.setId(id);
            }
        }
        return genre;
    }

    @Override
    public Genre readById(Integer id) {
        List<Integer> listId = readAll().stream().map(Genre::getId).toList();
        Genre genre = null;

        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            genre = genreDaoJdbc.getById(id);
        }
        return genre;
    }

    @Override
    public Genre readByName(String name) {
        List<String> listGenreName;
        Genre genre = null;
        listGenreName = readAll().stream().map(Genre::getName).toList();
        if (checkServiceImpl.checkCorrectInputFromExist(name, listGenreName)) {
            genre = genreDaoJdbc.getByName(name);
        }
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        List<Genre> genreList = genreDaoJdbc.getAll();
        return genreList;
    }

    @Override
    public Genre update(Integer id, String name, String description) {
        List<Integer> listId = readAll().stream().map(Genre::getId).toList();
        Genre genre = null;

        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            genre = genreDaoJdbc.getById(id);
            init();
            if (checkServiceImpl.checkCorrectInput(name, minInput, maxInputName)
                    && checkServiceImpl.checkCorrectInputWordWithoutSymbol(name)) {
                genre.setName(name);
            }
            if (checkServiceImpl.checkCorrectInput(description, minInput, maxInputDescription)
                    && checkServiceImpl.checkCorrectInputWordWithoutSymbol(description)) {
                genre.setDescription(description);
            }
            genreDaoJdbc.update(genre);
        }
        return genre;
    }

    @Override
    public void delete(Integer id) {
        List<Integer> listId = readAll().stream().map(Genre::getId).toList();
        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            genreDaoJdbc.delete(id);
        }
    }
}

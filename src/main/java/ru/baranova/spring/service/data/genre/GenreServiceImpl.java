package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT_NAME = 20;
    private final static int MAX_INPUT_DESCRIPTION = 200;
    private final GenreDao genreDaoJdbc;
    private final CheckService checkServiceImpl;
    private final AppService appServiceImpl;

    @Nullable
    @Override
    public Genre create(@NonNull String name, String description) {
        Genre genre = null;
        if (checkServiceImpl.checkIfNotExist(() -> readByName(name) == null)
                && checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
            String finalDescription = checkServiceImpl.isCorrectSymbolsInInputString(description, MIN_INPUT, MAX_INPUT_DESCRIPTION) ?
                    description : null;

            genre = appServiceImpl.evaluate(() -> genreDaoJdbc.create(name, finalDescription));
        }
        return genre;
    }

    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        return appServiceImpl.evaluate(() -> genreDaoJdbc.getById(id));
    }

    @Nullable
    @Override
    public Genre readByName(@NonNull String name) {
        return appServiceImpl.evaluate(() -> genreDaoJdbc.getByName(name));
    }

    @Override
    public List<Genre> readAll() {
        List<Genre> genreList = appServiceImpl.evaluate(genreDaoJdbc::getAll);
        return genreList == null ? new ArrayList<>() : genreList;
    }

    @Nullable
    @Override
    public Genre update(@NonNull Integer id, String name, String description) {
        Genre genre = null;
        if (checkServiceImpl.checkExist(() -> readById(id) != null)
                && checkServiceImpl.checkIfNotExist(() -> readByName(name) == null)) {
            String finalName = checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME) ?
                    name : genreDaoJdbc.getById(id).getName();
            String finalDescription = checkServiceImpl.isCorrectSymbolsInInputString(description, MIN_INPUT, MAX_INPUT_DESCRIPTION) ?
                    description : genreDaoJdbc.getById(id).getDescription();

            genre = appServiceImpl.evaluate(() -> genreDaoJdbc.update(id, finalName, finalDescription));
        }
        return genre;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        return checkServiceImpl.checkExist(() -> readById(id) != null)
                && appServiceImpl.evaluate(() -> genreDaoJdbc.delete(id));
    }
}

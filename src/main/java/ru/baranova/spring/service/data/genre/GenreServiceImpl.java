package ru.baranova.spring.service.data.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT_NAME = 20;
    private final static int MAX_INPUT_DESCRIPTION = 200;
    private final GenreDao genreDaoJdbc;
    private final CheckService checkService;
    private final Function<String, List<String>> nonexistentNameFn;
    private final BiFunction<Integer, Integer, Function<String, List<String>>> correctInputStrFn;
    private final Function<String, List<String>> nameMinMaxFn;
    private final Function<String, List<String>> descriptionMinMaxFn;

    public GenreServiceImpl(GenreDao genreDaoJdbc, CheckService checkService) {
        this.genreDaoJdbc = genreDaoJdbc;
        this.checkService = checkService;
        nonexistentNameFn = name -> checkService.checkIfNotExist(() -> getOrEmptyList(List.of(readByName(name))));
        correctInputStrFn =
                (minValue, maxValue) -> str -> checkService.checkCorrectInputStrLengthAndSymbols(str, minValue, maxValue);
        nameMinMaxFn = correctInputStrFn.apply(MIN_INPUT, MAX_INPUT_NAME);
        descriptionMinMaxFn = correctInputStrFn.apply(MIN_INPUT, MAX_INPUT_DESCRIPTION);
    }

    @Nullable
    @Override
    public Genre create(@NonNull String name, String description) {
        Genre genre = null;
        if (checkService.doCheck(name, nonexistentNameFn, nameMinMaxFn)) {
            genre = genreDaoJdbc.create(name
                    , checkService.correctOrDefault(description, descriptionMinMaxFn, null));
        }
        return genre;
    }

    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        return genreDaoJdbc.getById(id);
    }

    @Nullable
    @Override
    public Genre readByName(@NonNull String name) {
        return genreDaoJdbc.getByName(name);
    }

    @Override
    public List<Genre> readAll() {
        return getOrEmptyList(genreDaoJdbc.getAll());
    }

    @Nullable
    @Override
    public Genre update(@NonNull Integer id, String name, String description) {
        Genre genre = null;
        Genre genreById = genreDaoJdbc.getById(id);
        if (checkService.doCheck(genreById, checkService::checkExist)
                && checkService.doCheck(name, nonexistentNameFn)) {
            genre = genreDaoJdbc.update(id
                    , checkService.correctOrDefault(name, nameMinMaxFn, genreById::getName)
                    , checkService.correctOrDefault(description, descriptionMinMaxFn, genreById::getDescription));
        }
        return genre;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        return checkService.doCheck(genreDaoJdbc.getById(id), checkService::checkExist)
                && genreDaoJdbc.delete(id);
    }
}

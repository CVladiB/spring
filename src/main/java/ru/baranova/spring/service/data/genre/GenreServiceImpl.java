package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.genre.GenreDao;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.genre-service")
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDaoJdbc;
    private final CheckService checkService;
    @Setter
    private int minInput;
    @Setter
    private int maxInputName;
    @Setter
    private int maxInputDescription;
    private Function<String, List<String>> nonexistentNameFn;
    private Function<String, List<String>> nameMinMaxFn;
    private Function<String, List<String>> descriptionMinMaxFn;

    @PostConstruct
    private void initFunction() {
        BiFunction<Integer, Integer, Function<String, List<String>>> correctInputStrFn
                = (minValue, maxValue) -> str -> checkService.checkCorrectInputStrLengthAndSymbols(str, minValue, maxValue);
        nameMinMaxFn = correctInputStrFn.apply(minInput, maxInputName);
        descriptionMinMaxFn = correctInputStrFn.apply(minInput, maxInputDescription);
        nonexistentNameFn = name -> checkService.checkIfNotExist(() -> readByName(name));
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
    public List<Genre> readByName(@NonNull String name) {
        return genreDaoJdbc.getByName(name);
    }

    @Override
    public List<Genre> readAll() {
        return genreDaoJdbc.getAll();
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

package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
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

    @Nullable
    @Override
    public Genre create(@NonNull String name, String description) {
        init();
        Genre genre = null;

        Stream<String> nameStream = readAll().stream().map(Genre::getName);
        if (!checkServiceImpl.isInputExist(name, nameStream, null)
                && checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {
            if (!checkServiceImpl.isCorrectSymbolsInInputString(description, minInput, maxInputDescription)) {
                description = null;
            }
            genre = Genre.builder().name(name).description(description).build();
            Integer id = genreDaoJdbc.create(genre);
            genre.setId(id);
        }
        return genre;
    }

    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        Genre genre = null;

        Stream<Integer> idStream = readAll().stream().map(Genre::getId);
        try {
            if (checkServiceImpl.isInputExist(id, idStream, true)) {
                genre = genreDaoJdbc.getById(id);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }

        return genre;
    }

    @Nullable
    @Override
    public Genre readByName(@NonNull String name) {
        Genre genre = null;

        Stream<String> nameStream = readAll().stream().map(Genre::getName);
        try {
            if (checkServiceImpl.isInputExist(name, nameStream, true)) {
                genre = genreDaoJdbc.getByName(name);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        return genreDaoJdbc.getAll();
    }

    @Nullable
    @Override
    public Genre update(@NonNull Integer id, String name, String description) {
        Stream<Integer> idStream = readAll().stream().map(Genre::getId);
        Genre genre = null;

        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            init();
            genre = genreDaoJdbc.getById(id);

            if (checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {
                genre.setName(name);
            }
            if (checkServiceImpl.isCorrectSymbolsInInputString(description, minInput, maxInputDescription)) {
                genre.setDescription(description);
            }

            if (readByName(name) != null || genreDaoJdbc.update(genre) == 0) {
                genre = null;
            }
        }
        return genre;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        boolean isComplete = false;
        Stream<Integer> idStream = readAll().stream().map(Genre::getId);
        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            if (genreDaoJdbc.delete(id) > 0) {
                isComplete = true;
            }
        }
        return isComplete;
    }
}

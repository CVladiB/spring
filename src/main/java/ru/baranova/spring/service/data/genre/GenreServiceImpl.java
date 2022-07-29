package ru.baranova.spring.service.data.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.genre.GenreDao;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDaoJdbc;
    private final CheckService checkServiceImpl;
    private final ParseService parseServiceImpl;
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
        description = parseServiceImpl.parseDashToNull(description);

        Stream<String> nameStream = readAll().stream().map(Genre::getName);
        if (!checkServiceImpl.isInputExist(name, nameStream, null)) {
            if (checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)
                    && (description == null
                    || checkServiceImpl.isCorrectSymbolsInInputString(description, minInput, maxInputDescription))) {
                genre = Genre.builder().name(name).description(description).build();
                Integer id = genreDaoJdbc.create(genre);
                genre.setId(id);

            }
        }
        return genre;
    }

    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        Genre genre = null;

        Stream<Integer> idStream = readAll().stream().map(Genre::getId);
        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            genre = genreDaoJdbc.getById(id);
        }


        return genre;
    }

    @Nullable
    @Override
    public Genre readByName(@NonNull String name) {
        Genre genre = null;
        Stream<String> nameStream = readAll().stream().map(Genre::getName);
        if (checkServiceImpl.isInputExist(name, nameStream, true)) {
            genre = genreDaoJdbc.getByName(name);
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

            if (genreDaoJdbc.update(genre) == 0) {
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

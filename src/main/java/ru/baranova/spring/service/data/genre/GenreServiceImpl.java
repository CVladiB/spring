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
import ru.baranova.spring.repository.entity.GenreRepository;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.genre-service")
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
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

    @Transactional
    @Nullable
    @Override
    public Genre create(@NonNull String name, String description) {
        Genre genre = null;
        if (checkService.doCheck(name, nonexistentNameFn, nameMinMaxFn)) {
            genre = genreRepository.save(Genre.builder()
                    .name(name)
                    .description(checkService.correctOrDefault(description, descriptionMinMaxFn, null))
                    .build());
        }
        return genre;
    }

    @Transactional
    @Nullable
    @Override
    public Genre readById(@NonNull Integer id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Transactional
    @Nullable
    @Override
    public List<Genre> readByName(@NonNull String name) {
        return genreRepository.findByName(name);
    }

    @Transactional
    @Override
    public List<Genre> readAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Nullable
    @Override
    public Genre update(@NonNull Integer id, String name, String description) {
        Genre genre = null;
        Optional<Genre> genreById = genreRepository.findById(id);
        if (genreById.isPresent() && checkService.doCheck(genreById.get(), checkService::checkExist)
                && checkService.doCheck(name, nonexistentNameFn)) {
            genre = genreRepository.save(Genre.builder()
                    .id(id)
                    .name(checkService.correctOrDefault(name, nameMinMaxFn, genreById.get()::getName))
                    .description(checkService.correctOrDefault(description, descriptionMinMaxFn, genreById.get()::getDescription))
                    .build());
        }
        return genre;
    }

    @Transactional
    @Override
    public boolean delete(@NonNull Integer id) {
        Optional<Genre> genreById = genreRepository.findById(id);
        boolean isDelete = false;
        if (genreById.isPresent()) {
            genreRepository.delete(genreById.get());
            isDelete = true;
        }
        return isDelete;
    }
}

package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.repository.entity.AuthorRepository;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.author-service")
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final CheckService checkService;
    private final ParseService parseService;
    @Setter
    private int minInput;
    @Setter
    private int maxInputSurname;
    @Setter
    private int maxInputName;
    private Function<String, List<String>> surnameMinMaxFn;
    private Function<String, List<String>> nameMinMaxFn;
    private BiFunction<String, String, Function<String, List<String>>> nonexistentSurnameNameFn;

    @PostConstruct
    private void initFunction() {
        BiFunction<Integer, Integer, Function<String, List<String>>> correctInputStrFn
                = (minValue, maxValue) -> str -> checkService.checkCorrectInputStrLengthAndSymbols(str, minValue, maxValue);
        surnameMinMaxFn = correctInputStrFn.apply(minInput, maxInputSurname);
        nameMinMaxFn = correctInputStrFn.apply(minInput, maxInputName);
        nonexistentSurnameNameFn = (s, n) -> t -> checkService.checkIfNotExist(() -> readBySurnameAndName(s, n));
    }

    @Transactional
    @Nullable
    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        Author author = null;
        if (checkService.doCheck(null, nonexistentSurnameNameFn.apply(surname, name))
                && checkService.doCheck(surname, surnameMinMaxFn)
                && checkService.doCheck(name, nameMinMaxFn)) {
            author = authorRepository.save(Author.builder()
                    .surname(surname)
                    .name(name)
                    .build());
        }
        return author;
    }

    @Transactional
    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        return authorRepository.findBySurnameAndName(parseService.parseDashToNull(surname),
                parseService.parseDashToNull(name));
    }

    @Transactional
    @Override
    public List<Author> readAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Author author = null;
        Optional<Author> authorById = authorRepository.findById(id);
        if (authorById.isPresent() && checkService.doCheck(authorById.get()
                , checkService::checkExist
                , t -> nonexistentSurnameNameFn.apply(surname, name).apply(null))) {
            author = authorRepository.save(Author.builder()
                    .id(id)
                    .surname(checkService.correctOrDefault(surname, surnameMinMaxFn, authorById.get()::getSurname))
                    .name(checkService.correctOrDefault(name, nameMinMaxFn, authorById.get()::getName))
                    .build());
        }
        return author;
    }

    @Transactional
    @Override
    public boolean delete(@NonNull Integer id) {
        Optional<Author> authorById = authorRepository.findById(id);
        boolean isDelete = false;
        if (authorById.isPresent()) {
            authorRepository.delete(authorById.get());
            isDelete = true;
        }
        return isDelete;
    }
}

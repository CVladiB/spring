package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.dao.entity.author.AuthorDao;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.author-service")
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
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

    @Nullable
    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        Author author = null;
        if (checkService.doCheck(null, nonexistentSurnameNameFn.apply(surname, name))
                && checkService.doCheck(surname, surnameMinMaxFn)
                && checkService.doCheck(name, nameMinMaxFn)) {
            author = authorDao.create(surname, name);
        }
        return author;
    }

    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        return authorDao.getById(id);
    }

    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        return getOrEmptyList(authorDao.getBySurnameAndName(parseService.parseDashToNull(surname),
                parseService.parseDashToNull(name))
        );
    }

    @Override
    public List<Author> readAll() {
        return getOrEmptyList(authorDao.getAll());
    }

    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Author author = null;
        Author authorById = authorDao.getById(id);
        if (checkService.doCheck(authorById, checkService::checkExist, t -> nonexistentSurnameNameFn.apply(surname, name).apply(null))) {
            author = authorDao.update(id,
                    checkService.correctOrDefault(surname, surnameMinMaxFn, authorById::getSurname),
                    checkService.correctOrDefault(name, nameMinMaxFn, authorById::getName));
        }
        return author;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        return checkService.doCheck(authorDao.getById(id), checkService::checkExist)
                && authorDao.delete(id);
    }
}

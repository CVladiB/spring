package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT_SURNAME = 20;
    private final static int MAX_INPUT_NAME = 15;
    private final AuthorDao authorDaoJdbc;
    private final CheckService checkServiceImpl;
    private final ParseService parseServiceImpl;
    private final AppService appServiceImpl;

    @Nullable
    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        Author author = null;
        if (checkServiceImpl.checkIfNotExist(() -> readBySurnameAndName(surname, name).isEmpty())
                && checkServiceImpl.isCorrectSymbolsInInputString(surname, MIN_INPUT, MAX_INPUT_SURNAME)
                && checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
            author = appServiceImpl.evaluate(() -> authorDaoJdbc.create(surname, name));
        }
        return author;
    }

    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        return appServiceImpl.evaluate(() -> authorDaoJdbc.getById(id));
    }

    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        String finalSurname = parseServiceImpl.parseDashToNull(surname);
        String finalName = parseServiceImpl.parseDashToNull(name);

        List<Author> authorList = appServiceImpl.evaluate(() -> authorDaoJdbc.getBySurnameAndName(finalSurname, finalName));

        return authorList == null ? new ArrayList<>() : authorList;
    }

    @Override
    public List<Author> readAll() {
        List<Author> authorList = appServiceImpl.evaluate(authorDaoJdbc::getAll);
        return authorList == null ? new ArrayList<>() : authorList;
    }

    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Author author = null;
        if (checkServiceImpl.checkExist(() -> readById(id) != null)
                && checkServiceImpl.checkIfNotExist(() -> readBySurnameAndName(surname, name).isEmpty())) {
            String finalSurname2 = checkServiceImpl.isCorrectSymbolsInInputString(surname, MIN_INPUT, MAX_INPUT_SURNAME) ?
                    surname : authorDaoJdbc.getById(id).getSurname();
            String finalName2 = checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME) ?
                    name : authorDaoJdbc.getById(id).getName();

            author = appServiceImpl.evaluate(() -> authorDaoJdbc.update(id, finalSurname2, finalName2));
        }
        return author;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        return checkServiceImpl.checkExist(() -> readById(id) != null)
                && appServiceImpl.evaluate(() -> authorDaoJdbc.delete(id));
    }

}

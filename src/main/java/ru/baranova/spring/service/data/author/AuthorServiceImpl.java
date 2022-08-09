package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

    @Nullable
    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        Author author = null;
        try {
            if (checkIfNotExist(surname, name)
                    && checkServiceImpl.isCorrectSymbolsInInputString(surname, MIN_INPUT, MAX_INPUT_SURNAME)
                    && checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
                author = authorDaoJdbc.create(surname, name);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return author;
    }

    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        try {
            return authorDaoJdbc.getById(id);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }

    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        List<Author> authorList = new ArrayList<>();
        surname = parseServiceImpl.parseDashToNull(surname);
        name = parseServiceImpl.parseDashToNull(name);

        try {
            authorList = authorDaoJdbc.getBySurnameAndName(surname, name);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return authorList;
    }

    @Override
    public List<Author> readAll() {
        try {
            return authorDaoJdbc.getAll();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Author author = null;
        try {
            if (checkExist(id) && checkIfNotExist(surname, name)) {
                if (!checkServiceImpl.isCorrectSymbolsInInputString(surname, MIN_INPUT, MAX_INPUT_SURNAME)) {
                    surname = authorDaoJdbc.getById(id).getSurname();
                }
                if (!checkServiceImpl.isCorrectSymbolsInInputString(name, MIN_INPUT, MAX_INPUT_NAME)) {
                    name = authorDaoJdbc.getById(id).getName();
                }
                author = authorDaoJdbc.update(id, surname, name);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return author;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        return evaluate(() -> checkExist(id)
                && authorDaoJdbc.delete(id) > 0);
    }


    @Override
    public boolean checkExist(Integer id) {
        boolean isExist = evaluate(() -> readById(id) != null);
        if (!isExist) {
            log.info(BusinessConstants.CheckServiceLog.SHOULD_EXIST_INPUT);
        }
        return isExist;
    }

    public boolean checkIfNotExist(String surname, String name) {
        boolean isExist = evaluate(() -> readBySurnameAndName(surname, name).isEmpty());
        if (!isExist) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST);
        }
        return isExist;
    }

    public <T> T evaluate(Supplier<T> predicate) {
        try {
            return predicate.get();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return null;
    }
}

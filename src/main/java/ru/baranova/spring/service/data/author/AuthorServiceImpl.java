package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDaoJdbc;
    private final CheckService checkServiceImpl;
    private final ParseService parseServiceImpl;
    private int minInput;
    private int maxInputSurname;
    private int maxInputName;

    private void init() {
        minInput = 3;
        maxInputSurname = 20;
        maxInputName = 15;
    }

    @Nullable
    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        init();
        Author author = null;

        Stream<String> surnameStream = readAll().stream().map(Author::getSurname);
        Stream<String> nameStream = readAll().stream().map(Author::getName);
        if (!checkServiceImpl.isInputExist(surname, surnameStream, null)
                && !checkServiceImpl.isInputExist(name, nameStream, null)
                && checkServiceImpl.isCorrectSymbolsInInputString(surname, minInput, maxInputSurname)
                && checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {

            author = Author.builder().surname(surname).name(name).build();
            Integer id = authorDaoJdbc.create(author);
            author.setId(id);
        }
        return author;
    }

    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        Author author = null;

        Stream<Integer> idStream = readAll().stream().map(Author::getId);
        try {
            if (checkServiceImpl.isInputExist(id, idStream, true)) {
                author = authorDaoJdbc.getById(id);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }

        return author;
    }

    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        List<Author> authorList = new ArrayList<>();
        surname = parseServiceImpl.parseDashToNull(surname);
        name = parseServiceImpl.parseDashToNull(name);

        Stream<String> surnameStream = readAll().stream().map(Author::getSurname);
        Stream<String> nameStream = readAll().stream().map(Author::getName);
        try {
            if (surname != null && name != null) {
                if (checkServiceImpl.isInputExist(surname, surnameStream, true)
                        && checkServiceImpl.isInputExist(name, nameStream, true)) {
                    authorList = authorDaoJdbc.getBySurnameAndName(surname, name);
                }
            } else if (surname == null && name != null) {
                if (checkServiceImpl.isInputExist(name, nameStream, true)) {
                    authorList = authorDaoJdbc.getBySurnameAndName(null, name);
                }
            } else if (surname != null) {
                if (checkServiceImpl.isInputExist(surname, surnameStream, true)) {
                    authorList = authorDaoJdbc.getBySurnameAndName(surname, null);
                }
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }

        return authorList;
    }

    @Override
    public List<Author> readAll() {
        return authorDaoJdbc.getAll();
    }

    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Stream<Integer> idStream = readAll().stream().map(Author::getId);
        Author author = null;

        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            init();
            author = authorDaoJdbc.getById(id);

            if (checkServiceImpl.isCorrectSymbolsInInputString(surname, minInput, maxInputSurname)) {
                author.setSurname(surname);
            }
            if (checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {
                author.setName(name);
            }

            if (readBySurnameAndName(surname, name) != null || authorDaoJdbc.update(author) == 0) {
                author = null;
            }
        }
        return author;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        boolean isComplete = false;
        Stream<Integer> idStream = readAll().stream().map(Author::getId);
        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            if (authorDaoJdbc.delete(id) > 0) {
                isComplete = true;
            }
        }
        return isComplete;
    }
}

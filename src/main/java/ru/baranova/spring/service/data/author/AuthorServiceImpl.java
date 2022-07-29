package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDaoJdbc;
    private final CheckService checkServiceImpl;
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

        Supplier<Stream<String>> surnameStream = () -> readAll().stream().map(Author::getSurname);
        Supplier<Stream<String>> nameStream = () -> readAll().stream().map(Author::getName);
        if (!checkServiceImpl.isInputExist(surname, surnameStream.get(), null)
                && !checkServiceImpl.isInputExist(name, nameStream.get(), null)) {
            if (checkServiceImpl.isCorrectSymbolsInInputString(surname, minInput, maxInputSurname)
                    && checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {

                author = Author.builder().surname(surname).name(name).build();
                Integer id = authorDaoJdbc.create(author);
                author.setId(id);
            }
        }
        return author;
    }

    @Nullable
    @Override
    public Author readById(@NonNull Integer id) {
        Author author = null;

        Supplier<Stream<Integer>> idStream = () -> readAll().stream().map(Author::getId);
        if (checkServiceImpl.isInputExist(id, idStream.get(), true)) {
            author = authorDaoJdbc.getById(id);
        }

        return author;
    }

    @Nullable
    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        List<Author> authorList = null;
        surname = checkServiceImpl.returnNullField(surname);
        name = checkServiceImpl.returnNullField(name);

        Supplier<Stream<String>> surnameStream = () -> readAll().stream().map(Author::getSurname);
        Supplier<Stream<String>> nameStream = () -> readAll().stream().map(Author::getName);

        if (surname != null && name != null) {
            if (checkServiceImpl.isInputExist(surname, surnameStream.get(), true)
                    && checkServiceImpl.isInputExist(name, nameStream.get(), true)) {
                authorList = authorDaoJdbc.getBySurnameAndName(surname, name);
            }
        } else if (surname == null && name != null) {
            if (checkServiceImpl.isInputExist(name, nameStream.get(), true)) {
                authorList = authorDaoJdbc.getBySurnameAndName(null, name);
            }
        } else if (surname != null) {
            if (checkServiceImpl.isInputExist(surname, surnameStream.get(), true)) {
                authorList = authorDaoJdbc.getBySurnameAndName(surname, null);
            }
        }

        return authorList;
    }

    @Override
    public List<Author> readAll() {
        List<Author> authorList = authorDaoJdbc.getAll();
        return authorList;
    }

    @Nullable
    @Override
    public Author update(@NonNull Integer id, String surname, String name) {
        Supplier<Stream<Integer>> idStream = () -> readAll().stream().map(Author::getId);
        Author author = null;

        if (checkServiceImpl.isInputExist(id, idStream.get(), true)) {
            init();
            author = authorDaoJdbc.getById(id);
            surname = checkServiceImpl.returnNullField(surname);
            name = checkServiceImpl.returnNullField(name);

            if (surname != null && checkServiceImpl.isCorrectSymbolsInInputString(surname, minInput, maxInputSurname)) {
                author.setSurname(surname);
            }
            if (name != null && checkServiceImpl.isCorrectSymbolsInInputString(name, minInput, maxInputName)) {
                author.setName(name);
            }

            if (authorDaoJdbc.update(author) == 0) {
                author = null;
            }
        }
        return author;
    }

    @Nullable
    @Override
    public boolean delete(@NonNull Integer id) {
        boolean isComplete = false;
        Supplier<Stream<Integer>> idStream = () -> readAll().stream().map(Author::getId);
        if (checkServiceImpl.isInputExist(id, idStream.get(), true)) {
            if (authorDaoJdbc.delete(id) > 0) {
                isComplete = true;
            }
        }
        return isComplete;
    }
}

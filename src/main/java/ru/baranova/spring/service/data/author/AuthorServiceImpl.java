package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

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

    @Override
    public Author create(String surname, String name) {
        init();
        List<String> listSurname;
        List<String> listName;
        Author author = null;

        if (checkServiceImpl.checkCorrectInput(surname, minInput, maxInputSurname)
                && checkServiceImpl.checkCorrectInput(name, minInput, maxInputName)
                && checkServiceImpl.checkCorrectInputWordWithoutSymbol(surname)
                && checkServiceImpl.checkCorrectInputWordWithoutSymbol(name)) {
            listSurname = readAll().stream().map(Author::getSurname).toList();
            listName = readAll().stream().map(Author::getName).toList();
            if (checkServiceImpl.checkCorrectInputFromExist(surname, listSurname)
                    && checkServiceImpl.checkCorrectInputFromExist(name, listName)) {
                author = new Author();
                author.setSurname(surname);
                author.setName(name);
                Integer id = authorDaoJdbc.create(author);
                author.setId(id);
            }
        }
        return author;
    }

    @Override
    public Author readById(Integer id) {
        List<Integer> listId = readAll().stream().map(Author::getId).toList();
        Author author = null;

        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            author = authorDaoJdbc.getById(id);
        }
        return author;
    }

    @Override
    public List<Author> readBySurnameAndName(String surname, String name) {
        List<String> listSurname;
        List<String> listName;
        List<Author> authorList = null;

        if (surname != null) {
            listSurname = readAll().stream().map(Author::getSurname).toList();
            if (checkServiceImpl.checkCorrectInputFromExist(surname, listSurname) && name != null) {
                listName = readAll().stream().map(Author::getName).toList();
                if (checkServiceImpl.checkCorrectInputFromExist(name, listName)) {
                    authorList = authorDaoJdbc.getBySurnameAndName(surname, name);
                }
            }
        }
        return authorList;
    }

    @Override
    public List<Author> readAll() {
        return authorDaoJdbc.getAll();
    }

    @Override
    public Author update(Integer id, String surname, String name) {
        List<Integer> listId = readAll().stream().map(Author::getId).toList();
        Author author = null;

        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            author = authorDaoJdbc.getById(id);
            init();
            if (checkServiceImpl.checkCorrectInput(surname, minInput, maxInputSurname)
                    && checkServiceImpl.checkCorrectInputWordWithoutSymbol(surname)) {
                author.setSurname(surname);
            }
            if (checkServiceImpl.checkCorrectInput(name, minInput, maxInputName)
                    && checkServiceImpl.checkCorrectInputWordWithoutSymbol(name)) {
                author.setName(name);
            }
            authorDaoJdbc.update(author);
        }
        return author;
    }

    @Override
    public void delete(Integer id) {
        List<Integer> listId = readAll().stream().map(Author::getId).toList();
        if (checkServiceImpl.checkCorrectInputFromExist(id, listId)) {
            authorDaoJdbc.delete(id);
        }
    }
}
